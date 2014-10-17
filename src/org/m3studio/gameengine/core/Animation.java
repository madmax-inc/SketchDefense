package org.m3studio.gameengine.core;

/**
 * @author madmax
 * <p>An abstract class, representing animation.</p>
 * <p>In order to build your own animation, extend it and implement the {@link #step()} method.</p>
 * <p>Invoke {@link #setEndingTime()} after instantinating.
 */
public abstract class Animation {
	private Engine engine;
	private GameObject target;
	private float currentTime;
	private float endingTime;
	private boolean isLooped;
	private Class<? extends Interpolator> interpolationBuilder;
	
	/**
	 * <p>Creates new {@link Animation} object.</p>
	 * @param target {@link GameObject}, which is being influenced by the {@link Animation}
	 * @param interpolationBuilder {@link Class} object, represents a proper {@link Interpolator} class to use
	 * @param isLooped Marks animation as loopable or not.
	 */
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
	
	/**
	 * @return {@link GameObject} which is associated with animation.
	 */
	public GameObject getTarget() {
		return target;
	}
	
	/**
	 * @return Current animtion time in ms.
	 */
	public final float getCurrentTime() {
		return currentTime;
	}
	
	/**
	 * <p>Method, that set the animation time.</p>
	 * <p><b>Important notice! Invoke this after the creation!</b></p>
	 * 
	 * @param endingTime Animation time in ms.
	 */
	public final void setEndingTime(float endingTime) {
		this.endingTime = endingTime;
	}
	
	protected final Interpolator makeInterpolator() {
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
	
	/**
	 * <p>Method, invoked by {@link AnimationThread} to update the current animation time and invoke the {@link #step()} method</p>
	 * @param step Time in ms after last update.
	 */
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
	
	/**
	 * <p>Method invoked every quant of time to update some of animation target properties.</p>
	 */
	public abstract void step();
}
