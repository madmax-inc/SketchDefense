package org.m3studio.sketchdefense;

import org.m3studio.gameengine.utils.graph.GraphEdge;
import org.m3studio.gameengine.utils.graph.GraphVertex;

public class ConcreteGraphEdge extends GraphEdge {
	private float length;
	
	public ConcreteGraphEdge(GraphVertex vertex1, GraphVertex vertex2, float length) {
		super(vertex1, vertex2);
		this.length = length;
	}
	
	@Override
	public float getLength() {
		return length;
	}	

}
