package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
		
		acceptInputGrab();
		
		updateRotation(5);
	}
	
    public static void acceptInputGrab() {
        if(Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Mouse.setGrabbed(false);
        }
    }
	
	public void updateRotation(int delta) {
        if(Mouse.isGrabbed()) {
            float mouseDX = Mouse.getDX();
            float mouseDY = -Mouse.getDY();
            yaw += mouseDX * 0.05f * delta;
            pitch += mouseDY * 0.0f * delta;
            //pitch = Math.max(-85, Math.min(85, yaw));
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
