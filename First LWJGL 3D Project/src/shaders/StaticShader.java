package shaders;

import org.lwjgl.util.vector.Vector2f; 

public class StaticShader extends Shader3DProgramm {
    
    private static final String VERTEX_FILE = "src/shaders/vertexShader.gsh";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.gsh";

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    protected void getAllUniformLocations() {
    	super.getAllUniformLocations();
        
    	super.addUniformVariable("numberOfRows");
    	super.addUniformVariable("offset");
    }
   
    public void loadNumbersOfRows(int numberOfRows) {
    	super.loadFloat(getUniformVariable("numberOfRows"), numberOfRows);
    }
   
    public void loadOffset(float x, float y) {
    	super.loadVector(getUniformVariable("offset"), new Vector2f(x,y));
    }
}