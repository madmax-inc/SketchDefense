package org.m3studio.gameengine.utils;

public class GraphEdge {
	private GraphVertex vertices[];
	
	public GraphEdge(GraphVertex vertex1, GraphVertex vertex2) {
		vertices = new GraphVertex[2];
		
		vertices[0] = vertex1;
		vertices[1] = vertex2;
	}
}
