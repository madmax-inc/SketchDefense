package org.m3studio.gameengine.utils;

import org.m3studio.gameengine.core.ResourceFactory;
import org.m3studio.gameengine.core.Vector;
import org.m3studio.gameengine.core.VisibleGameObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class FPSMeter extends VisibleGameObject {
	private Bitmap bitmap;
	private Canvas canvas;
	private Paint paint;
	private float lastFPS;
	private long lastCheck;
	private static final int size = 400;

	public FPSMeter(Vector position, float z) {
		super(position, z);
		
		this.bitmap = Bitmap.createBitmap(size, size * 2, Config.ARGB_8888);
		this.canvas = new Canvas(bitmap);
		
		this.paint = new Paint();
		paint.setARGB(255, 0, 255, 0);
		paint.setTextSize(size/8);
		
		this.lastFPS = 0.0f;
		this.lastCheck = System.currentTimeMillis();
		
		canvas.drawColor(Color.BLACK);
		canvas.drawText("Pool: " + String.valueOf(ResourceFactory.getInstance().getAllocatedObjects()), 0, size * 0.2f, paint);
		canvas.drawText("FPS: " + String.valueOf(lastFPS), 0, size*0.8f, paint);
	}

	public FPSMeter() {
		this(new Vector(0, 0), 0.0f);
	}

	@Override
	public Bitmap getBitmap() {
		if ((System.currentTimeMillis() - lastCheck) > 1000) {
			lastFPS = getEngine().getFPS();
			lastCheck = System.currentTimeMillis();
			
			canvas.drawColor(Color.BLACK);
			canvas.drawText("Pool: " + String.valueOf(ResourceFactory.getInstance().getAllocatedObjects()), 0, size * 0.2f, paint);
			canvas.drawText("FPS: " + String.valueOf(lastFPS), 0, size*0.8f, paint);
		}
		
		return bitmap;
	}

	@Override
	public boolean isPointInside(Vector position) {
		return false;
	}

	@Override
	public RectF getBoundingRect() {
		return new RectF(0, 0, size * 2, size);
	}

	@Override
	public void update(long step) {

	}

}
