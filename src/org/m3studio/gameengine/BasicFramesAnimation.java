package org.m3studio.gameengine;

public class BasicFramesAnimation extends Animation {
	private Interpolator frameInterpolator;

	public BasicFramesAnimation(float animationTime, VisibleGameObject target, Class<? extends Interpolator> interpolationBuilder, boolean isLooped) {
		super(target, interpolationBuilder, isLooped);

		frameInterpolator = this.makeInterpolator();
		frameInterpolator.addPoint(new Vector(0.0f, 0.0f));
		frameInterpolator.addPoint(new Vector(animationTime, target.getSprite().getTotalFrames()));
		
		this.setEndingTime(animationTime);
	}

	@Override
	public void step() {
		VisibleGameObject object = (VisibleGameObject) this.getTarget();
		object.setFrameNum((int) frameInterpolator.interpolate(this.getCurrentTime()));
	}

}
