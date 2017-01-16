package engineTester;

import java.util.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.*;
import models.*;
import renderEngine.*;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		RawModel model = OBJLoader.loadObjModel("stall", loader);

		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white")));
		ModelTexture texture = texturedModel.getTexture();
		
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0,0,-25), 0, 0, 0, 1);
		
		Light light = new Light(new Vector3f(200, 200, 100), new Vector3f(1,1,1f));
		Camera camera = new Camera();
		
		MasterRenderer masterrenderer = new MasterRenderer();
		
		List<Entity> allEntities = new ArrayList<>();
		
		
		while(!Display.isCloseRequested()) {
           // entity.increaseRotation(0, 1, 0);
			camera.move();
			
			for(Entity lentity: allEntities) {
				masterrenderer.processEntity(lentity);
			}
			
			masterrenderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		masterrenderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
