package org.m3studio.gameengine.core;

import java.util.ArrayList;

/**
 * @author madmax
 * <p>Class that handles all the operations with the Animations Pipeline and updates all the animations.</p>
 */
public class AnimationThread extends Thread {
	private CollectionHandler<Animation> animationsHandler;
	private boolean isRunning;
	private boolean isPaused;

	/**
	 * <p>Creates new thread</p>
	 * @param animationsHandler {@link CollectionHandler} which handles the underlying collection access
	 */
	AnimationThread(CollectionHandler<Animation> animationsHandler) {
		super();
		this.animationsHandler = animationsHandler;
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
		ArrayList<Animation> animations = (ArrayList<Animation>) animationsHandler.getCollection();
		
		while (isRunning) {
			while (isPaused)
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			
			long stepValue = (System.currentTimeMillis() - lastUpdate);
			lastUpdate = System.currentTimeMillis();

			animationsHandler.getReadLock().lock();
			
			int arraySize = animations.size();

			for (int i = 0; i < arraySize; i++) {
				Animation o = animations.get(i);

				o.update(stepValue);
			}
			
			animationsHandler.getReadLock().unlock();
		}
	}
}
