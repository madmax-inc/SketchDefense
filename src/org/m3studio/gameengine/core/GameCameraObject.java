package org.m3studio.gameengine.core;

import android.graphics.Matrix;

public class GameCameraObject extends GameObject {
	GameCameraObject(Vector position) {
		super(position);
	}
	
	GameCameraObject() {
		this(new Vector(0, 0));
	}
	
	@Override
	public Matrix getMatrix() {
		Vector position = getPosition();
		float scale = getScale();
		
		transformationMatrix.reset();
		
		transformationMatrix.postScale(1.0f/scale, 1.0f/scale);
		transformationMatrix.postRotate((float)Math.toDegrees(-getAngle()));
		transformationMatrix.postTranslate(-position.x, -position.y);
		
		ResourceFactory.getInstance().releaseObject(position);
		
		return transformationMatrix;
	}
}
