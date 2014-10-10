package org.m3studio.gameengine.core;

import org.m3studio.gameengine.core.Vector;

import android.graphics.Matrix;

public class GameObject implements Comparable<GameObject> {
	private Engine engine;
	
	private Vector position;
	private float scale;
	private float angle;
	private float z;
	
	//Event Dispatching
	private boolean eventDispatchEnabled;
	
	private boolean positionChanged;
	private boolean scaleChanged;
	private boolean angleChanged;
	private boolean zChanged;
	
	private Vector dPos;
	private float dScale;
	private float dAngle;
	private float dZ;
	
	public GameObject(Vector position, float z) {
		this.position = position;
		this.scale = 1.0f;
		this.angle = 0.0f;
		this.z = z;
		
		this.eventDispatchEnabled = true;
		
		this.positionChanged = false;
		this.scaleChanged = false;
		this.angleChanged = false;
		this.zChanged = false;
		
		this.dPos = new Vector(0, 0);
		this.dScale = 0.0f;
		this.dAngle = 0.0f;
		this.dZ = 0.0f;
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
		Vector result;
		
		synchronized (position) {
			result = new Vector(position);
		}
		
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
		Matrix matrix = new Matrix();
		
		matrix.postScale(scale, scale);
		matrix.postRotate((float) Math.toDegrees(angle));
		
		synchronized (position) {
			matrix.postTranslate(position.x, position.y);
		}
		
		return matrix;
	}
	
	public final void setPosition(Vector position) {
		synchronized (this.position) {
			Vector dr = new Vector(position, this.position);
			dPos.add(dr);
		
			this.position = position;
		}
		
		positionChanged = true;
	}
	
	public final void setScale(float scale) {
		float ds = scale - this.scale;
		dScale += ds;
		
		this.scale = scale;
		
		scaleChanged = true;
	}
	
	public final void setAngle(float angle) {
		float da = angle - this.angle;
		dAngle += da;
		
		this.angle = angle;
		
		angleChanged = true;
	}
	
	public final void setZ(float z) {
		float dz = z - this.z;
		dZ += dz;
		
		this.z = z;
		
		zChanged = true;
	}

	
	public void disableEventDispatch() {
		if (!this.eventDispatchEnabled)
			return;
		
		this.eventDispatchEnabled = false;
	}
	
	public void forceEventDispatch() {
		if (this.eventDispatchEnabled)
			return;
		
		Vector dPosSnapshot;
		
		synchronized (position) {
			dPosSnapshot = new Vector(dPos);
		}
		
		onMultipleChanges(dPosSnapshot, dScale, dAngle, dZ);
		
		this.eventDispatchEnabled = true;
		
		synchronized (position) {
			dPos = new Vector(0, 0);
		}
		
		dScale = 0.0f;
		dAngle = 0.0f;
		dZ = 0.0f;
		
		positionChanged = false;
		scaleChanged = false;
		angleChanged = false;
		zChanged = false;
	}
	
	public void dispatchEvents() {
		if (!eventDispatchEnabled)
			return;
		
		if (positionChanged) {
			Vector dPosSnapshot;
			
			synchronized (position) {
				dPosSnapshot = new Vector(dPos);
			}
			
			onPositionChanged(dPosSnapshot);
			
			synchronized (position) {
				dPos = new Vector(0, 0);
			}
			
			positionChanged = false;
		}
		
		if (scaleChanged) {
			onScaleChanged(dScale);
			
			dScale = 0.0f;
			
			scaleChanged = false;
		}
		
		if (angleChanged) {
			onAngleChanged(dAngle);
			
			dAngle = 0.0f;
			
			angleChanged = false;
		}
		
		if (zChanged) {
			onZChanged(dZ);
			
			dZ = 0.0f;
			
			zChanged = false;
		}
	}
	
	public void update(long step) {

	}

	@Override
	public final int compareTo(GameObject another) {
		if (z > another.z)
			return 1;
		else
			return -1;
	}
	
	//Events
	protected void onPositionChanged(Vector dr) {
		
	}
	
	protected void onScaleChanged(float ds) {
		
	}
	
	protected void onAngleChanged(float da) {
		
	}
	
	protected void onZChanged(float dz) {
		
	}
	
	protected void onMultipleChanges(Vector dr, float ds, float da, float dz) {
		
	}
}