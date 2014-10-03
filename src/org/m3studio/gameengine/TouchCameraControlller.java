package org.m3studio.gameengine;

import android.view.MotionEvent;

public class TouchCameraControlller extends TouchHandler {
	private GameCameraObject camera;
	private TouchCameraControllerState state;
	
	private Vector dragStartPosScreen;
	private Vector dragStartPosCamera;
	private float resizeStartDistance;

	public TouchCameraControlller(GameCameraObject camera) {
		this.camera = camera;
		this.state = TouchCameraControllerState.STATE_IDLE;
	}
	
	public boolean handleTouchEvent(MotionEvent event, Vector mappedPoints[]) {
		switch (state) {
			case STATE_IDLE:
				if (event.getPointerCount() == 1) {
					state = TouchCameraControllerState.STATE_DRAG;
					dragStartPosScreen = new Vector(event.getX(), event.getY());
					dragStartPosCamera = camera.getPosition();
				} else if (event.getPointerCount() == 2) {
					state = TouchCameraControllerState.STATE_RESIZE;
					float dx = event.getX(1) - event.getX(0);
					float dy = event.getY(1) - event.getY(0);
					
					resizeStartDistance = (float) Math.sqrt(dx * dx + dy * dy);
				}
				break;
			case STATE_DRAG:
				if (event.getPointerCount() == 1) {
					if (event.getAction() == MotionEvent.ACTION_MOVE) {
						Vector newPos = new Vector(dragStartPosCamera);

						Vector currentPosScreen = new Vector(event.getX(), event.getY());
						Vector delta = new Vector(currentPosScreen, dragStartPosScreen);
						delta.multiply(1/camera.getScale());

						newPos.add(delta);
						camera.setPosition(newPos);
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						state = TouchCameraControllerState.STATE_IDLE;
					}
				} else if (event.getPointerCount() == 2) {
					state = TouchCameraControllerState.STATE_RESIZE;
					float dx = event.getX(1) - event.getX(0);
					float dy = event.getY(1) - event.getY(0);
					
					resizeStartDistance = (float) Math.sqrt(dx * dx + dy * dy);
				}
				break;
			case STATE_RESIZE:
				if (event.getPointerCount() == 1) {
					state = TouchCameraControllerState.STATE_DRAG;
					dragStartPosScreen = new Vector(event.getX(), event.getY());
					dragStartPosCamera = camera.getPosition();
				} else if (event.getPointerCount() == 2) {
					float dx = event.getX(1) - event.getX(0);
					float dy = event.getY(1) - event.getY(0);
					
					float resizeCurrentDistance = (float) Math.sqrt(dx * dx + dy * dy);
					
					camera.setScale(resizeCurrentDistance / resizeStartDistance);
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
