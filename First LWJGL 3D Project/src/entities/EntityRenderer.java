package entities;
 
import models.*;
import renderEngine.MasterRenderer;

import java.util.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;
import entities.*;
 
public class EntityRenderer {
    private static StaticShader shader = null;
    
    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
 
    
    public void render(Map<TexturedModel, List<Entity>> entities) {

    	for(TexturedModel model: entities.keySet()) {
    		this.prepareTexturedModel(model);
    		
    		List<Entity> batch = entities.get(model);
    		
    		for(Entity entity:batch) {
    			prepareInstance(entity);
    			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
    		}
    		
    		unbindTexturedModel();
    	}
    }
    
    private void prepareTexturedModel(TexturedModel model) {
    	 RawModel rawModel = model.getRawModel();
         GL30.glBindVertexArray(rawModel.getVaoID());
         
         GL20.glEnableVertexAttribArray(0);
         GL20.glEnableVertexAttribArray(1);
         GL20.glEnableVertexAttribArray(2);
         
         ModelTexture texture = model.getTexture();
         
         shader.loadNumbersOfRows(texture.getNumberOfRows());
         
         if(texture.isHasTransparency()) {
        	 MasterRenderer.disableBackfaceCulling();
         }
         
         
         shader.loadFakeLighting(texture.isUseFakeLighting());
         shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
         
         GL13.glActiveTexture(GL13.GL_TEXTURE0);
         GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

    }
    
    private void unbindTexturedModel() {
    	MasterRenderer.enableBackfaceCulling();
    	
        
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        
        GL30.glBindVertexArray(0);
    }
    
    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
        		entity.getPosition(),
                entity.getRotX(), 
                entity.getRotY(), 
                entity.getRotZ(), 
                entity.getScale()
        );
        
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
        shader.loadTransformationMatrix(transformationMatrix);	   
    }
}