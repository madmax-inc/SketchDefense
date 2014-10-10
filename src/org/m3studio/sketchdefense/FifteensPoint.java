package org.m3studio.sketchdefense;

public class FifteensPoint {
	public int i;
	public int j;
	
	public FifteensPoint() {
		this(0, 0);
	}
	
	public FifteensPoint(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public boolean isNeighbor(FifteensPoint b) {
		if (i == b.i)
			if (b.j == (j - 1) || b.j == (j+1))
				return true;
		
		if (j == b.j)
			if (b.i == (i - 1) || b.i == (i+1))
				return true;
		
		return false;
	}
}
