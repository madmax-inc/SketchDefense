package org.m3studio.gameengine.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import java.util.HashMap;

public class ResourceFactory {
	//Resources
	private Resources resources;
	
	//Object pools
	private HashMap<Class<? extends Object>, ObjectPool> objectPool;
	
	private static ResourceFactory instance;
	
	private ResourceFactory() {
		objectPool = new HashMap<Class<? extends Object>, ObjectPool>();
	}
	
	public static ResourceFactory getInstance() {
		if (instance == null)
			instance = new ResourceFactory();
		
		return instance;
	}
	
	//Resources
	
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
	
	public Typeface makeTypefaceFromAsset(String path) {
		return Typeface.createFromAsset(resources.getAssets(), path);
	}
	
	//Object pools
	public Object obtainObject(Class<? extends Object> classObject) {
		if (!objectPool.containsKey(classObject)) {
			objectPool.put(classObject, new ObjectPool(classObject));
		}
		
		return objectPool.get(classObject).obtainObject();
	}
	
	public void releaseObject(Object obj) {
		objectPool.get(obj.getClass()).releaseObject(obj);
	}
}
