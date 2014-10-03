package org.m3studio.gameengine.core;

import java.util.ArrayList;

public abstract class Interpolator {
	private ArrayList<Vector> points;
	
	protected Interpolator(ArrayList<Vector> points) {
		this.points = points;
	}
	
	protected Interpolator() {
		this(new ArrayList<Vector>());
	}
	
	public void addPoint(Vector point) {
		points.add(point);
	}
	
	public void clearPoints() {
		points.clear();
	}
	
	public ArrayList<Vector> getPoints() {
		return points;
	}
	
	public abstract float interpolate(float x);
}
