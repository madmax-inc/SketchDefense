package org.m3studio.sketchdefense;

import org.m3studio.gameengine.Animation;
import org.m3studio.gameengine.GameObject;
import org.m3studio.gameengine.Interpolator;
import org.m3studio.gameengine.Vector;

public class Rotation extends Animation {
	Interpolator rotationInterpolator;

	public Rotation(float rotationTime, boolean clockwise, GameObject target, Class<? extends Interpolator> interpolationBuilder, boolean isLooped) {
		super(target, interpolationBuilder, isLooped);
		
		rotationInterpolator = this.makeInterpolator();
		rotationInterpolator.addPoint(new Vector(0, 0));
		rotationInterpolator.addPoint(new Vector(rotationTime, (float) ((clockwise?-1.0f:1.0f) * 2 * Math.PI)));
		
		this.setEndingTime(rotationTime);
	}

	@Override
	public void step() {
		GameObject target = this.getTarget();
		
		target.setAngle(rotationInterpolator.interpolate(this.getCurrentTime()));
	}

}
