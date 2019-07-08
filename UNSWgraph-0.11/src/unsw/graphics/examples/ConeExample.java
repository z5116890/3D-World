package unsw.graphics.examples;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.GL3;

import unsw.graphics.Application3D;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

/**
 * Create a cone by making a circle out of triangles and then changing the z
 * coordinate of the middle point.
 *
 * @author Robert Clifton-Everest
 */

public class ConeExample extends Application3D implements MouseListener{


    private float rotateX = 0;
    private float rotateY = 0;
    private Point2D myMousePoint = null;
    private static final int ROTATION_SCALE = 1;
    private static final int NUM_SLICES = 64;

    private float height = 2;
    private float radius = 0.5f;
    private TriangleMesh cone;

    public ConeExample() {
        super("Cone example", 800, 800);
    }

    public static void main(String[] args) {
        ConeExample example = new ConeExample();
        example.start();
    }

    @Override
    public void reshape(GL3 gl, int width, int height) {
        super.reshape(gl, width, height);
        Shader.setProjMatrix(gl, Matrix4.perspective(60, 1, 1, 100));
    }

    @Override
    public void init(GL3 gl) {
        super.init(gl);
        getWindow().addMouseListener(this);

        Shader shader = new Shader(gl, "shaders/vertex_phong.glsl",
                "shaders/fragment_phong.glsl");
        shader.use(gl);

        // Set the lighting properties
        Shader.setPoint3D(gl, "lightPos", new Point3D(0, 0, 5));
        Shader.setColor(gl, "lightIntensity", Color.WHITE);
        Shader.setColor(gl, "ambientIntensity", new Color(0.2f, 0.2f, 0.2f));

        // Set the material properties
        Shader.setColor(gl, "ambientCoeff", Color.WHITE);
        Shader.setColor(gl, "diffuseCoeff", new Color(0.5f, 0.5f, 0.5f));
        Shader.setColor(gl, "specularCoeff", new Color(0.8f, 0.8f, 0.8f));
        Shader.setFloat(gl, "phongExp", 16f);

        cone = makeCone(gl);
    }

    private float getX(float t){
    	return (float) (radius* Math.cos(2 * Math.PI * t));

    }

    private float getY(float t){
    	return (float) (radius* Math.sin(2 * Math.PI * t));

    }

    @Override
    public void display(GL3 gl) {
        super.display(gl);

        CoordFrame3D frame = CoordFrame3D.identity()
                .translate(0, 0, -6)
                .rotateX(rotateX)
                .rotateY(rotateY);

        Shader.setPenColor(gl, Color.GRAY);
        cone.draw(gl, frame);
    }

    private TriangleMesh makeCone(GL3 gl) {
        // Make the approximating triangular mesh.
    	List<Point3D> vertices = new ArrayList<Point3D>();
        List<Integer> indices = new ArrayList<Integer>();
        float[][] altitudes = {{0, 0, 0, 0, 0},
                				{0, 0, 0.5f, 1, 0},
                				{0, 0.5f, 1, 2, 0},
                				{0, 0, 0.5f, 1, 0},
                				{0, 0, 0, 0, 0}};
        
        //vertices
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                vertices.add(new Point3D(j,altitudes[j][i], i));
            }
        }
        
        //faces
        for(int i = 1; i < 5; i++){
        	//first triangle of quad
        	int startTri1 = i * 5;
            int middleTri1 = startTri1 + 1;
            int endTri1 = (i - 1) * 5 + 1;
            //second triangle of quad
            int startTri2 = (i - 1) * 5 + 1;
            int middleTri2 = (i - 1) * 5;
            int endTri2 = i * 5;
            for(int j = 1; j < 5; j++) {
            	//add points for 1st triangle
	        	indices.add(startTri1++);
	        	indices.add(middleTri1++);
	        	indices.add(endTri1++);
	        	//add points for 2 triangle to form quad
	        	indices.add(startTri2++);
	        	indices.add(middleTri2++);
	        	indices.add(endTri2++);
            }
        }
        System.out.println(indices);
        TriangleMesh terrain = new TriangleMesh(vertices, indices, true);
        terrain.init(gl);
        //terrain.draw(gl);
        return terrain;
    }


	@Override
	public void mouseDragged(MouseEvent e) {
		Point2D p = new Point2D(e.getX(), e.getY());

        if (myMousePoint != null) {
            float dx = p.getX() - myMousePoint.getX();
            float dy = p.getY() - myMousePoint.getY();

            // Note: dragging in the x dir rotates about y
            //       dragging in the y dir rotates about x
            rotateY += dx * ROTATION_SCALE;
            rotateX += dy * ROTATION_SCALE;

        }
        myMousePoint = p;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		 myMousePoint = new Point2D(e.getX(), e.getY());
	}

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseWheelMoved(MouseEvent e) { }

    @Override
    public void destroy(GL3 gl) {
        super.destroy(gl);
        cone.destroy(gl);
    }
}
