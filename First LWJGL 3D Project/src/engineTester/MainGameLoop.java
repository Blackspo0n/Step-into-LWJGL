package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
				
		Renderer renderer = new Renderer(shader);
		
		float[] vertices = {
				-0.5f,  0.5f, 0,
				-0.5f, -0.5f, 0,
				 0.5f, -0.5f, 0,
				 0.5f,  0.5f, 0
		};
		
		int[] indices = {
				0,1,3,
				3,1,2
		};
		
		float[] textureCoords = {
				0,0,
				0,1f,
				1f,1f,
				1f,0
		};
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0,0,-1), 0, 0, 0, 1);
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()) {
			//entity.increasePosition(0, 0, -0.1f);
			camera.move();
			
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
