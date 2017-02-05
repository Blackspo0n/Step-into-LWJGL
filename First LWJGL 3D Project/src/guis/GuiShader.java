package guis;
 
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
 
import shaders.ShaderProgram;
 
public class GuiShader extends ShaderProgram {
	protected Map<String, Integer> locations;
	
    private static final String VERTEX_FILE = "src/guis/guiVertexShader.gsh";
    private static final String FRAGMENT_FILE = "src/guis/guiFragmentShader.gsh";
     
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    protected void getAllUniformLocations() {
    	addUniformVariable("transformationMatrix");
    }
 
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }


    protected void addUniformVariable(String UniformName) {
    	if(locations == null) {
            locations = new HashMap<String, Integer>();
    	}
    	
    	locations.put(UniformName, super.getUniformLocation(UniformName));
    }

    protected int getUniformVariable(String UniformName) {
    	return locations.get(UniformName);
    }
    
    
    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(getUniformVariable("transformationMatrix"), matrix);
    }
}