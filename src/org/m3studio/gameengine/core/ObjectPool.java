package org.m3studio.gameengine.core;

import java.util.LinkedList;

public class ObjectPool {
	private LinkedList<Object> availableObjects;
	private LinkedList<Object> inUseObjects;
	private Class<? extends Object> classObject;
	
	public ObjectPool(Class<? extends Object> classObject) {
		this.availableObjects = new LinkedList<Object>();
		this.inUseObjects = new LinkedList<Object>();
		this.classObject = classObject;
	}
	
	public synchronized Object obtainObject() {
		Object instance;
		
		if (availableObjects.size() == 0) {
			try {
				instance = classObject.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			} 
		} else {
			instance = availableObjects.getFirst();
			availableObjects.removeFirst();
		}
		
		inUseObjects.add(instance);
		return instance;
	}
	
	public synchronized void releaseObject(Object obj) {
		inUseObjects.remove(obj);
		availableObjects.add(obj);
	}
	
	public void allowGC() {
		availableObjects.clear();
		inUseObjects.clear();
	}
	
	public int getTotalAllocations() {
		return availableObjects.size() + inUseObjects.size();
	}
}
