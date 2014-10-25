package org.m3studio.gameengine.core;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * @author madmax
 * <p>Collection Handler is a basic class that handles IO operations to a collections.</p>
 * <p>Caution! {@link #getCollection()} method is absolutely insecure!</p>
 * <p>Use it only to read from collection (acquire the read lock first with {@link #getReadLock()}</p>
 * 
 * @param <CollectionType> Collection Type to handle.
 * @param <T> Element type.
 */
public class CollectionHandler<T> {
	private ReentrantReadWriteLock lock;
	private Collection<T> collection;
	
	public CollectionHandler(Collection<T> collection) {
		this.lock = new ReentrantReadWriteLock();
		this.collection = collection;
	}
	
	public Lock getReadLock() {
		return lock.readLock();
	}
	
	public Collection<T> getCollection() {
		return collection;
	}
	
	public void add(T object) {
		lock.writeLock().lock();
		collection.add(object);
		lock.writeLock().unlock();
	}
	
	public void remove(T object) {
		lock.writeLock().lock();
		collection.remove(object);
		lock.writeLock().unlock();
	}
}
