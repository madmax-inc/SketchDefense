package org.m3studio.gameengine.utils;

import org.m3studio.gameengine.core.GameCameraObject;
import org.m3studio.gameengine.core.ResourceFactory;
import org.m3studio.gameengine.core.TouchHandler;
import org.m3studio.gameengine.core.Vector;

import android.view.MotionEvent;

public class TouchCameraControlller extends TouchHandler {
	private GameCameraObject camera;
	private TouchCameraControllerState state;
	
	private Vector dragStartPosScreen;
	private Vector dragStartPosCamera;
	private float resizeStartDistanceScreen;
	private float resizeStartScaleCamera;

	public TouchCameraControlller(GameCameraObject camera) {
		this.camera = camera;
		this.state = TouchCameraControllerState.STATE_IDLE;
		
		this.dragStartPosScreen = new Vector();
		this.dragStartPosCamera = new Vector();
	}
	
	public boolean handleTouchEvent(MotionEvent event, Vector mappedPoints[]) {
		switch (state) {
			case STATE_IDLE:
				if (event.getPointerCount() == 1) {
					state = TouchCameraControllerState.STATE_DRAG;
					
					dragStartPosScreen.x = event.getX();
					dragStartPosScreen.y = event.getY();
					
					Vector cameraPos = camera.getPosition();
					dragStartPosCamera.set(cameraPos);
					ResourceFactory.getInstance().releaseObject(cameraPos);
				} else if (event.getPointerCount() == 2) {
					state = TouchCameraControllerState.STATE_RESIZE;
					float dx = event.getX(1) - event.getX(0);
					float dy = event.getY(1) - event.getY(0);
					
					resizeStartDistanceScreen = (float) Math.sqrt(dx * dx + dy * dy);
					resizeStartScaleCamera = camera.getScale();
				}
				break;
			case STATE_DRAG:
				if (event.getPointerCount() == 1) {
					if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
						Vector newPos = (Vector) ResourceFactory.getInstance().obtainObject(Vector.class);
						newPos.set(dragStartPosCamera);

						Vector currentPosScreen = (Vector) ResourceFactory.getInstance().obtainObject(Vector.class); 
						currentPosScreen.x = event.getX();
						currentPosScreen.y = event.getY();
						
						currentPosScreen.subtract(dragStartPosScreen);
						currentPosScreen.multiply(-camera.getScale());

						newPos.add(currentPosScreen);
						camera.setPosition(newPos);
						
						ResourceFactory.getInstance().releaseObject(currentPosScreen);
						ResourceFactory.getInstance().releaseObject(newPos);
					} else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
						state = TouchCameraControllerState.STATE_IDLE;
					}
				} else if (event.getPointerCount() == 2) {
					state = TouchCameraControllerState.STATE_RESIZE;
					float dx = event.getX(1) - event.getX(0);
					float dy = event.getY(1) - event.getY(0);
					
					resizeStartDistanceScreen = (float) Math.sqrt(dx * dx + dy * dy);
					resizeStartScaleCamera = camera.getScale();
				}
				break;
			case STATE_RESIZE:
				if (event.getPointerCount() == 1) {
					state = TouchCameraControllerState.STATE_DRAG;
					dragStartPosScreen.x = event.getX();
					dragStartPosScreen.y = event.getY();
					
					Vector cameraPos = camera.getPosition();
					dragStartPosCamera.set(cameraPos);
					ResourceFactory.getInstance().releaseObject(cameraPos);
				} else if (event.getPointerCount() == 2) {
					if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
						float dx = event.getX(1) - event.getX(0);
						float dy = event.getY(1) - event.getY(0);

						float resizeCurrentDistance = (float) Math.sqrt(dx * dx + dy * dy);

						camera.setScale(resizeStartScaleCamera * (resizeCurrentDistance / resizeStartDistanceScreen));
					} else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
						state = TouchCameraControllerState.STATE_IDLE;
					}
				}
				break;
		}
		
		return true;
	}
	
	private enum TouchCameraControllerState {
		STATE_IDLE,
		STATE_DRAG,
		STATE_RESIZE
	}

}
