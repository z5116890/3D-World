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
public class Tree{

    private Point3D position;
    private Texture myTexture;
    private static TriangleMesh myTree;

    public Tree(float x, float y, float z) throws IOException {
        myTree = new TriangleMesh("res/models/tree.ply", true, true);
        position = new Point3D(x, y, z);
    }
    
    public Point3D getPosition() {
        return position;
    }

    public void drawSelf(GL3 gl, CoordFrame3D frame) {
//        gl.glEnable(GL3.GL_CULL_FACE);
        Shader.setInt(gl, "tex", 0);

        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, myTexture.getId());
        Shader.setPenColor(gl, Color.WHITE);
        myTree.draw(gl, frame.translate(position).translate(0,0.5f,0).scale(0.1f,0.1f,0.1f));
    }

    public void init(GL3 gl) {
        myTree.init(gl);
        String textureFilename = "res/Textures/tree.jpg";
        String textureExtension = "jpg";
        myTexture = new Texture(gl, textureFilename, textureExtension, false);

    }

    public void destroy(GL3 gl){
        myTree.destroy(gl);
    }

    

}
