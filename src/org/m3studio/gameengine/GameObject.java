package org.m3studio.gameengine;

import org.m3studio.gameengine.Vector;
import android.graphics.Matrix;

public class GameObject implements Comparable<GameObject> {
	private Engine engine;
	private Matrix matrix;
	
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
		this.matrix = new Matrix();
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
		updateMatrix();
		this.engine = engine;
	}
	
	protected final Engine getEngine() {
		return engine;
	}
	
	public final Vector getPosition() {
		return new Vector(position);
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
	
	public final Matrix getMatrix() {
		return new Matrix(matrix);
	}
	
	public final void setPosition(Vector position) {
		Vector dr = new Vector(position, this.position);
		dPos.add(dr);
		
		this.position = position;
		
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
	
	public final void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
	
	protected void updateMatrix() {
		Matrix tempMatrix = new Matrix();
		
		tempMatrix.postScale(scale, scale);
		tempMatrix.postRotate((float) Math.toDegrees(angle));
		tempMatrix.postTranslate(position.x, position.y);
		
		setMatrix(tempMatrix);
	}
	
	public void disableEventDispatch() {
		if (!this.eventDispatchEnabled)
			return;
		
		this.eventDispatchEnabled = false;
	}
	
	public void forceEventDispatch() {
		if (this.eventDispatchEnabled)
			return;
		
		onMultipleChanges(dPos, dScale, dAngle, dZ);
		
		this.eventDispatchEnabled = true;
	}
	
	public void dispatchEvents() {
		if (!eventDispatchEnabled)
			return;
		
		if (positionChanged) {
			onPositionChanged(dPos);
			
			dPos.x = 0.0f;
			dPos.y = 0.0f;
			
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
		updateMatrix();
	}
	
	protected void onScaleChanged(float ds) {
		updateMatrix();
	}
	
	protected void onAngleChanged(float da) {
		updateMatrix();
	}
	
	protected void onZChanged(float dz) {
		
	}
	
	protected void onMultipleChanges(Vector dr, float ds, float da, float dz) {
		
	}
}