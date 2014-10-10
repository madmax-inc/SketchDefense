package org.m3studio.gameengine.core;

public abstract class GameObjectComponent {
	private GameObject gameObject;
	
	public GameObjectComponent() {
		
	}
	
	public final void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	protected final GameObject getGameObject() {
		return gameObject;
	}
	
	protected abstract void update(long step);
}
