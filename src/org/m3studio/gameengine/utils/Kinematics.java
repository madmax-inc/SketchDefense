package org.m3studio.gameengine.utils;

import org.m3studio.gameengine.core.GameObject;
import org.m3studio.gameengine.core.GameObjectComponent;
import org.m3studio.gameengine.core.ResourceFactory;
import org.m3studio.gameengine.core.Vector;

public class Kinematics extends GameObjectComponent {
	private Vector velocity;

	public Kinematics(Vector v) {
		super();
		
		this.velocity = v;
	}

	@Override
	protected void update(long step) {
		GameObject obj = getGameObject();
		float dt = step / 1000.0f;
		
		Vector currentPos = obj.getPosition();
		
		Vector deltaPos = (Vector) ResourceFactory.getInstance().obtainObject(Vector.class);
		deltaPos.set(velocity);
		deltaPos.multiply(dt);
		
		currentPos.add(deltaPos);
		
		obj.setPosition(currentPos);
		
		ResourceFactory.getInstance().releaseObject(currentPos);
		ResourceFactory.getInstance().releaseObject(deltaPos);
	}

}
