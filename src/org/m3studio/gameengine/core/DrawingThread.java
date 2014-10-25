package org.m3studio.gameengine.core;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class DrawingThread extends Thread {
	private Engine engine;
	private boolean limitFps;
	private int fpsLimit;
	
	private float fps;
	
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
	
	public float getFPS() {
		return fps;
	}

	@Override
	public void run() {
		SurfaceHolder holder = engine.getHolder();
		Canvas canvas;
		long redrawTime = 0L;
		long redrawInterval = 0L;
		
		long lastFreeze = 0L;
		
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
			
			canvas = null;
			try {
				canvas = holder.lockCanvas(null);
				
				long timeStart = System.currentTimeMillis();
				
				synchronized (holder) {
					engine.redraw(canvas);
				}
				
				long duration = System.currentTimeMillis() - timeStart;
				fps = 1000.0f / (float) duration;
				
				if (fps < 30.0f) {
					Log.e("FREEZE", "Low fps = " + String.valueOf(fps) + "!");
					
					if (lastFreeze != 0L) {
						Log.e("FREEZE", "Ms since last freeze: " + String.valueOf(System.currentTimeMillis() - lastFreeze));
					}
					
					lastFreeze = System.currentTimeMillis();
				}
			} catch (NullPointerException e) {
				Log.e("QQQ", "Null Pointer!");
				e.printStackTrace();
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
			
			redrawTime = System.currentTimeMillis();
		}
	}
}