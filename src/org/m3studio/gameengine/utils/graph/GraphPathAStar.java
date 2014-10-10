package org.m3studio.gameengine.utils.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class GraphPathAStar extends GraphPathAlgorithm {

	@Override
	public GraphPath getPathBetween(GraphVertex start, GraphVertex finish) {
		ArrayList<AStarVertex> openList = new ArrayList<AStarVertex>();
		ArrayList<AStarVertex> closedList = new ArrayList<AStarVertex>();
		
		//Add the starting point to the open list
		openList.add(new AStarVertex(start, null, 0, start.estimatedLengthTo(finish)));
		
		GraphPath result = null;
		AStarVertex finalPoint = null;
		
		outer: while (!openList.isEmpty()) {
			//Let's find a point with a minimal total length of all
			Collections.sort(openList);
			
			for (Iterator<AStarVertex> openListIterator = openList.iterator(); openListIterator.hasNext();) {
				AStarVertex aStarVertex = openListIterator.next();
				GraphVertex graphVertex = aStarVertex.getCurrentVertex();
				
				//Check if the point is the finish point
				if (graphVertex == finish) {
					finalPoint = aStarVertex;
					break outer;
				}
				
				//Add the current vertex to the closed list, and remove from the open list
				closedList.add(aStarVertex);
				openList.remove(aStarVertex);
				openListIterator = openList.iterator();
				
				//Let's take all the adjacent vertices, not presented in closed list
				int incidentEdgesCount = graphVertex.getIncidentEdgesCount();
				
				adjacent: for (int i = 0; i < incidentEdgesCount; i++) {
					GraphEdge incidentEdge = graphVertex.getIncidentEdge(i);
					GraphVertex adjacentVertex = graphVertex.getAdjacentVertex(incidentEdge);
					
					AStarVertex searchFor = new AStarVertex(adjacentVertex, aStarVertex, aStarVertex.getRealPathLength() + incidentEdge.getLength(), adjacentVertex.estimatedLengthTo(finish));
					
					if (closedList.contains(searchFor))
						continue adjacent;
						
					// Found a vertex, not presented in a closed list
					// Take a look, if it is already presented in the open list
					if (openList.contains(searchFor)) {
						AStarVertex existingVertex = openList.get(openList.lastIndexOf(searchFor));
						
						if (existingVertex.compareTo(searchFor) == -1) {
							//Reroute the path through the current vertex
							openList.remove(existingVertex);
						}
					}
					
					//Add it
					openList.add(searchFor);
					Collections.sort(openList);
					openListIterator = openList.iterator();
				}
			}
		}
		
		if (finalPoint != null)
			result = finalPoint.buildPath();
		
		return result;
	}
	
	private class AStarVertex implements Comparable<AStarVertex> {
		private GraphVertex current;
		private AStarVertex parent;
		
		private float realPathLength;
		private float estimatedPathLength;
		
		public AStarVertex(GraphVertex current, AStarVertex parent, float realPathLength, float estimatedPathLength) {
			this.current = current;
			this.parent = parent;
			this.realPathLength = realPathLength;
			this.estimatedPathLength = estimatedPathLength;
		}
		
		public final void setParentVertex(AStarVertex parent) {
			this.parent = parent;
		}
		
		public final void setRealPathLength(float realPathLength) {
			this.realPathLength = realPathLength;
		}
		
		public final void setEstimatedPathLength(float estimatedPathLength) {
			this.estimatedPathLength = estimatedPathLength;
		}
		
		public final GraphVertex getCurrentVertex() {
			return current;
		}
		
		public final AStarVertex getParentVertex() {
			return parent;
		}
		
		public final float getRealPathLength() {
			return realPathLength;
		}
		
		public final float getEstimatedPathLength() {
			return estimatedPathLength;
		}
		
		public final float getTotalPathLength() {
			return (realPathLength + estimatedPathLength);
		}
		
		public final GraphPath buildPath() {
			ArrayList<GraphVertex> vertices = new ArrayList<GraphVertex>();
			
			AStarVertex currentVertex = this;
			float pathCost = this.getRealPathLength();
			
			do {
				vertices.add(currentVertex.getCurrentVertex());
				currentVertex = currentVertex.getParentVertex();
				
			} while (currentVertex.getParentVertex() != null);
			
			Collections.reverse(vertices);
			
			return new GraphPath(vertices, pathCost);
		}

		@Override
		public int compareTo(AStarVertex another) {
			if (this.getTotalPathLength() > another.getTotalPathLength())
				return 1;
			else
				return -1;
		}
	}

}
