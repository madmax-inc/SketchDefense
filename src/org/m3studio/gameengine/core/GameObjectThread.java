package org.m3studio.gameengine.core;

import java.util.ArrayList;

public class GameObjectThread extends Thread {
	private CollectionHandler<GameObject> gameObjectsHandler;
	private boolean isRunning;
	private boolean isPaused;

	GameObjectThread(CollectionHandler<GameObject> gameObjectsHandler) {
		super();
		this.gameObjectsHandler = gameObjectsHandler;
		this.isRunning = false;
		this.isPaused = false;
	}
	
	@Override
	public synchronized void start() {
		this.isRunning = true;
		super.start();
	}
	
	public synchronized void Stop() {
		this.isRunning = false;
	}
	
	public synchronized void pause() {
		this.isPaused = true;
	}
	
	public synchronized void Resume() {
		this.isPaused = false;
	}

	@Override
	public void run() {
		long lastUpdate = System.currentTimeMillis();
		ArrayList<GameObject> objects = (ArrayList<GameObject>) gameObjectsHandler.getCollection();
		
		while (isRunning) {
			while (isPaused)
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			
			long stepValue = (System.currentTimeMillis() - lastUpdate);
			lastUpdate = System.currentTimeMillis();

			gameObjectsHandler.getReadLock().lock();
			
			int arraySize = objects.size();

			for (int i = 0; i < arraySize; i++) {
				GameObject o = objects.get(i);

				o.updateComponents(stepValue);
				o.update(stepValue);
			}
			
			gameObjectsHandler.getReadLock().unlock();
		}
	}
}
