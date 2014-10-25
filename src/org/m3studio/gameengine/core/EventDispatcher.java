package org.m3studio.gameengine.core;

import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class EventDispatcher {
	private HashMap<String, HashSet<EventListener>> handlers;
	private GameObject source;
	
	public EventDispatcher(GameObject source) {
		this.handlers = new HashMap<String, HashSet<EventListener>>();
		this.source = source;
	}
	
	public void addListener(String eventName, EventListener listener) {
		if (!handlers.containsKey(eventName))
			handlers.put(eventName, new HashSet<EventListener>());
		
		handlers.get(eventName).add(listener);
	}
	
	public void removeListener(String eventName, EventListener listener) {
		if (!handlers.containsKey(eventName))
			return;
		
		handlers.get(eventName).remove(listener);
	}
	
	public void invokeEvent(String eventName) {
		if (!handlers.containsKey(eventName))
			return;
		
		HashSet<EventListener> listeners = new HashSet<EventListener>(handlers.get(eventName));
		
		Thread eventThread = new Thread(new RunnableEvent(listeners, new GameObjectEvent(source, eventName)));
		eventThread.start();
	}
	
	private class RunnableEvent implements Runnable {
		private HashSet<EventListener> listeners;
		private GameObjectEvent event;
		
		public RunnableEvent(HashSet<EventListener> listeners, GameObjectEvent event) {
			this.listeners = listeners;
			this.event = event;
		}

		@Override
		public void run() {
			for (Iterator<EventListener> it = listeners.iterator(); it.hasNext();) {
				it.next().handleEvent(event);
			}
		}
		
	}

	public class GameObjectEvent extends EventObject {
		private static final long serialVersionUID = 1L;
		private String eventName;

		public GameObjectEvent(Object source, String eventName) {
			super(source);
			
			this.eventName = eventName;
		}
		
		public String getEventName() {
			return eventName;
		}
		
	}
	
	public interface EventListener {
		public void handleEvent(GameObjectEvent event);
	}
}
