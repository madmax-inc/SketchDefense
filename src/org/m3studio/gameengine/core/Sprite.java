package org.m3studio.gameengine.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	private Bitmap[] frames;
	private Vector coords;
	
	Sprite(Bitmap framesStrip, int rows, int columns, int framesCount, Vector coords) {
		int frameWidth = framesStrip.getWidth() / columns;
		int frameHeight = framesStrip.getHeight() / rows;
		
		this.frames = new Bitmap[framesCount];
		this.coords = coords;
		
		outer: for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++) {
				int frameNum = (i * columns) + j;
				
				if (frameNum >= framesCount)
					break outer;
				
				frames[frameNum] = Bitmap.createBitmap(frameWidth, frameHeight, Config.ARGB_8888);
				Canvas canvas = new Canvas(frames[frameNum]);
				canvas.drawBitmap(framesStrip, new Rect(j * frameWidth, i * frameHeight, (j + 1) * frameWidth, (i + 1) * frameHeight), new Rect(0, 0, frameWidth, frameHeight), null);
			}
		
	}
	
	public Bitmap getBitmap(int frameNum) {
		return frames[frameNum];
	}
	
	public Vector getCoords() {
		return coords;
	}
	
	public int getTotalFrames() {
		return frames.length;
	}
	
	public Rect getBoundingRect() {
		return new Rect((int) (-coords.x), (int) (-coords.y), (int) (frames[0].getWidth() - coords.x), (int) (frames[0].getHeight() - coords.y));
	}
}
