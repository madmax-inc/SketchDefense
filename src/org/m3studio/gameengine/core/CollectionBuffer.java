package org.m3studio.gameengine.core;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionBuffer<T> {
	private ArrayList<T> toAdd;
	private ArrayList<T> toRemove;
	
	public CollectionBuffer() {
		toAdd = new ArrayList<T>();
		toRemove = new ArrayList<T>();
	}
	
	public void add(T object) {
		synchronized (toAdd) {
			toAdd.add(object);
		}
	}
	
	public void remove(T object) {
		synchronized (toRemove) {
			toRemove.add(object);
		}
	}
	
	public void doAdd(Collection<T> collection) {
		synchronized (collection) {
			synchronized (toAdd) {
				if (toAdd.size() > 0) {
					collection.addAll(toAdd);
					toAdd.clear();
				}
			}
		}
	}
	
	public void doRemove(Collection<T> collection) {
		synchronized (collection) {
			synchronized (toRemove) {
				if (toRemove.size() > 0) {
					collection.removeAll(toRemove);
					toRemove.clear();
				}
			}
		}
	}
	
	public void doUpdate(Collection<T> collection) {
		doRemove(collection);
		doAdd(collection);
	}

}
