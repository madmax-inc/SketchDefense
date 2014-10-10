package org.m3studio.gameengine.core;

import java.util.ArrayList;

public class AnimationThread extends Thread {
	private ArrayList<Animation> animationsList;
	private CollectionBuffer<Animation> animationsListBuffer;
	private boolean isRunning;
	private boolean isPaused;

	AnimationThread(ArrayList<Animation> animationsList, CollectionBuffer<Animation> animationsListBuffer) {
		super();
		this.animationsList = animationsList;
		this.animationsListBuffer = animationsListBuffer;
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

			int arraySize = animationsList.size();

			for (int i = 0; i < arraySize; i++) {
				Animation o = animationsList.get(i);

				o.update(stepValue);
			}
			
			animationsListBuffer.doUpdate(animationsList);
		}
	}
}
