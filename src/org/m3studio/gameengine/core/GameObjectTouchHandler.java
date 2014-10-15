package org.m3studio.gameengine.core;

import java.util.Iterator;
import java.util.TreeSet;

import android.graphics.Rect;
import android.view.MotionEvent;

public class GameObjectTouchHandler extends TouchHandler {
	private TreeSet<VisibleGameObject> renderingPipeline;
	
	public GameObjectTouchHandler(TreeSet<VisibleGameObject> renderingPipeline) {
		this.renderingPipeline = renderingPipeline;
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
				
				Vector innerPos = object.getPosition();
				Vector insidePos = new Vector(mappedPoints[0], innerPos);
				ResourceFactory.getInstance().releaseObject(innerPos);
				

				if (boundingRect.contains((int) mappedPoints[0].x, (int) mappedPoints[0].y)) {
					if (object.isPointInside(insidePos)) {
						flag = true;

						object.touch();
						
						break;
					}
				}
			}
		}
		
		return flag;
	}

}
