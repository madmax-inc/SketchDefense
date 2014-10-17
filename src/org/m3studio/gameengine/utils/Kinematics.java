package org.m3studio.gameengine.utils;

import org.m3studio.gameengine.core.GameObjectComponent;
import org.m3studio.gameengine.core.ResourceFactory;
import org.m3studio.gameengine.core.Vector;

public class Kinematics extends GameObjectComponent {
	private Vector velocity;

	public Kinematics(Vector velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * @return Vector, obtained through {@link ResourceFactory}'s obtainObject method
	 */
	public final Vector getVelocity() {
		Vector v = (Vector) ResourceFactory.getInstance().obtainObject(Vector.class);
		
		synchronized (velocity) {
			v.set(velocity);
		}
		
		return v;
	}
	
	public final void setVelocity(Vector velocity) {
		synchronized (velocity) {
			this.velocity.set(velocity);
		}
	}

	@Override
	protected void update(long step) {
		float dt = (float) step / 1000.0f;
		
		Vector v = getVelocity();
		v.multiply(dt);
		
		Vector pos = getGameObject().getPosition();
		pos.add(v);
		
		getGameObject().setPosition(pos);
		
		ResourceFactory.getInstance().releaseObject(pos);
		ResourceFactory.getInstance().releaseObject(v);
	}

}
