package org.m3studio.gameengine.utils;

import java.util.ArrayList;

import org.m3studio.gameengine.core.EventDispatcher;
import org.m3studio.gameengine.core.EventDispatcher.EventListener;
import org.m3studio.gameengine.core.EventDispatcher.GameObjectEvent;
import org.m3studio.gameengine.core.GameObjectComponent;
import org.m3studio.gameengine.core.VisibleGameObject;

import android.graphics.RectF;

/**
 * <p>Class that enables discrete collision detection for {@link VisibleGameObject}</p>
 * <p>Add handlers to a {@link CollisionDetector} in order to be notified when a collision occurs.</p>
 * @author madmax
 */
public class CollisionDetector extends GameObjectComponent implements EventListener {
	private ArrayList<VisibleGameObject> checkCollisionWith;
	private EventDispatcher dispatcher;
	private boolean skipCheck;
	
	public CollisionDetector(VisibleGameObject object) {
		this.checkCollisionWith = new ArrayList<VisibleGameObject>();
		this.dispatcher = new EventDispatcher(object);
		this.skipCheck = false;
		
		object.addComponent(this);
	}
	
	public void addCollisionListener(EventListener listener) {
		dispatcher.addListener("Collision", listener);
	}
	
	public void removeCollisionListener(EventListener listener) {
		dispatcher.removeListener("Collision", listener);
	}
	
	public void addObjectToCheckCollisionWith(VisibleGameObject object) {
		checkCollisionWith.add(object);
	}
	
	public void removeObjectToCheckCollisionWith(VisibleGameObject object) {
		checkCollisionWith.remove(object);
	}
	
	private void collisionDetected() {
		this.skipCheck = true;
		dispatcher.invokeEvent("Collision");
	}

	@Override
	public void handleEvent(GameObjectEvent event) {
		if (event.getSource() != getGameObject())
			return;
		
		if (skipCheck)
			return;
		
		if (event.getEventName().equals("Position")) {
			RectF selfRect = ((VisibleGameObject) getGameObject()).getBoundingRect();
			
			int collisionCheckSize = checkCollisionWith.size();
			for (int i = 0; i < collisionCheckSize; i++) {
				RectF otherRect = checkCollisionWith.get(i).getBoundingRect();
				
				if (otherRect.intersect(selfRect)) {
					dispatcher.invokeEvent("Collision");
					
					//Check if other object implements CollisionDetector too
					CollisionDetector otherDetector = (CollisionDetector) getGameObject().getComponentByClass(CollisionDetector.class);
					
					if (otherDetector != null)
						otherDetector.collisionDetected();
				}
			}
		}
	}

	@Override
	protected void update(long step) {
		
	}

}
