package unsw.graphics.world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Texture;
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
    private Texture myTexture;
    private String textureFilename = "res/Textures/tree.jpg";
    private String textureExtension = "jpg";

    public Tree(float x, float y, float z) throws IOException {
        super("res/models/tree.ply", true);
        position = new Point3D(x, y, z);
    }
    
    public Point3D getPosition() {
        return position;
    }

    public void drawSelf(GL3 gl, CoordFrame3D frame) {
        gl.glEnable(GL3.GL_CULL_FACE);
//        Shader.setPenColor(gl, Color.GREEN);
        Shader.setInt(gl, "tex", 0);

        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, myTexture.getId());

        Shader.setPenColor(gl, Color.WHITE);
        this.draw(gl, frame.translate(position).translate(0,0.5f,0).scale(0.1f,0.1f,0.1f));
    }

    public void setTexture(GL3 gl){
        this.init(gl);
        myTexture = new Texture(gl, textureFilename, textureExtension, false);

    }

    

}
