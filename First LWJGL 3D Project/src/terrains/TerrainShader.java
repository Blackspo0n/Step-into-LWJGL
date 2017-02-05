package terrains;
 
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;
import entities.Camera;
import entities.Light;
import shaders.Shader3DProgramm;
import shaders.ShaderProgram;
 
public class TerrainShader extends Shader3DProgramm {
    private static final String VERTEX_FILE = "src/terrains/terrainVertexShader.gsh";
    private static final String FRAGMENT_FILE = "src/terrains/terrainFragmentShader.gsh";

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    protected void getAllUniformLocations() {
    	super.getAllUniformLocations();
    	
        addUniformVariable("backgroundTexture");
        addUniformVariable("rTexture");
        addUniformVariable("gTexture");
        addUniformVariable("bTexture");
        addUniformVariable("blendMap");
    }
   
    public void connectTextureUnits() {
    	super.loadInt(getUniformVariable("backgroundTexture"), 0);
    	super.loadInt(getUniformVariable("rTexture"), 1);
    	super.loadInt(getUniformVariable("gTexture"), 2);
    	super.loadInt(getUniformVariable("bTexture"), 3);
    	super.loadInt(getUniformVariable("blendMap"), 4);
    }
}