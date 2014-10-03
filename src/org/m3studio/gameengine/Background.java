package org.m3studio.gameengine;

import android.graphics.Bitmap;

public class Background extends GameObject {
	private Bitmap backgroundBitmap;
	
	Background(Bitmap backgroundBitmap, Vector position) {
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

}
