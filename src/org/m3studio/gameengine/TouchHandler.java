package org.m3studio.gameengine;

import android.view.MotionEvent;

public abstract class TouchHandler {
	public abstract boolean handleTouchEvent(MotionEvent event, Vector mappedPoints[]);
}
