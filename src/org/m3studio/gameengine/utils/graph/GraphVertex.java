package org.m3studio.gameengine.utils.graph;

import java.util.ArrayList;
import java.util.Iterator;

public class GraphVertex {
	private ArrayList<GraphEdge> incidentEdges;
	
	public GraphVertex() {
		incidentEdges = new ArrayList<GraphEdge>();
	}
	
	public final void addIncidentEdge(GraphEdge edge) {
		incidentEdges.add(edge);
	}
	
	public final void removeIncidentEdge(GraphEdge edge) {
		incidentEdges.remove(edge);
	}
	
	public final int getIncidentEdgesCount() {
		return incidentEdges.size();
	}
	
	public final GraphEdge getIncidentEdge(int index) {
		return incidentEdges.get(index);
	}
	
	public final boolean isAdjacentFor(GraphVertex vertex) {
		for (Iterator<GraphEdge> it = incidentEdges.iterator(); it.hasNext();) {
			GraphEdge edge = it.next();
			
			if (edge.isIncidentFor(vertex))
				return true;
		}
		
		return false;
	}
	
	public final GraphVertex getAdjacentVertex(GraphEdge edge) {
		if (!incidentEdges.contains(edge))
			return null;
		
		if (edge.getFirstVertex() == this)
			return edge.getSecondVertex();
		else
			return edge.getFirstVertex();
	}
	
	public float estimatedLengthTo(GraphVertex vertex) {
		return 0.0f;
	}
}
