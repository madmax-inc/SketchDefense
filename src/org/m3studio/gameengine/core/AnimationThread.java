package org.m3studio.gameengine.core;

import java.util.ArrayList;

/**
 * @author madmax
 * <p>Class that handles all the operations with the Animations Pipeline and updates all the animations.</p>
 */
public class AnimationThread extends Thread {
	private ArrayList<Animation> animationsList;
	private CollectionBuffer<Animation> animationsListBuffer;
	private boolean isRunning;
	private boolean isPaused;

	/**
	 * <p>Creates new thread</p>
	 * @param animationsList Animation Pipeline to handle
	 * @param animationsListBuffer {@link CollectionBuffer} which interacts with {@link Engine} to make interaction with the Animation Pipeline more predictable.
	 */
	AnimationThread(ArrayList<Animation> animationsList, CollectionBuffer<Animation> animationsListBuffer) {
		super();
		this.animationsList = animationsList;
		this.animationsListBuffer = animationsListBuffer;
		this.isRunning = false;
		this.isPaused = false;
	}
	
	/**
	 * <p>Starts the thread</p>
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		this.isRunning = true;
		super.start();
	}
	
	/**
	 * <p>Stops the thread</p>
	 */
	public synchronized void Stop() {
		this.isRunning = false;
	}
	
	/**
	 * <p>Pauses the thread</p>
	 */
	public synchronized void pause() {
		this.isPaused = true;
	}
	
	/**
	 * <p>Resumes the thread</p>
	 */
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
