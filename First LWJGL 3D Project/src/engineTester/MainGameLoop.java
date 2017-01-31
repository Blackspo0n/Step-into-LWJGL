package engineTester;

import java.util.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.*;
import guis.GuiRenderer;
import guis.GuiTexture;
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

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
        
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
       
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
        
        		
        Terrain terrain = new Terrain(0,-1,loader,
        	texturePack,
        	blendMap,
        	"heightmap"
        );
		
		RawModel model = OBJLoader.loadObjModel("tree", loader);
		RawModel model1 = OBJLoader.loadObjModel("grassModel", loader);
		RawModel model2 = OBJLoader.loadObjModel("fern", loader);
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
		TexturedModel staticModel1 = new TexturedModel(model1,new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel staticModel2 = new TexturedModel(model2,new ModelTexture(loader.loadTexture("fern_atlas")));
		
		staticModel1.getTexture().setHasTransparency(true);
		staticModel2.getTexture().setHasTransparency(true);
		staticModel1.getTexture().setUseFakeLighting(true);
		staticModel2.getTexture().setUseFakeLighting(true);
		
		staticModel2.getTexture().setNumberOfRows(2);
		
        List<Entity> allEntities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i=0;i<500;i++){
        	float x = random.nextFloat()*800 - 400;
        	float z = random.nextFloat() * -600;
            allEntities.add(new Entity(staticModel, new Vector3f(x,terrain.getHeightOfTerrain(x, z),z),0,0,0,5));
        }
        for(int i=0;i<500;i++){
        	float x = random.nextFloat()*800 - 400;
        	float z = random.nextFloat() * -600;
            allEntities.add(new Entity(staticModel1, new Vector3f(x,terrain.getHeightOfTerrain(x, z),z),0,0,0,1));
        }
        for(int i=0;i<500;i++){

        	float x = random.nextFloat()*800 - 400;
        	float z = random.nextFloat() * -600;
            allEntities.add(new Entity(staticModel2, random.nextInt(4), new Vector3f(x,terrain.getHeightOfTerrain(x, z),z),0,0,0,0.5f));
        }
         
        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));

        MasterRenderer renderer = new MasterRenderer();
        
        RawModel bunnyModel = OBJLoader.loadObjModel("person",  loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("playerTexture")));
        
        Player player = new Player(stanfordBunny, new Vector3f(100,0,-50),0,0,0,0.3f);
        Camera camera = new Camera(player);   
        
        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui1 = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f,0.5f), new Vector2f(0.25f,0.25f));
        GuiTexture gui2 = new GuiTexture(loader.loadTexture("thinmatrix"), new Vector2f(0.30f,0.74f), new Vector2f(0.4f,0.4f));

        guis.add(gui1);
        guis.add(gui2);
        
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        
		while(!Display.isCloseRequested()) {
           // entity.increaseRotation(0, 1, 0);
			camera.move();
			player.move(terrain);
			
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			
			for(Entity lentity: allEntities) {
				//lentity.increaseRotation(0, 1, 0);
				renderer.processEntity(lentity);
			}
			
			renderer.render(light, camera);
			guiRenderer.render(guis);
			
			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
