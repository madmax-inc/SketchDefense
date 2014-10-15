package org.m3studio.gameengine.core;

import java.util.Observable;

public class GameObjectObservable extends Observable {
	private boolean hasPositionChanged;
	private boolean hasScaleChanged;
	private boolean hasAngleChanged;
	private boolean hasZChanged;

	public GameObjectObservable() {
		super();
		
		hasPositionChanged = false;
		hasScaleChanged = false;
		hasAngleChanged = false;
		hasZChanged = false;
	}
	
	protected void setPositionChanged() {
		hasPositionChanged = true;
	}
	
	protected void clearPositionChanged() {
		hasPositionChanged = false;
	}
	
	public boolean hasPositionChanged() {
		return hasPositionChanged;
	}
	
	protected void setScaleChanged() {
		hasScaleChanged = true;
	}
	
	protected void clearScaleChanged() {
		hasScaleChanged = false;
	}
	
	public boolean hasScaleChanged() {
		return hasScaleChanged;
	}
	
	protected void setAngleChanged() {
		hasAngleChanged = true;
	}
	
	protected void clearAngleChanged() {
		hasAngleChanged = false;
	}
	
	public boolean hasAngleChanged() {
		return hasAngleChanged;
	}
	
	protected void setZChanged() {
		hasZChanged = true;
	}
	
	protected void clearZChanged() {
		hasZChanged = false;
	}
	
	public boolean hasZChanged() {
		return hasZChanged;
	}

}
