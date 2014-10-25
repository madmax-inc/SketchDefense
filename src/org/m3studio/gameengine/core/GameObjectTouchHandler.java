package org.m3studio.gameengine.core;

import java.util.Iterator;
import java.util.TreeSet;

import android.graphics.RectF;
import android.view.MotionEvent;

public class GameObjectTouchHandler extends TouchHandler {
	private CollectionHandler<VisibleGameObject> renderingHandler;
	
	public GameObjectTouchHandler(CollectionHandler<VisibleGameObject> renderingHandler) {
		this.renderingHandler = renderingHandler;
	}

	@Override
	public boolean handleTouchEvent(MotionEvent event, Vector mappedPoints[]) {
		TreeSet<VisibleGameObject> objects = (TreeSet<VisibleGameObject>) renderingHandler.getCollection();
		
		if (event.getPointerCount() > 1)
			return false;
		
		if (event.getAction() != MotionEvent.ACTION_UP)
			return false;
		
		boolean flag = false;
		
		
		renderingHandler.getReadLock().lock();

		for (Iterator<VisibleGameObject> it = objects.descendingIterator(); it.hasNext();) {
			VisibleGameObject object = it.next();
			RectF boundingRect = object.getBoundingRect();

			Vector innerPos = object.getPosition();
			Vector insidePos = new Vector(mappedPoints[0], innerPos);
			ResourceFactory.getInstance().releaseObject(innerPos);

			if (boundingRect.contains((int) mappedPoints[0].x,
					(int) mappedPoints[0].y)) {
				if (object.isPointInside(insidePos)) {
					flag = true;

					object.touch();

					break;
				}
			}
		}

		renderingHandler.getReadLock().unlock();
		
		return flag;
	}

}
