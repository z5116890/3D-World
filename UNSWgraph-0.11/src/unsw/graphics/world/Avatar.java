package unsw.graphics.world;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.geometry.Point3D;

public class Avatar implements KeyListener {
    private Point3D myPos;
    private float myAngle; // Only rotate around y-axis
    private Terrain myTerrain;
    private CoordFrame3D viewFrame;

    public Avatar(Terrain terrain) {
        myPos = new Point3D(-terrain.getWidth()/2f, 0f, -terrain.getDepth()*2f);
        myAngle = 0;
        myTerrain = terrain;
        viewFrame = CoordFrame3D.identity().translate(myPos).translate(0, -1.5f, 0);
    }

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void draw(GL3 gl){
		
	}
	
}
