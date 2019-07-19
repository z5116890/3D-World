package unsw.graphics.world;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.*;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;



/**
 * COMMENT: Comment Game
 *
 * @author malcolmr
 */
public class World extends Application3D{

    private Terrain terrain;
    private TriangleMesh terrainMesh;
    private Texture myTexture;
    private String textureFilename = "res/Textures/grass.bmp";
    private String textureExtension = "bmp";

    private WorldCamera myCamera;
    //data so we can drag around world
    private float rotateX = 0;
    private float rotateY = 0;
    private float posX = 0;
    private float posY = 0;
    private float posZ = -6;


    public World(Terrain terrain) {
    	super("Assignment 2", 800, 600);
        this.terrain = terrain;

    }

    /**
     * Load a level file and display it.
     *
     * @param args - The first argument is a level file in JSON format
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        //terrain.printAltitude();
        World world = new World(terrain);
        world.start();
    }

	@Override
	public void display(GL3 gl) {
		super.display(gl);
		CoordFrame3D frame = CoordFrame3D.identity()
                .translate(posX, posY, posZ)
                .rotateX(rotateX)
                .rotateY(rotateY);
		Shader.setViewMatrix(gl, frame.getMatrix());

		//set texture
		Shader.setInt(gl, "tex", 0); // tex in the shader is the 0'th active texture

        gl.glActiveTexture(GL.GL_TEXTURE0); // All future texture operations are
                                            // for the 0'th active texture
        gl.glBindTexture(GL.GL_TEXTURE_2D,
                myTexture.getId()); // Bind the texture id of the
                                                // texture we want to the 0th
                                                // active texture
		this.setTerrainProperties(gl, frame);
		this.myCamera.setView(gl);
		this.terrainMesh.draw(gl, frame);
		this.terrain.drawObjects(gl, frame);

	}

	//set lighting properties for terrain
	public void setTerrainProperties(GL3 gl, CoordFrame3D frame){
		//set sunlight direction
		Point3D localSunlight = frame.transform(this.terrain.getSunlight().asPoint3D());
	    Shader.setPoint3D(gl, "sunlightDirection", localSunlight);
	    Shader.setColor(gl, "sunlight", Color.WHITE);

	    // Set the lighting properties
	    //Shader.setPoint3D(gl, "lightPos", new Point3D(0, 0, 5));
	   //Shader.setColor(gl, "lightIntensity", Color.WHITE);
	    Shader.setColor(gl, "ambientIntensity", new Color(0.2f, 0.2f, 0.2f));

	    // Set the material properties
	    Shader.setColor(gl, "ambientCoeff", Color.WHITE);
	    Shader.setColor(gl, "diffuseCoeff", new Color(0.5f, 0.5f, 0.5f));
	    //no specular for grass
	    Shader.setColor(gl, "specularCoeff", new Color(0, 0, 0));
	    Shader.setFloat(gl, "phongExp", 16f);

        Shader.setPenColor(gl, Color.WHITE);
	}

	@Override
	public void destroy(GL3 gl) {
		super.destroy(gl);
		this.terrainMesh.destroy(gl);
		myTexture.destroy(gl);
		this.terrain.destroyObjects(gl);


	}

	@Override
	public void init(GL3 gl) {
		super.init(gl);

		//load textures

        myTexture = new Texture(gl, textureFilename, textureExtension, false);

		Shader shader = new Shader(gl, "shaders/vertex_directional_tex.glsl",
                "shaders/fragment_directional_tex.glsl");
		shader.use(gl);


		this.terrainMesh = terrain.makeTerrain(gl, myTexture);
		this.myCamera = new WorldCamera(terrain);
		getWindow().addKeyListener(myCamera);

	}

	@Override
	public void reshape(GL3 gl, int width, int height) {
		super.reshape(gl, width, height);
		Shader.setProjMatrix(gl, Matrix4.perspective(60, width/(float)height, 1, 100));
	}



}
