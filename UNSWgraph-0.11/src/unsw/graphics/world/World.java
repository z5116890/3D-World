package unsw.graphics.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.GL3;

import unsw.graphics.*;
import unsw.graphics.geometry.Point2D;



/**
 * COMMENT: Comment Game
 *
 * @author malcolmr
 */
public class World extends Application3D{

    private Terrain terrain;
    private WorldCamera myCamera;

    private WorldCamera myCamera;
    //data so we can drag around world
    private float rotateX = 0;
    private float rotateY = 0;
    private float posX = 0;
    private float posY = 0;
    private float posZ = 0;
    private Point2D myMousePoint = null;
    private static final int ROTATION_SCALE = 1;


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

        this.myCamera.setView(gl);
        this.terrain.drawSelf(gl, frame);
        this.terrain.drawObjects(gl, frame);

    }


    @Override
    public void destroy(GL3 gl) {
        super.destroy(gl);
//        this.terrainMesh.destroy(gl);
        this.terrain.destroyObjects(gl);
//        myTexture.destroy(gl);
    }

		//load textures

        Shader shader = new Shader(gl, "shaders/vertex_directional_tex.glsl",
                "shaders/fragment_directional_tex.glsl");

        getWindow().addMouseListener(this);
//        getWindow().addKeyListener(this);

        terrain.makeTerrain(gl);
        this.myCamera = new WorldCamera(terrain);
        getWindow().addKeyListener(myCamera);
        shader.use(gl);
    }

    @Override
    public void reshape(GL3 gl, int width, int height) {
        super.reshape(gl, width, height);
        Shader.setProjMatrix(gl, Matrix4.perspective(60, width / (float) height, 1, 100));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        myMousePoint = new Point2D(e.getX(), e.getY());
    }

	}

	@Override
	public void reshape(GL3 gl, int width, int height) {
		super.reshape(gl, width, height);
		Shader.setProjMatrix(gl, Matrix4.perspective(60, width/(float)height, 1, 100));
	}



}
