package org.m3studio.gameengine.utils.graph;

public class GraphEdge {
	private GraphVertex a;
	private GraphVertex b;
	
	public GraphEdge(GraphVertex vertex1, GraphVertex vertex2) {
		a = vertex1;
		b = vertex2;
		
		notifyVerticesConnect();
	}
	
	private final void notifyVerticesConnect() {
		a.addIncidentEdge(this);
		b.addIncidentEdge(this);
	}
	
	private final void notifyVerticesBreak() {
		a.removeIncidentEdge(this);
		b.removeIncidentEdge(this);
	}
	
	public final void setFirstVertex(GraphVertex a) {
		notifyVerticesBreak();
		
		this.a = a;
		
		notifyVerticesConnect();
	}
	
	public final void setSecondVertex(GraphVertex b) {
		notifyVerticesBreak();
		
		this.b = b;
		
		notifyVerticesConnect();
	}
	
	public final GraphVertex getFirstVertex() {
		return a;
	}
	
	public final GraphVertex getSecondVertex() {
		return b;
	}
	
	public final boolean isIncidentFor(GraphVertex vertex) {
		if (a == vertex || b == vertex)
			return true;
		else
			return false;
	}
	
	public float getLength() {
		return 1.0f;
	}
}
