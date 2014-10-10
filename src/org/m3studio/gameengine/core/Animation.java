package org.m3studio.gameengine.core;

public abstract class Animation {
	private Engine engine;
	private GameObject target;
	private float currentTime;
	private float endingTime;
	private boolean isLooped;
	private Class<? extends Interpolator> interpolationBuilder;
	
	public Animation(GameObject target, Class<? extends Interpolator> interpolationBuilder, boolean isLooped) {
		this.target = target;
		this.interpolationBuilder = interpolationBuilder;
		this.isLooped = isLooped;
		this.currentTime = 0.0f;
		this.endingTime = 0.0f;
	}
	
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	public GameObject getTarget() {
		return target;
	}
	
	public float getCurrentTime() {
		return currentTime;
	}
	
	public void setEndingTime(float endingTime) {
		this.endingTime = endingTime;
	}
	
	protected Interpolator makeInterpolator() {
		try {
			return interpolationBuilder.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final void update(long step) {
		currentTime += (float) step / 1000.0f;
		
		step();
		if (currentTime > endingTime) {
			if (isLooped)
				currentTime = 0.0f;
			else
				engine.removeAnimation(this);
		}
	}
	
	public abstract void step();
}
