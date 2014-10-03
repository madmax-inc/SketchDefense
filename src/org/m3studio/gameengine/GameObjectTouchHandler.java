package org.m3studio.gameengine;

import java.util.Iterator;
import java.util.TreeSet;

import org.m3studio.sketchdefense.Rotation;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class GameObjectTouchHandler extends TouchHandler {
	private Engine engine;
	private TreeSet<VisibleGameObject> renderingPipeline;
	private Object globalObjectsMutex;
	
	public GameObjectTouchHandler(Engine engine, TreeSet<VisibleGameObject> renderingPipeline, Object globalObjectsMutex) {
		this.engine = engine;
		this.renderingPipeline = renderingPipeline;
		this.globalObjectsMutex = globalObjectsMutex;
	}

	@Override
	public boolean handleTouchEvent(MotionEvent event, Vector mappedPoints[]) {
		if (event.getPointerCount() > 1)
			return false;
		
		if (event.getAction() != MotionEvent.ACTION_UP)
			return false;
		
		boolean flag = false;
		
		synchronized (renderingPipeline) {
			for (Iterator<VisibleGameObject> it = renderingPipeline.descendingIterator(); it.hasNext();) {
				VisibleGameObject object = it.next();
				Rect boundingRect = object.getBoundingRect();

				if (boundingRect.contains((int) mappedPoints[0].x, (int) mappedPoints[0].y)) {
					if (object.isPointInside(new Vector(mappedPoints[0], object.getPosition()))) {
						Log.d("TOUCH", "POINT INSIDE");
						flag = true;

						Rotation rot = new Rotation(
								(float) ((Math.random() + 0.1) * 10.0f),
								(Math.random() > 0.5) ? true : false, object,
								LagrangeInterpolator.class, false);
						engine.addAnimation(rot);

						// object.onTouch();
						Log.d("TOUCH", "TOUCHED!");
						break;
					}
				}
			}
		}
		
		Log.d("TOUCH", "RETURNING!");
		
		return flag;
	}

}
