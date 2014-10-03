package org.m3studio.gameengine.core;

import android.graphics.Bitmap;
import android.graphics.Rect;

public abstract class VisibleGameObject extends GameObject {
	//Event Dispatch
	private boolean isTouched;
	
	public VisibleGameObject(Vector position, float z) {
		super(position, z);

		this.isTouched = false;
	}
	
	public VisibleGameObject(Sprite sprite) {
		this(new Vector(0, 0), 0.0f);
	}
	
	public abstract Bitmap getBitmap(); 
	public abstract Rect getBoundingRect();
	
	public abstract boolean isPointInside(Vector position);
	
	public void touch() {
		isTouched = true;
	}
	
	@Override
	public void dispatchEvents() {
		super.dispatchEvents();
		
		if (isTouched) {
			isTouched = false;
			
			onTouch();
		}
	}
	
	//Events
	protected void onTouch() {

	}
}
