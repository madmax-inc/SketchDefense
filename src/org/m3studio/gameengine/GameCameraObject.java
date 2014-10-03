package org.m3studio.gameengine;

import android.graphics.Matrix;

public class GameCameraObject extends GameObject {
	GameCameraObject(Vector position) {
		super(position);
	}
	
	GameCameraObject() {
		this(new Vector(0, 0));
	}
	
	@Override
	protected void updateMatrix() {
		Vector position = getPosition();
		float scale = getScale();
		
		Matrix projection = new Matrix();
		
		projection.postScale(1.0f/scale, 1.0f/scale);
		projection.postRotate((float)Math.toDegrees(-getAngle()));
		projection.postTranslate(-position.x, -position.y);
		
		this.setMatrix(projection);
	}
}
