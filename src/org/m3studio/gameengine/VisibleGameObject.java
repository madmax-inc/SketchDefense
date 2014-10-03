package org.m3studio.gameengine;

import org.m3studio.sketchdefense.Rotation;
import org.m3studio.sketchdefense.Scale;

import android.graphics.Matrix;
import android.graphics.Rect;

public class VisibleGameObject extends GameObject {
	private Sprite sprite;
	private BasicFramesAnimation animation;
	private int frameNum;
	
	//Event Dispatch
	private boolean isTouched;
	
	public VisibleGameObject(Sprite sprite, Vector position, float z) {
		super(position, z);
		this.sprite = sprite;
		this.frameNum = 0;
		this.isTouched = false;
		
		animation = new BasicFramesAnimation(0.6f, this, LagrangeInterpolator.class, true);
	}
	
	public VisibleGameObject(Sprite sprite) {
		this(sprite, new Vector(0, 0), 0.0f);
	}
		
	public Sprite getSprite() {
		return sprite;
	}
	
	public int getFrameNum() {
		return frameNum;
	}
	
	public Rect getBoundingRect() {
		Vector position = getPosition();
		float scale = getScale();
		
		Rect r = sprite.getBoundingRect();
		
		r.left *= scale;
		r.top *= scale;
		r.right *= scale;
		r.bottom *= scale;
		
		r.left += position.x;
		r.top += position.y;
		r.right += position.x;
		r.bottom += position.y;
		
		return r;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void setFrameNum(int frameNum) {
		this.frameNum = frameNum;
	}
	
	public void addAnimationToAnimationPipeline() {
		this.getEngine().addAnimation(animation);
	}
	
	public void removeAnimationFromAnimationPipeline() {
		this.getEngine().removeAnimation(animation);
	}
	
	public boolean isPointInside(Vector position) {
		//TODO
		return true;
	}
	
	public void touch() {
		isTouched = true;
	}
	
	@Override
	public void dispatchEvents() {
		super.dispatchEvents();
		
		if (isTouched) {
			isTouched = false;
			
			onTouch();
		}
	}
	
	@Override
	protected void updateMatrix() {
		Vector spriteCoords = sprite.getCoords();
		Vector position = getPosition();
		
		Matrix tempMatrix = new Matrix();
		tempMatrix.postTranslate(-spriteCoords.x, -spriteCoords.y);
		tempMatrix.postScale(getScale(), getScale());
		tempMatrix.postRotate((float) Math.toDegrees(getAngle()));
		tempMatrix.postTranslate(position.x, position.y);
		
		setMatrix(tempMatrix);
	}
	
	//Events
	protected void onTouch() {
		Animation animation;
		
		if (Math.random() > 0.5) {
			animation = new Rotation(
					(float) ((Math.random() + 0.1) * 10.0f),
					(Math.random() > 0.5) ? true : false, this,
					LagrangeInterpolator.class, false);
		} else {
			animation = new Scale(
					(float) ((Math.random() + 0.1) * 10.0f),
					(float) ((Math.random() + 0.1) * 5.0f), this,
					LagrangeInterpolator.class, false);
		}
		
		this.getEngine().addAnimation(animation);
	}
}
