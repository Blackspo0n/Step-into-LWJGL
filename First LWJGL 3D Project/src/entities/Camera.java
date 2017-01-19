package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,5,0);
	private float pitch = 10;
	private float yaw;
	private float roll;
	
	public Camera() {
	}

	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= 0.2f;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += 1.5f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += 0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= 0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.y += 0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			position.y -= 0.2f;
		}
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            position.y+=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            position.y-=0.2f;
        }

		if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
			roll += 0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
			roll -= 0.5f;
		}
		
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	
}
