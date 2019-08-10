package unsw.graphics.world;

import java.io.IOException;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL3;
import unsw.graphics.*;
import java.awt.Color;

import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;

public class Avatar extends TriangleMesh{
    private Point3D myPos;
    private Boolean show;
    private float myAngle;
    private CoordFrame3D frame = CoordFrame3D.identity();

    public Avatar(float x, float y, float z) throws IOException {
    	super("res/models/wolf.ply", true);
    	this.myPos = new Point3D(x,y,z);
        show = false;
    }
    
    
    public Boolean getShow(){
    	return this.show;
    }
    
    public void setShow(Boolean show){
    	this.show = show;
    }
    
    public void setAngle(float angle){
    	this.myAngle += angle;
    }
    
    //move avatar down
    public void setPosDown(float angle){
    	myPos = myPos.translate(.1f * (float) Math.sin(Math.toRadians(-angle)) * 5, 0, .1f * (float) Math.cos(Math.toRadians(-angle)) * 5);
    }
  //move avatar up
    public void setPosUp(float angle){
    	myPos = myPos.translate(-.1f * (float) Math.sin(Math.toRadians(-angle)) * 5, 0, -.1f * (float) Math.cos(Math.toRadians(-angle)) * 5);
    }
    
    
    public void drawSelf(GL3 gl){
    	//configure to make avatar climb more aesthetically
        //For cube
//    	CoordFrame3D modelFrame = frame.translate(myPos).translate(0, 0.5f, 0).rotateY(myAngle);
        //For wolf //translate(0, 0.3f, 3.5f)
        CoordFrame3D modelFrame = frame.translate(myPos).rotateY(myAngle)
                .rotateX(100).rotateY(180);
        Shader.setPenColor(gl, Color.BLUE);
        this.draw(gl, modelFrame);
    	
    }
    
    public Point3D getPosition() {
        return myPos;
    }
    
    public float getAngle(){
    	return myAngle;
    }
    
    public void setPos(Point3D pos){
    	this.myPos = pos;
    }
    
    public void setFrame(CoordFrame3D frame){
    	this.frame = frame;
    }

	
}
