package model;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;

public class Solution {
	private int width;
	private int height;
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	private List<List<Integer>> adjacencyMatrix;
	private int cost;
	private boolean isFeasible;

	public Solution(GraphInstance inst) {
		width = inst.getWidth();
		height = inst.getHeight();
		
		// Set Nodes
		nodes = new ArrayList<Node>();
		for (int i = 0; i < inst.getNodes().size(); i++) {
			Node node = inst.getNodes().get(i);
			Node newNode = new Node();
			newNode.id = node.id;
			newNode.x = 0;
			newNode.y = 0;
			nodes.add(newNode);
		}
		
		// Set Edges
		edges = inst.getEdges();
		
		// Calculate Adjacency Matrix
		adjacencyMatrix = new ArrayList<List<Integer>>();
		for(int i = 0; i < nodes.size(); i++) {
			List<Integer> row = new ArrayList<Integer>();
			for(int j = 0; j < nodes.size(); j++) {
				row.add(0);
			}
			adjacencyMatrix.add(row);
		}
		for (int i = 0; i < inst.getEdges().size(); i++) {
			Edge edge = inst.getEdges().get(i);
			adjacencyMatrix.get(edge.source).set(edge.target, 1);
		}
	}
	
	/*
	public void positionGraphOnGrid() {
		ArrayList<Integer> nextLayerNodes = new ArrayList<Integer>();
		for (int i = 0; i <= height; i++) {
			nextLayerNodes = getNextLayerNodes(nextLayerNodes);
			for (int j = 0; j <= width; j++) {
				if (j < nextLayerNodes.size()) {
					HashMap<String, Integer> node = nodes.get(nextLayerNodes.get(j));
					// HashMap<String, Integer> node = nodesD.get(nextLayerNodes.get(j));
					if (node.get("assigned") == 0) {
						// first check all dummy nodes and addthem (predecessor x has to be known to calculatenew x)
						node.put("x", j);
						node.put("y", i);
						node.put("assigned", 1);
					}
				}
			}

		}
	}
	*/
	
	/*
	private ArrayList<Integer> getNextLayerNodes(ArrayList<Integer> sources) {
		ArrayList<Integer> newSources = new ArrayList<Integer>();

		if (sources.size() > 0) {
			// get subsequent layers
			for (int i = 0; i < sources.size(); i++) {
				int source = sources.get(i);

				int[] row = adjacencyMatrix[source];

				for (int j = 0; j < row.length; j++) {
					if (row[j] == 1) {
						boolean allSourcesAssigned = true;
						for (int k = 0; k < adjacencyMatrix.length; k++) {
							if (adjacencyMatrix[k][j] == 1 && nodes.get(k).get("assigned") == 0) {
								allSourcesAssigned = false;
								// add dummy node here for each unassigned node
							}
						}
						if(!newSources.contains(j)) {
							if (allSourcesAssigned) {
								// add normal node
								newSources.add(j);
							} else {
	
							}
						}
					}
				}
			}

		} else {
			// get first layer
			for (int i = 0; i < nodes.size(); i++) {
				int nodeIdx = nodes.get(i).get("id");
				boolean isTarget = false;
				for (int j = 0; j < adjacencyMatrix.length; j++) {
					int[] row = adjacencyMatrix[j];
					if (row[nodeIdx] == 1) {
						isTarget = true;
					}
				}
				if (!isTarget) {
					newSources.add(nodeIdx);
				}
			}
		}

		// out.println(newSources);

		return newSources;
	}
	*/

	private void calculateFeasibility() {
		out.println("calculateFeasibility()");
	}

	private void calculateCost() {
		out.println("calculateCost()");
	}
	
	public GraphInstance getGraphInstanceFromSolution() {
		// TODO: removed dummy nodes
		return new GraphInstance(width, height, nodes, edges);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public List<List<Integer>> getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public int getCost() {
		return cost;
	}

	public boolean isFeasible() {
		return isFeasible;
	}

	@Override
	public String toString() {
		String feasiblePrint = "\n\tfeasible: " + isFeasible;
		String costPrint = "\n\tcost: " + cost;
		String nodeNumberPrint = "\n\tnode number: " + nodes.size();
		String nodesPrint = "\n\tnodes: " + nodes;
		String edgesPrint = "\n\tedges: " + edges;

		String matrixPrint = "\n\tmatrix: [";
		for (int i = 0; i < adjacencyMatrix.size(); i++) {
			List<Integer> row = adjacencyMatrix.get(i);
			matrixPrint += "\n\t\t ";
			for (int j = 0; j < row.size(); j++) {
				matrixPrint += row.get(j) + " ";
			}
		}
		matrixPrint += "\n\t\t]";

		return "Solution:" + feasiblePrint + costPrint + nodesPrint + nodeNumberPrint + edgesPrint  + matrixPrint;
	}

}
