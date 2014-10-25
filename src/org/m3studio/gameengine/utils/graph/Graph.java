package org.m3studio.gameengine.utils.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class Graph {
	private HashSet<GraphVertex> vertices;

	public Graph() {
		this.vertices = new HashSet<GraphVertex>();
	}
	
	public final void addVertex(GraphVertex vertex) {
		vertices.add(vertex);
	}
	
	public final void clearGraph() {
		vertices.clear();
	}
	
	public final void removeVertex(GraphVertex vertex) {
		vertices.remove(vertex);
	}
	
	public final GraphVertex getClosestVertex(final GraphVertex vertex) {
		ArrayList<GraphVertex> vertexList = new ArrayList<GraphVertex>(vertices);
		Collections.sort(vertexList, new Comparator<GraphVertex>() {
			@Override
			public int compare(GraphVertex lhs, GraphVertex rhs) {
				if (lhs.estimatedLengthTo(vertex) > rhs.estimatedLengthTo(vertex))
					return 1;
				else
					return -1;
			}
		});
		
		return vertexList.get(0);
	}

}
