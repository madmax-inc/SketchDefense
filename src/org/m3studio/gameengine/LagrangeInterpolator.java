package org.m3studio.gameengine;

import java.util.ArrayList;

public class LagrangeInterpolator extends Interpolator {

	public LagrangeInterpolator(ArrayList<Vector> points) {
		super(points);
	}

	public LagrangeInterpolator() {
		super();
	}

	@Override
	public float interpolate(float x) {
		ArrayList<Vector> points = this.getPoints();
		int pointsCount = points.size();
		
		float value = 0.0f;
		
		for (int i = 0; i < pointsCount; i++) {
			Vector pointI = points.get(i);
			
			float multiplication = pointI.y;
			
			for (int j = 0; j < pointsCount; j++) {
				if (i == j)
					continue;
				
				Vector pointJ = points.get(j);
				
				multiplication *= (x - pointJ.x)/(pointI.x - pointJ.x);
			}
			
			value += multiplication;
		}
		
		return value;
	}

}
