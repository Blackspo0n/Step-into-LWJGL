package renderEngine;

import java.io.*;
import java.util.*;
import org.lwjgl.util.vector.*;
import models.RawModel;

public class OBJLoader {
	
	public static RawModel loadObjModel(String fileName, Loader loader) {
		FileReader fr = null;
		
		try {
			fr = new FileReader(new File("res/" + fileName + ".obj"));
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			System.exit(-1);
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line;

		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();

		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] texturesArray = null;
		int[] indicesArray = null;
		
		try {
			while(true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				
				if(line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(
							Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3])
					);
					vertices.add(vertex);
				}
				else if(line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(
							Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2])
					);
					textures.add(texture);
				}
				else if(line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(
							Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3])
					);	
					normals.add(normal);
				}
				else if(line.startsWith("f ")) {
					texturesArray = new float[vertices.size()*2];
					normalsArray = new float[vertices.size()*3];
					verticesArray = new float[vertices.size()*3];
					
					break;
				}
				else {
					System.err.println("unusual line:" + line);
				}				
			}
			
			while(line != null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				
				String[] currentline = line.split(" ");
				String[] vertex1 = currentline[1].split("/");
				String[] vertex2 = currentline[2].split("/");
				String[] vertex3 = currentline[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
				
				line = reader.readLine();
			}
			
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		verticesArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		
		for(Vector3f vertex:vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size();i++) {
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices,
			List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
			float[] normalsArray) {
		
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray[currentVertexPointer*2] = currentTex.x;
		textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
		
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2 ])-1);
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3+1] = currentNorm.y;
		normalsArray[currentVertexPointer*3+2] = currentNorm.z;
	}
}
