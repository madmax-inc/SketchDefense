package org.m3studio.gameengine.utils;

import org.m3studio.gameengine.core.Animation;
import org.m3studio.gameengine.core.Interpolator;
import org.m3studio.gameengine.core.Vector;
import org.m3studio.gameengine.core.VisibleSpriteGameObject;

public class BasicFramesAnimation extends Animation {
	private Interpolator frameInterpolator;

	public BasicFramesAnimation(float animationTime, VisibleSpriteGameObject target, Class<? extends Interpolator> interpolationBuilder, boolean isLooped) {
		super(target, interpolationBuilder, isLooped);

		frameInterpolator = this.makeInterpolator();
		frameInterpolator.addPoint(new Vector(0.0f, 0.0f));
		frameInterpolator.addPoint(new Vector(animationTime, target.getSprite().getTotalFrames() - 1));
		
		this.setEndingTime(animationTime);
	}

	@Override
	protected void step() {
		VisibleSpriteGameObject object = (VisibleSpriteGameObject) this.getTarget();
		object.setFrameNum((int) frameInterpolator.interpolate(this.getCurrentTime()));
	}

}
