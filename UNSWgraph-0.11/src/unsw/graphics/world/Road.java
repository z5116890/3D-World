package unsw.graphics.world;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

/**
 * COMMENT: Comment Road
 *
 * @author malcolmr
 */
public class Road {

    private List<Point2D> points;
    private float width;
    private float height;
    private Texture roadTexture;
    private static final int midPoints = 16;
    private TriangleMesh roadMesh;
    private Point2D prevT;

    /**
     * Create a new road with the specified spine
     *
     * @param width
     * @param spine
     */
    public Road(float width, List<Point2D> spine, float height) {
        this.width = width;
        this.points = spine;
        this.height = height;
    }

    /**
     * The width of the road.
     *
     * @return
     */
    public double width() {
        return width;
    }

    /**
     * The altitude of the road.
     *
     * @return
     */

    public float getHeight() {
        return height;
    }

    /**
     * Get the number of segments in the curve
     *
     * @return
     */
    public int size() {
        return points.size() / 3;
    }

    /**
     * Get the specified control point.
     *
     * @param i
     * @return
     */
    public Point2D controlPoint(int i) {
        return points.get(i);
    }

    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     *
     * @param t
     * @return
     */
    public Point2D point(float t) {
        int i = (int) Math.floor(t);
        i -= (i != 0 && t == i) ? 1 : 0;
        t = t - i;

        i *= 3;

        Point2D p0 = points.get(i++);
        Point2D p1 = points.get(i++);
        Point2D p2 = points.get(i++);
        Point2D p3 = points.get(i++);


        float x = b(0, t) * p0.getX() + b(1, t) * p1.getX() + b(2, t) * p2.getX() + b(3, t) * p3.getX();
        float y = b(0, t) * p0.getY() + b(1, t) * p1.getY() + b(2, t) * p2.getY() + b(3, t) * p3.getY();

        return new Point2D(x, y);
    }

    /**
     * Calculate the Bezier coefficients
     *
     * @param i
     * @param t
     * @return
     */
    private float b(int i, float t) {

        switch (i) {

            case 0:
                return (1 - t) * (1 - t) * (1 - t);

            case 1:
                return 3 * (1 - t) * (1 - t) * t;

            case 2:
                return 3 * (1 - t) * t * t;

            case 3:
                return t * t * t;
        }

        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

    public Point2D tangent(float t) {
        int i = (int) Math.floor(t);
        i -= (i != 0 && t == i) ? 1 : 0;
        t = t - i;

        i *= 3;

        Point2D p0 = points.get(i++);
        Point2D p1 = points.get(i++);
        Point2D p2 = points.get(i++);
        Point2D p3 = points.get(i++);

        float x = tangentB(0, t) * p0.getX() + tangentB(1, t) * p1.getX() + tangentB(2, t) * p2.getX() + tangentB(3, t) * p3.getX();
        float y = tangentB(0, t) * p0.getY() + tangentB(1, t) * p1.getY() + tangentB(2, t) * p2.getY() + tangentB(3, t) * p3.getY();

        //Find the slope of line vertical to the tangent line, with magnitude of half the road width
        //Therefore in the future steps in init, we can add and minus this magnitude around the bezier
        //points to get a road with required width
        if(x == 0) {
            x = (float) (width * 0.5);
            y = 0;
        } else if (y == 0){
            y = (float) (width * 0.5);
            if(x>0){
                y = -y;
            }
            x = 0;
        } else{
            double k = -x/y;
            double angle = Math.atan(k);
            x = (float) (width*0.5*Math.cos(angle));
            y = (float) (width*0.5*Math.sin(angle));
        }

        return new Point2D(x, y);
    }

    private float tangentB(int i, float t) {
        switch (i) {

            case 0:
                return -3 * (t - 1) * (t - 1);

            case 1:
                return 3 * (t - 1) * (3 * t - 1);

            case 2:
                return 6 * t - 9 * t * t;

            case 3:
                return 3 * t * t;

        }

        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

    public void init(GL3 gl) {
        roadTexture = new Texture(gl, "res/Textures/road.png", "png", false);
        List<Point3D> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Point2D> textCoords = new ArrayList<>();

        for (float i = 0; i <= midPoints * size(); ++i) {
            Point2D p = point(i / midPoints);
            Point2D tanP = tangent(i / midPoints);
            vertices.add(new Point3D(p.getX() + tanP.getX(), height, p.getY() + tanP.getY()));
            vertices.add(new Point3D(p.getX() - tanP.getX(), height, p.getY() - tanP.getY()));
        }
        for (int i = 0; i < vertices.size() - 3; i += 2) {
            indices.addAll(Arrays.asList(i, i + 1, i + 2, i + 1, i + 3, i + 2));
        }
        for (int i = 0; i < vertices.size() / 2; ++i) {
            textCoords.add(new Point2D(0, i));
            textCoords.add(new Point2D(1, i));
        }

        roadMesh = new TriangleMesh(vertices, indices, true, textCoords);
        roadMesh.init(gl);

    }

    public void drawSelf(GL3 gl, CoordFrame3D frame) {
        Shader.setInt(gl, "tex", 0);

        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, roadTexture.getId());
        Shader.setPenColor(gl, Color.WHITE);
        roadMesh.draw(gl, frame.translate(0, 0.01f, 0));

    }

    public void destroy(GL3 gl) {
        roadMesh.destroy(gl);
    }
}
