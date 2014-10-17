package org.m3studio.gameengine.core;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Background extends FlatGameObject {
	private Bitmap backgroundBitmap;
	
	public Background(Bitmap backgroundBitmap, Vector position) {
		super(position);
		this.backgroundBitmap = backgroundBitmap;
	}

	public Background(Bitmap backgroundBitmap) {
		this(backgroundBitmap, new Vector(0, 0));
	}
	
	public final Bitmap getBitmap() {
		return backgroundBitmap;
	}
	
	public final void setBitmap(Bitmap backgroundBitmap) {
		this.backgroundBitmap = backgroundBitmap;
	}

	@Override
	public RectF getBoundingRect() {
		return new RectF(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
	}

	@Override
	public void update(long step) {
		
	}

}
