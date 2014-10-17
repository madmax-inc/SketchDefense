package org.m3studio.gameengine.utils;

import org.m3studio.gameengine.core.EventDispatcher.EventListener;
import org.m3studio.gameengine.core.EventDispatcher.GameObjectEvent;
import org.m3studio.gameengine.core.FlatGameObject;
import org.m3studio.gameengine.core.ResourceFactory;
import org.m3studio.gameengine.core.Vector;

import android.graphics.RectF;

public class BoundaryRestriction implements EventListener {
	private FlatGameObject object;
	private RectF boundary;
	
	private Vector oldPosition;
	private float oldAngle;
	private float oldScale;
	
	public BoundaryRestriction(FlatGameObject object, RectF boundary) {
		this.object = object;
		this.boundary = boundary;
		
		this.oldPosition = new Vector();
		
		updateOld();
	}
	
	private void updateOld() {
		Vector objPos = object.getPosition();
		this.oldPosition.set(objPos);
		ResourceFactory.getInstance().releaseObject(objPos);
		
		this.oldAngle = object.getAngle();
		this.oldScale = object.getScale();
	}
	
	@Override
	public void handleEvent(GameObjectEvent event) {
		if (event.getSource() != object)
			return;
		
		if (event.isChanged("Position") || event.isChanged("Scale") || event.isChanged("Angle")) {
			if (!boundary.contains(object.getBoundingRect())) {
				object.setPosition(oldPosition);
				object.setAngle(oldAngle);
				object.setScale(oldScale);
			} else {
				updateOld();
			}
		}
	}

}
