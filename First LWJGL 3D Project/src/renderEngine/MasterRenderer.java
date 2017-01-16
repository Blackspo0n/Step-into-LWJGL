package renderEngine;

import java.util.*;

import entities.*;
import models.TexturedModel;
import shaders.StaticShader;

public class MasterRenderer {
	
	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	
	public void render(Light sun, Camera camera) {
		renderer.prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		
		renderer.render(entities);
		
		shader.stop();
		entities.clear();
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		
		List<Entity> batch = entities.get(entityModel);
		
		
		if(batch != null) {
			List<Entity> newBatch =  new ArrayList<>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
			
		}
		else {
			
		}
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
}
