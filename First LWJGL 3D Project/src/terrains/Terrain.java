package terrains;
 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;
 
public class Terrain {
     
    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 80;
    private static final float MAX_PIXEL_COLOUR = 256* 256*256;

    
    private float x;
    private float z;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private BufferedImage heightMap;
    
    
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
 
    private RawModel generateTerrain(Loader loader, String heightMap){
    	    	
    	try {
			this.heightMap = ImageIO.read(new File("res/" + heightMap + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	int VERTEX_COUNT = this.heightMap.getHeight();
    	
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer*3+1] = getHeight(j,i);
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