package unsw.graphics.world;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private int width;
    private int depth;
    private float[][] altitudes;
    private List<Tree> trees;
    private List<Road> roads;
    private Vector3 sunlight;

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth, Vector3 sunlight) {
        this.width = width;
        this.depth = depth;
        altitudes = new float[width][depth];
        trees = new ArrayList<Tree>();
        roads = new ArrayList<Road>();
        this.sunlight = sunlight;
    }

    public List<Tree> trees() {
        return trees;
    }

    public List<Road> roads() {
        return roads;
    }

    public Vector3 getSunlight() {
        return sunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        sunlight = new Vector3(dx, dy, dz);      
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return altitudes[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, float h) {
        altitudes[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * @param x
     * @param z
     * @return
     */
    public float altitude(float x, float z) {
        float altitude = 0;

        // TODO: Implement this
        //next array corresponds to z
        //next element corresponds to x
        //y corresponds to altitude
        altitude = this.altitudes[(int) z][(int) x];
        
        return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(float x, float z) {
        float y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        trees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(float width, List<Point2D> spine) {
        Road road = new Road(width, spine);
        roads.add(road);        
    }
    
    
    //compute points from 2d array altitude
    public TriangleMesh drawTerrain(){
        List<Point3D> vertices = new ArrayList<Point3D>();
        List<Integer> indices = new ArrayList<Integer>();
        
        //vertices
        for(int i = 0; i < this.depth; i++) {
            for(int j = 0; j < this.width; j++) {
                vertices.add(new Point3D(j,this.altitude(j, i), i));
            }
        }
        
        //faces
        for(int i = 1; i < this.width; i++){
        	//first triangle of quad
        	int startTri1 = i * this.width;
            int middleTri1 = startTri1 + 1;
            int endTri1 = (i - 1) * this.width + 1;
            //second triangle of quad
            int startTri2 = (i - 1) * this.width + 1;
            int middleTri2 = (i - 1) * this.width;
            int endTri2 = i * this.width;
            for(int j = 1; j < this.depth - 1; j++) {
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
        TriangleMesh terrain = new TriangleMesh(vertices, indices, true);
        return terrain;
        
    }
    
    //prints altitude
    public void printAltitude(){
    	System.out.println(Arrays.deepToString(this.altitudes));
    	System.out.println(this.altitude(5, 4));
    	System.out.println(this.width);
    	System.out.println(this.depth);
    	this.drawTerrain();
    }

}
