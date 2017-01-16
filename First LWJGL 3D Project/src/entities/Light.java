package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	private Vector3f position;
	private Vector3f colour;
	
	public Light(Vector3f position, Vector3f colour) {
		super();
		this.position = position;
		this.colour = colour;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getColour() {
		return colour;
	}
	
	
}
