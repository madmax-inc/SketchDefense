package org.m3studio.gameengine.core;

import android.graphics.RectF;

public abstract class FlatGameObject extends GameObject {

	public FlatGameObject(Vector position, float z) {
		super(position, z);
	}

	public FlatGameObject(Vector position) {
		super(position);
	}

	public FlatGameObject() {
		super();
	}
	
	public abstract RectF getBoundingRect();
}
