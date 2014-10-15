package org.m3studio.gameengine.core;

import java.util.EventObject;
import java.util.HashSet;
import java.util.LinkedList;

public class EventDispatcher {
	private HashSet<String> changesList;
	private LinkedList<EventListener> listeners;
	private GameObject source;
	
	public EventDispatcher(GameObject source) {
		this.changesList = new HashSet<String>();
		this.listeners = new LinkedList<EventListener>();
		this.source = source;
	}
	
	public void addListener(EventListener listener) {
		listeners.add(listener);
	}
	
	public void setChanged(String what) {
		changesList.add(what);
	}
	
	public void invokeEvent() {
		if (changesList.size() == 0)
			return;
		
		GameObjectEvent event = new GameObjectEvent(source, changesList);
		int listenersCount = listeners.size();
		
		for (int i = listenersCount - 1; i >= 0; i--) {
			listeners.get(i).handleEvent(event);
		}
		
		changesList.clear();
		
		return;
	}

	public class GameObjectEvent extends EventObject {
		private static final long serialVersionUID = 1L;
		private HashSet<String> changesList;

		public GameObjectEvent(Object source, HashSet<String> changesList) {
			super(source);
			
			this.changesList = changesList;
		}
		
		public boolean isChanged(String what) {
			return changesList.contains(what);
		}
		
	}
	
	public interface EventListener {
		public void handleEvent(GameObjectEvent event);
	}
}
