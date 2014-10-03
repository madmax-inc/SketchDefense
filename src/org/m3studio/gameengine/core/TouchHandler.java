package org.m3studio.gameengine.core;

import android.view.MotionEvent;

public abstract class TouchHandler {
	public abstract boolean handleTouchEvent(MotionEvent event, Vector mappedPoints[]);
}
