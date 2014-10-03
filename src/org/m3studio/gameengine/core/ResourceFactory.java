package org.m3studio.gameengine.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResourceFactory {
	private Resources resources;
	private static ResourceFactory instance;
	
	private ResourceFactory() {
		
	}
	
	public static ResourceFactory getInstance() {
		if (instance == null)
			instance = new ResourceFactory();
		
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
	
	public Background makeBackgroundFromResource(int id, Vector coords) {
		Bitmap back = BitmapFactory.decodeResource(resources, id);
		
		Background background = new Background(back, coords);
		
		return background;
	}
}
