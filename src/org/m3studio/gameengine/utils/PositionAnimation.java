package org.m3studio.gameengine.utils;

import org.m3studio.gameengine.core.Animation;
import org.m3studio.gameengine.core.GameObject;
import org.m3studio.gameengine.core.Interpolator;
import org.m3studio.gameengine.core.Vector;

public class PositionAnimation extends Animation {
	private Interpolator xInterpolator;
	private Interpolator yInterpolator;
	
	private Vector animationVector;

	public PositionAnimation(Vector start, Vector finish, float animationTime, GameObject target, Class<? extends Interpolator> interpolationBuilder, boolean isLooped) {
		super(target, interpolationBuilder, isLooped);
		
		this.xInterpolator = makeInterpolator();
		this.yInterpolator = makeInterpolator();
		
		this.xInterpolator.addPoint(new Vector(0.0f, start.x));
		this.yInterpolator.addPoint(new Vector(0.0f, start.y));
		
		this.xInterpolator.addPoint(new Vector(animationTime, finish.x));
		this.yInterpolator.addPoint(new Vector(animationTime, finish.y));
		
		this.setEndingTime(animationTime);
		
		this.animationVector = new Vector();
	}

	@Override
	public void step() {
		animationVector.x = xInterpolator.interpolate(getCurrentTime());
		animationVector.y = yInterpolator.interpolate(getCurrentTime());
		
		getTarget().setPosition(animationVector);
	}

}
