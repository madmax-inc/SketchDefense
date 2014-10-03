package org.m3studio.gameengine.core;

import java.util.ArrayList;

public class GameObjectThread extends Thread {
	private ArrayList<GameObject> gameObjectsList;
	private CollectionBuffer<GameObject> gameObjectsListBuffer;
	private Object globalObjectsMutex;
	private boolean isRunning;
	private boolean isPaused;

	GameObjectThread(ArrayList<GameObject> gameObjectsList, CollectionBuffer<GameObject> gameObjectsListBuffer, Object globalObjectsMutex) {
		super();
		this.gameObjectsList = gameObjectsList;
		this.gameObjectsListBuffer = gameObjectsListBuffer;
		this.globalObjectsMutex = globalObjectsMutex;
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
		
		while (isRunning) {
			while (isPaused)
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			
			long stepValue = (System.currentTimeMillis() - lastUpdate);
			lastUpdate = System.currentTimeMillis();
			
			synchronized (gameObjectsList) {
				synchronized (globalObjectsMutex) {
					int arraySize = gameObjectsList.size();
					
					for (int i = 0; i < arraySize; i++) {
						GameObject o = gameObjectsList.get(i);
						
						o.dispatchEvents();
						o.update(stepValue);
					}
				}
			}
			
			gameObjectsListBuffer.doUpdate(gameObjectsList);
		}
	}
}
