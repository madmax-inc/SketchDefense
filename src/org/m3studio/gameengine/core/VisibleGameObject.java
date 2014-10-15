package org.m3studio.gameengine.core;

import android.graphics.Bitmap;
import android.graphics.Rect;

public abstract class VisibleGameObject extends GameObject {
	public VisibleGameObject(Vector position, float z) {
		super(position, z);
	}
	
	public VisibleGameObject() {
		this(new Vector(0, 0), 0.0f);
	}
	
	public abstract Bitmap getBitmap(); 
	public abstract Rect getBoundingRect();
	public abstract boolean isPointInside(Vector position);
	
	public void touch() {
		dispatcher.setChanged("Touch");
	}
}
