package org.m3studio.gameengine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SpriteFactory {
	private Resources resources;
	private static SpriteFactory instance;
	
	private SpriteFactory() {
		
	}
	
	public static SpriteFactory getInstance() {
		if (instance == null)
			instance = new SpriteFactory();
		
		return instance;
	}
	
	public void setResources(Resources res) {
		this.resources = res;
	}
	
	public Sprite makeSpriteFromResourceStrip(int id, int rows, int columns, int frames, Vector coords) {
		Bitmap strip = BitmapFactory.decodeResource(resources, id);
		
		Sprite sprite = new Sprite(strip, rows, columns, frames, coords);
		
		return sprite;
	}
}
