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
        
        if(x == Math.ceil(x) && z == Math.ceil(z)){
        	return this.altitudes[(int) x][(int) z];
        }
        //x left
        int x1 = (int) Math.floor(x);
        //x right
        int x2 = (int) Math.ceil(x);
        //z towards negative
        int z1 = (int) Math.floor(x);
        //z towards positive
        int z2 = (int) Math.ceil(x);
        
        //grid made of 2 triangles is made of (x1, z1)---(x2, z1)
        //                                    |                 |
        //      							  |                 |
        //									  (x1, z2)---(x2, z2)
        
        
        return altitude;
    }
    
    public double area(int x1, int z1, int x2, int z2, int x3, int z3) { 
    	return Math.abs((x1*(z2-z3) + x2*(z3-z1)+ x3*(z1-z2))/2.0); 
    } 
    
    //given a point and 3 points of triangle, check to see if point lies inside triangle by calculating area.
    public boolean inside(int x1, int y1, int x2,int y2, int x3, int y3, int x, int y){
    	// Calculate total area of triangle 
        double A = area (x1, y1, x2, y2, x3, y3); 
       
       // Calculate area of triangle 1 with given point as one of the vertices.
        double A1 = area (x, y, x2, y2, x3, y3); 
       
       // Calculate area of triangle 2 with given point as one of the vertices.
        double A2 = area (x1, y1, x, y, x3, y3); 
       
      // Calculate area of triangle 3 with given point as one of the vertices.
        double A3 = area (x1, y1, x2, y2, x, y); 
         
       // Check if sum of triangles 1,2 and 3 is same as total area of trianlge
        return (A == A1 + A2 + A3); 
    
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
    public TriangleMesh makeTerrain(GL3 gl){
        List<Point3D> vertices = new ArrayList<Point3D>();
        List<Integer> indices = new ArrayList<Integer>();
        
        
        //vertices
        for(int i = 0; i < this.depth; i++) {
            for(int j = 0; j < this.width; j++) {
                vertices.add(new Point3D(j,this.altitudes[j][i], i));
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
            for(int j = 1; j < this.depth; j++) {
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
        System.out.println(indices);
        TriangleMesh terrain = new TriangleMesh(vertices, indices, false);
        terrain.init(gl);
        
        return terrain;
        
    }
    
    //prints altitude
    public void printAltitude(GL3 gl){
    	System.out.println(Arrays.deepToString(this.altitudes));
    	System.out.println(this.altitude(5, 4));
    	System.out.println(this.width);
    	System.out.println(this.depth);
    }

}
