package org.m3studio.gameengine.core;

import android.graphics.Matrix;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.m3studio.gameengine.core.EventDispatcher.EventListener;

public abstract class GameObject {
	private Engine engine;
	
	private Vector position;
	private float scale;
	private float angle;
	private float z;
	
	private ReentrantReadWriteLock lock;
	
	private LinkedList<GameObjectComponent> components;
	
	protected EventDispatcher dispatcher;
	
	public GameObject(Vector position, float z) {
		this.position = position;
		this.scale = 1.0f;
		this.angle = 0.0f;
		this.z = z;
		
		this.lock = new ReentrantReadWriteLock();
		
		this.components = new LinkedList<GameObjectComponent>();
		
		this.dispatcher = new EventDispatcher(this);
	}
	
	public GameObject(Vector position) {
		this(position, 0.0f);
	}
	
	public GameObject() {
		this(new Vector(0, 0));
	}
	
	public final void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	protected final Engine getEngine() {
		return engine;
	}
	
	public final Vector getPosition() {
		Vector result = (Vector) ResourceFactory.getInstance().obtainObject(Vector.class);
		
		lock.readLock().lock();
		result.set(position);
		lock.readLock().unlock();
		
		return result;
	}
	
	public final float getScale() {
		return scale;
	}
	
	public final float getAngle() {
		return angle;
	}
	
	public final float getZ() {
		return z;
	}
	
	public Matrix getMatrix() {
		Matrix transformationMatrix = (Matrix) ResourceFactory.getInstance().obtainObject(Matrix.class);
		
		transformationMatrix.reset();
		
		Vector positionVal = getPosition();

		transformationMatrix.postScale(scale, scale);
		transformationMatrix.postRotate((float) Math.toDegrees(angle));
		transformationMatrix.postTranslate(positionVal.x, positionVal.y);
		
		ResourceFactory.getInstance().releaseObject(positionVal);
		
		return transformationMatrix;
	}
	
	public final void setPosition(Vector position) {
		dispatcher.invokeEvent("Position");
		
		this.position.set(position);
	}
	
	public final void setScale(float scale) {
		dispatcher.invokeEvent("Scale");
		
		this.scale = scale;
	}
	
	public final void setAngle(float angle) {
		dispatcher.invokeEvent("Angle");
		
		this.angle = angle;
	}
	
	public final void setZ(float z) {
		dispatcher.invokeEvent("Z");
		
		this.z = z;
	}
	
	public final void addComponent(GameObjectComponent component) {
		component.setGameObject(this);
		components.add(component);
	}
	
	public final GameObjectComponent getComponentByClass(Class<? extends GameObjectComponent> classObject) {
		int listSize = components.size();
		
		for (int i = 0; i < listSize; i++) {
			GameObjectComponent comp = classObject.cast(components.get(i));
			
			if (comp != null)
				return comp;
		}
		
		return null;
	}
	
	public final void addEventListener(String eventName, EventListener listener) {
		dispatcher.addListener(eventName, listener);
	}
	
	public final void updateComponents(long step) {
		int componentsCount = components.size();
		
		for (int i = 0; i < componentsCount; i++) {
			components.get(i).update(step);
		}
	}
	
	public abstract void update(long step);
}