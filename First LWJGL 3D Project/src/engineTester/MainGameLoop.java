package engineTester;

import java.util.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.*;
import models.*;
import renderEngine.*;
import terrains.Terrain;
import terrains.TerrainTexture;
import terrains.TerrainTexturePack;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		RawModel model = OBJLoader.loadObjModel("tree", loader);
		RawModel model1 = OBJLoader.loadObjModel("grassModel", loader);
		RawModel model2 = OBJLoader.loadObjModel("fern", loader);
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
		TexturedModel staticModel1 = new TexturedModel(model1,new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel staticModel2 = new TexturedModel(model2,new ModelTexture(loader.loadTexture("fern")));
		
		staticModel1.getTexture().setHasTransparency(true);
		staticModel2.getTexture().setHasTransparency(true);
		staticModel1.getTexture().setUseFakeLighting(true);
		staticModel2.getTexture().setUseFakeLighting(true);
		
        List<Entity> allEntities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i=0;i<500;i++){
            allEntities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
        }
        for(int i=0;i<500;i++){
            allEntities.add(new Entity(staticModel1, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,1));
        }
        for(int i=0;i<500;i++){
            allEntities.add(new Entity(staticModel2, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,1));
        }
         
        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
        
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
       
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
        
        		
        Terrain terrain = new Terrain(0,-1,loader,
        	texturePack,
        	blendMap
        );
        Terrain terrain2 = new Terrain(-1,-1,loader,
        	texturePack,
            blendMap
        );
        
        MasterRenderer renderer = new MasterRenderer();
        
        RawModel bunnyModel = OBJLoader.loadObjModel("stanfordBunny",  loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));
        
        Player player = new Player(stanfordBunny, new Vector3f(100,0,-50),0,0,0,1);
        Camera camera = new Camera(player);   
        
		while(!Display.isCloseRequested()) {
           // entity.increaseRotation(0, 1, 0);
			camera.move();
			player.move();
			
			renderer.processEntity(player);
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			for(Entity lentity: allEntities) {
				//lentity.increaseRotation(0, 1, 0);
				renderer.processEntity(lentity);
			}
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
