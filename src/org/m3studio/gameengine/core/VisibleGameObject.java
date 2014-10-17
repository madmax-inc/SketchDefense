package org.m3studio.gameengine.core;

import android.graphics.Bitmap;

public abstract class VisibleGameObject extends FlatGameObject {
	public VisibleGameObject(Vector position, float z) {
		super(position, z);
	}
	
	public VisibleGameObject() {
		this(new Vector(0, 0), 0.0f);
	}
	
	public abstract Bitmap getBitmap(); 
	public abstract boolean isPointInside(Vector position);
	
	public void touch() {
		dispatcher.setChanged("Touch");
	}
}
