package org.m3studio.gameengine;

public class Vector {
	public float x;
	public float y;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(Vector b) {
		this.x = b.x;
		this.y = b.y;
	}
	
	Vector(Vector a, Vector b) {
		this.x = b.x - a.x;
		this.y = b.y - a.y;
	}
	
	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}
	
	public void normalize() {
		double d = getLength();
		
		x /= d;
		y /= d;
	}
	
	public void multiply(float b) {
		x *= b;
		y *= b;
	}
	
	public void add(Vector b) {
		this.x += b.x;
		this.y += b.y;
	}
	
	public float dotProduct(Vector b) {
		return (this.x * b.x + this.y * b.y);
	}
	
	public double angleBetween(Vector b) {
		Vector cA = new Vector(this);
		Vector cB = new Vector(b);
		
		cA.normalize();
		cB.normalize();
		
		return Math.acos(cA.dotProduct(cB));
	}
}
