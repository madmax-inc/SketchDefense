package org.m3studio.gameengine.core;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawingThread extends Thread {
	private Engine engine;
	private boolean limitFps;
	private int fpsLimit;
	
	private boolean isRunning;
	private boolean isPaused;
	
	DrawingThread(Engine engine, boolean limitFps, int fpsLimit) {
		super();
		this.engine = engine;
		this.limitFps = limitFps;
		this.fpsLimit = fpsLimit;
		
		
		this.isRunning = false;
		this.isPaused = false;
	}
	
	DrawingThread(Engine engine, boolean limitFps) {
		this(engine, limitFps, 30);
	}
	
	public void setFpsLimit(int fpsLimit) {
		this.fpsLimit = fpsLimit;
	}
	
	public void setLimitFps(boolean limitFps) {
		this.limitFps = limitFps;
	}
	
	@Override
	public synchronized void start() {
		isRunning = true;
		super.start();
	}
	
	public synchronized void Stop() {
		isRunning = false;
	}
	
	public synchronized void pause() {
		this.isPaused = true;
	}
	
	public synchronized void Resume() {
		this.isPaused = false;
	}

	@Override
	public void run() {
		SurfaceHolder holder = engine.getHolder();
		Canvas canvas;
		long redrawTime = 0L;
		long redrawInterval = 0L;
		
		if (limitFps) {
			redrawInterval = 1000L/fpsLimit;
		}
		
		while (isRunning) {
			while (isPaused)
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			
			if (limitFps) {
				if ((System.currentTimeMillis() - redrawTime) < redrawInterval)
					try {
						Thread.sleep(redrawInterval - System.currentTimeMillis() + redrawTime);
					} catch (InterruptedException e) {
					}
			}
			
			canvas = holder.lockCanvas();
			synchronized (holder) {
				canvas = engine.redraw(canvas);

				holder.unlockCanvasAndPost(canvas);
			}
			
			redrawTime = System.currentTimeMillis();
		}
	}
}