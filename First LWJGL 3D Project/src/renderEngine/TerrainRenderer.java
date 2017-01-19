package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

public class TerrainRenderer {
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		this.shader.start();
		this.shader.loadProjectionMatrix(projectionMatrix);
		this.shader.stop();
	}
	
	public void render(List<Terrain> terrians) {
		for(Terrain terrian:terrians) {
			prepareTerrian(terrian);
			loadModelMatrix(terrian);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrian.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}
	
    private void prepareTerrian(Terrain terrian) {
   	 RawModel rawModel = terrian.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        
        ModelTexture texture = terrian.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
   }
   
   private void unbindTexturedModel() {
       GL20.glDisableVertexAttribArray(0);
       GL20.glDisableVertexAttribArray(1);
       GL20.glDisableVertexAttribArray(2);
       
       GL30.glBindVertexArray(0);
   }
   
   private void loadModelMatrix(Terrain terrian) {
       Matrix4f transformationMatrix = Maths.createTransformationMatrix(
       		new Vector3f(terrian.getX(), 0, terrian.getZ()),
            0, 0, 0, 1
       );
       
       shader.loadTransformationMatrix(transformationMatrix);	   
   }
}
