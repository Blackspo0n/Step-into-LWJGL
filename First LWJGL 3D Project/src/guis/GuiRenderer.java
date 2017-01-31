package guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;

import models.RawModel;
import renderEngine.Loader;
import shaders.StaticShader;
import toolbox.Maths;

public class GuiRenderer {

	private final RawModel quad;
	private Loader loader;
	private GuiShader shader;
	
	public GuiRenderer(Loader loader) {
		this.loader = loader;
		
		float[] positions = {
				-1, 1,
				-1,-1,
				 1, 1,
				 1,-1
		};
		
		this.quad = loader.loadToVAO(positions);
		shader = new GuiShader();
		
	}
	
	public void render(List<GuiTexture> guis) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		//render
		for(GuiTexture gui: guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());

			shader.loadTransformation(Maths.createTransformationMatrix(gui.getPosition(),gui.getScale()));
			
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
