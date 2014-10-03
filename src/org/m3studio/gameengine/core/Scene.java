package org.m3studio.gameengine.core;

public abstract class Scene extends Thread {
	private Engine engine;

	public Scene(Engine engine) {
		super();
		
		this.engine = engine;
	}
	
	protected abstract void onSceneLoad();
	protected abstract void onSceneRun();
	protected abstract void onSceneEnd();
	
	@Override
	public final void run() {
		onSceneLoad();
		
		onSceneRun();
		
		onSceneEnd();
	}

}
