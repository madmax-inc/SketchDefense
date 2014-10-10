package org.m3studio.gameengine.utils.graph;

import java.util.ArrayList;

public class GraphPath {
	private ArrayList<GraphVertex> path;
	private float pathCost;

	public GraphPath(ArrayList<GraphVertex> path, float pathCost) {
		this.path = path;
		this.pathCost = pathCost;
	}
	
	@Override
	public String toString() {
		return "Length of the path is " + String.valueOf(pathCost);
	}
}
