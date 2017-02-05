package shaders;
 
import java.util.*;
import org.lwjgl.util.vector.*;
import toolbox.*;
import entities.*;
import renderEngine.Environment;
 
abstract public class Shader3DProgramm extends ShaderProgram {	
	protected Map<String, Integer> locations;
    
    public Shader3DProgramm(String VertexShaderFile,String FragmentShaderFile) {
        super(VertexShaderFile, FragmentShaderFile);
    }
     
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }
 
    protected void getAllUniformLocations() {
    	addUniformVariable("transformationMatrix");
    	addUniformVariable("projectionMatrix");
    	addUniformVariable("viewMatrix");
    	addUniformVariable("shineDamper");
    	addUniformVariable("reflectivity");
    	addUniformVariable("useFakeLighting");
    	addUniformVariable("skyColour");
        
        for(int i = 0; i < Environment.MAX_LIGHTS; i++) {
        	addUniformVariable("lightPosition[" + i + "]");
        	addUniformVariable("lightColour[" + i + "]");
        }
    }

    protected void addUniformVariable(String UniformName) {
    	if(locations == null) {
            locations = new HashMap<String, Integer>();
    	}
    	
    	locations.put(UniformName, super.getUniformLocation(UniformName));
    }

    protected int getUniformVariable(String UniformName) {
    	return locations.containsKey(UniformName) ? locations.get(UniformName) : 0;
    }
    
    public void loadSkyColour(float r, float g, float b) {
    	super.loadVector(getUniformVariable("skyColour"), new Vector3f(r,g,b));
    }
    
    public void loadFakeLighting(boolean usefake) {
    	super.loadBoolean(getUniformVariable("useFakeLighting"), usefake);
    } 
    
    public void loadShineVariables(float damper,float reflectivity){
        super.loadFloat(getUniformVariable("shineDamper"), damper);
        super.loadFloat(getUniformVariable("reflectivity"), reflectivity);
    }
 
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(getUniformVariable("transformationMatrix"), matrix);
    }
     
    public void loadLights(List<Light> lights){
    	for(int i = 0; i < Environment.MAX_LIGHTS; i++) {
    		if(lights.size() > i) {
    	        super.loadVector(getUniformVariable("lightPosition[" + i + "]"), lights.get(i).getPosition());
    	        super.loadVector(getUniformVariable("lightColour[" + i + "]"), lights.get(i).getColour());
    		}
    		else {
    	        super.loadVector(getUniformVariable("lightPosition[" + i + "]"),  new Vector3f(0,0,0));
    	        super.loadVector(getUniformVariable("lightColour[" + i + "]"), new Vector3f(0,0,0));
    		}
    	}
    } 
    
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(getUniformVariable("viewMatrix"), viewMatrix);
    }
    

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(getUniformVariable("projectionMatrix"), projection);
    }
}