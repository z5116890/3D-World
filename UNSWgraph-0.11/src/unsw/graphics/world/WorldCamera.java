package unsw.graphics.world;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Point3D;

public class WorldCamera implements KeyListener {
    private Point3D myPos;
    private float myAngle; // Only rotate around y-axis
    private Terrain myTerrain;
    private CoordFrame3D viewFrame;

    public WorldCamera(Terrain terrain) {
        myPos = new Point3D(-terrain.getWidth()/2f, 0f, -terrain.getDepth()*2f);
        myAngle = 0;
        myTerrain = terrain;
        viewFrame = CoordFrame3D.identity().translate(myPos).translate(0, -1, 0);
    }

    public void setView(GL3 gl) {
        Shader.setViewMatrix(gl, viewFrame.getMatrix());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                myAngle -= 5;
                break;

            case KeyEvent.VK_RIGHT:
                myAngle += 5;
                break;

            case KeyEvent.VK_DOWN:
                myPos = myPos.translate(-.1f * (float) Math.sin(Math.toRadians(-myAngle)) * 5, 0, -.1f * (float) Math.cos(Math.toRadians(-myAngle)) * 5);
                break;

            case KeyEvent.VK_UP:
                myPos = myPos.translate(.1f * (float) Math.sin(Math.toRadians(-myAngle)) * 5, 0, .1f * (float) Math.cos(Math.toRadians(-myAngle)) * 5);
                break;
        }
        if (-myPos.getX() >= 0 && -myPos.getX() <= myTerrain.getWidth() - 1 && -myPos.getZ() >= 0 && -myPos.getZ() <= myTerrain.getDepth() - 1) {
            myPos = new Point3D(myPos.getX(), (float) -myTerrain.getGridAltitude(Math.round(-myPos.getX()), Math.round(-myPos.getZ())), myPos.getZ());
        }
        viewFrame = CoordFrame3D.identity().rotateY(myAngle);
        viewFrame = viewFrame.translate(myPos).translate(0, -1, 0);

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}