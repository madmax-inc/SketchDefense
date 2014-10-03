package org.m3studio.sketchdefense;

import org.m3studio.gameengine.Animation;
import org.m3studio.gameengine.GameObject;
import org.m3studio.gameengine.Interpolator;
import org.m3studio.gameengine.Vector;

public class Scale extends Animation {
	Interpolator scaleInterpolator;

	public Scale(float scaleTime, float scaleFactor, GameObject target, Class<? extends Interpolator> interpolationBuilder, boolean isLooped) {
		super(target, interpolationBuilder, isLooped);
		
		scaleInterpolator = this.makeInterpolator();
		scaleInterpolator.addPoint(new Vector(0.0f, target.getScale()));
		scaleInterpolator.addPoint(new Vector(scaleTime / 2, scaleFactor));
		scaleInterpolator.addPoint(new Vector(scaleTime, target.getScale()));
		
		this.setEndingTime(scaleTime);
	}

	@Override
	public void step() {
		GameObject target = this.getTarget();
		
		target.setScale(scaleInterpolator.interpolate(this.getCurrentTime()));
	}

}
