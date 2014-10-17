package org.m3studio.gameengine.core;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

public class GameCameraObject extends FlatGameObject {
	private DisplayMetrics displayMetrics;
	
	public GameCameraObject(Vector position, DisplayMetrics displayMetrics) {
		super(position);
		this.displayMetrics = displayMetrics;
	}
	
	public GameCameraObject(DisplayMetrics displayMetrics) {
		this(new Vector(0, 0), displayMetrics);
	}
	
	@Override
	public final RectF getBoundingRect() {
		RectF rect = new RectF(0.0f, 0.0f, displayMetrics.widthPixels, displayMetrics.heightPixels);
		
		getMatrix();
		transformationMatrix.mapRect(rect);
		
		return rect;
	}
	
	@Override
	public Matrix getMatrix() {
		Vector position = getPosition();
		float scale = getScale();
		
		transformationMatrix.reset();
		
		transformationMatrix.postTranslate(0.5f * displayMetrics.widthPixels, 0.5f * displayMetrics.heightPixels);
		transformationMatrix.postScale(1.0f/scale, 1.0f/scale);
		transformationMatrix.postRotate((float)Math.toDegrees(-getAngle()));
		transformationMatrix.postTranslate(-position.x, -position.y);
		
		ResourceFactory.getInstance().releaseObject(position);
		
		return transformationMatrix;
	}

	@Override
	public void update(long step) {
		
	}
}
