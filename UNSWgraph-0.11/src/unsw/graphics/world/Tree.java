package unsw.graphics.world;

import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

import java.awt.*;
import java.io.IOException;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree extends TriangleMesh{

    private Point3D position;


    public Tree(float x, float y, float z) throws IOException {
        super("res/models/tree.ply", true);
        position = new Point3D(x, y, z);
    }
    
    public Point3D getPosition() {
        return position;
    }

    public void drawSelf(GL3 gl, CoordFrame3D frame) {
        gl.glEnable(GL3.GL_CULL_FACE);
        Shader.setPenColor(gl, Color.GREEN);
        this.draw(gl, frame.translate(position).translate(0,0.5f,0).scale(0.1f,0.1f,0.1f));
    }

    

}
