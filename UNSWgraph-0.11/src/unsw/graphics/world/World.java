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
 * This project at this stage is able to show the terrain
 * with trees based on the input level file, it is able
 * to be controlled by mouse for rotation. It can also
 * been controlled by keyboard direction keys to rotate
 * through y-axis and move forward and backward
 *
 * Both the terrain and trees are being textured using
 * different pattern
 *
 * @author malcolmr
 */
public class World extends Application3D implements MouseListener, KeyListener {

    private Terrain terrain;
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
     * @throws FileNotFoundException change run configurations
     */
    public static void main(String[] args) throws IOException {
        Terrain terrain = LevelIO.load(new File(args[0]));
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
    }


    @Override
    public void destroy(GL3 gl) {
        super.destroy(gl);
        this.terrain.destroyObjects(gl);
    }

    @Override
    public void init(GL3 gl) {
        super.init(gl);

        Shader shader = new Shader(gl, "shaders/vertex_directional_tex.glsl",
                "shaders/fragment_directional_tex.glsl");

        getWindow().addMouseListener(this);

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

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
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

    //helps to zoom in and out
    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            posX -= 1;
        }

        if (key == KeyEvent.VK_RIGHT) {
            posX += 1;
        }

        if (key == KeyEvent.VK_UP) {
            posY += 1;
        }

        if (key == KeyEvent.VK_DOWN) {
            posY -= 1;
        }

        //zoom in
        if (key == KeyEvent.VK_X) {
            posZ += 1;
        }

        //zoom out
        if (key == KeyEvent.VK_Z) {
            posZ -= 1;
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }


}
