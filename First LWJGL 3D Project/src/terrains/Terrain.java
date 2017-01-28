package terrains;
 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;
import toolbox.Maths;
 
public class Terrain {
     
    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 40;
    private static final float MAX_PIXEL_COLOUR = 256 * 256*256;

    
    private float x;
    private float z;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private BufferedImage heightMap;
    
    
    private float[][] heights;
    
    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap){
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader, heightMap);
    }
     
    
    
    public TerrainTexturePack getTexturePack() {
		return texturePack;
	}



	public void setTexturePack(TerrainTexturePack texturePack) {
		this.texturePack = texturePack;
	}



	public TerrainTexture getBlendMap() {
		return blendMap;
	}



	public void setBlendMap(TerrainTexture blendMap) {
		this.blendMap = blendMap;
	}



	public static float getSize() {
		return SIZE;
	}




	public void setX(float x) {
		this.x = x;
	}



	public void setZ(float z) {
		this.z = z;
	}



	public void setModel(RawModel model) {
		this.model = model;
	}



	public float getX() {
        return x;
    }
 
    public float getZ() {
        return z;
    }
 
    public RawModel getModel() {
        return model;
    }
    
    private Vector3f calculateNormal(int x, int z) {
    	float normalL = getHeight(x-1, z);
    	float normalR = getHeight(x+1, z);
    	float normalD = getHeight(x, z-1);
    	float normalU = getHeight(x, z+1);
    	
    	Vector3f normal = new Vector3f(normalL-normalR,2f, normalD - normalU);
    	
    	normal.normalise();
    	return normal;
    }
 
    public float getHeightOfTerrain(float worldX, float worldZ) {
    	float terrainX = worldX - this.x;
    	float terrainZ = worldZ - this.z;
    	
    	float gridSquareSize = SIZE / ((float)heights.length -1);
    	int gridX = (int)Math.floor(terrainX / gridSquareSize);
    	int gridZ = (int)Math.floor(terrainZ / gridSquareSize);
    	
    	if(gridX >= heights.length -1 || gridZ >= heights.length -1 || gridX < 0 || gridZ < 0) {
    		return 0;
    	}

    	float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
    	float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
    	
    	float answer;
    	
    	if (xCoord <= (1-zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
    	
    	return answer;
    	
    }
    
    private RawModel generateTerrain(Loader loader, String heightMap){
    	    	
    	try {
			this.heightMap = ImageIO.read(new File("res/" + heightMap + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	int VERTEX_COUNT = this.heightMap.getHeight();
    	heights = new float[VERTEX_COUNT][VERTEX_COUNT];
    	
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                
                float height = getHeight(j, i);
                
                heights[j][i] = height;
                
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                
                Vector3f norm = calculateNormal(j,i);
                
                normals[vertexPointer*3] = norm.x;
                normals[vertexPointer*3+1] = norm.y;
                normals[vertexPointer*3+2] = norm.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }
    
    private float getHeight(int x, int z) {

    	if(x < 0 || x >= heightMap.getHeight() || z < 0 || z >= heightMap.getHeight()) {
    		return 0;
    	}
    	
    	float height = heightMap.getRGB(x, z);
    	
    	height += MAX_PIXEL_COLOUR/2;
    	height /= MAX_PIXEL_COLOUR/2;
    	height *= MAX_HEIGHT;
    	
    	return height;
    	
    }
 
}