package org.m3studio.gameengine;

import android.graphics.Matrix;
import android.graphics.Rect;

public class VisibleGameObject extends GameObject {
	private Sprite sprite;
	private BasicFramesAnimation animation;
	private int frameNum;
	
	public VisibleGameObject(Sprite sprite, Vector position, float z) {
		super(position, z);
		this.sprite = sprite;
		this.frameNum = 0;
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
}
