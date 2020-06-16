package model;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;

public class Solution {
	private int width;
	private int height;
	private ArrayList<HashMap<String, Integer>> nodes;
	private ArrayList<HashMap<String, Integer>> edges;
	private ArrayList<ArrayList<Integer>> adjacencyList;
	// private ArrayList<HashMap<String, Integer>> nodesD;
	// private int[][] adjacencyMatrixD;
	private int[][] adjacencyMatrix;
	private int cost;
	private boolean isFeasible;

	public Solution(GraphInstance inst) {
		width = inst.getWidth();
		height = inst.getHeight();

		nodes = new ArrayList<HashMap<String, Integer>>();
		// nodesD = new ArrayList<HashMap<String, Integer>>();
		for (int i = 0; i < inst.getNodes().size(); i++) {
			HashMap<String, Integer> node = inst.getNodes().get(i);
			HashMap<String, Integer> newNode = new HashMap<String, Integer>();
			newNode.put("id", node.get("id"));
			newNode.put("x", 0);
			newNode.put("y", 0);
			newNode.put("assigned", 0);
			nodes.add(newNode);
			// nodesD.add((HashMap)newNode.clone());
		}
		
		edges = inst.getEdges();

		adjacencyList = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < inst.getNodes().size(); i++) {
			adjacencyList.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < inst.getEdges().size(); i++) {
			HashMap<String, Integer> edge = inst.getEdges().get(i);
			adjacencyList.get(edge.get("source")).add(edge.get("target"));
		}

		adjacencyMatrix = new int[nodes.size()][nodes.size()];
		// adjacencyMatrixD = new int[nodes.size()][nodes.size()];

		for (int i = 0; i < inst.getEdges().size(); i++) {
			HashMap<String, Integer> edge = inst.getEdges().get(i);
			adjacencyMatrix[edge.get("source")][edge.get("target")] = 1;
			// adjacencyMatrixD[edge.get("source")][edge.get("target")] = 1;
		}
	}

	public void positionGraphOnGrid() {
		ArrayList<Integer> nextLayerNodes = new ArrayList<Integer>();
		for (int i = 0; i <= height; i++) {
			nextLayerNodes = getNextLayerNodes(nextLayerNodes);
			for (int j = 0; j <= width; j++) {
				if (j < nextLayerNodes.size()) {
					HashMap<String, Integer> node = nodes.get(nextLayerNodes.get(j));
					// HashMap<String, Integer> node = nodesD.get(nextLayerNodes.get(j));
					if (node.get("assigned") == 0) {
						node.put("x", j);
						node.put("y", i);
						node.put("assigned", 1);
					}
				}
			}

		}
	}

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
							}
						}
						if(!newSources.contains(j)) {
							if (allSourcesAssigned) {
								// add normal node
								newSources.add(j);
							} else {
								/*
								// add dummy node								
								HashMap<String, Integer> dummyNode = new HashMap<String, Integer>();
								dummyNode.put("id", nodesD.size());
								dummyNode.put("x", 0);
								dummyNode.put("y", 0);
								dummyNode.put("assigned", 0);
								dummyNode.put("dummy", 1);
								//TODO: dummyNode.put("sourceXCoord", )
								nodesD.add(dummyNode);
								newSources.add(nodesD.size());
								*/
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

	private void calculateFeasibility() {
		out.println("calculateFeasibility()");
	}

	private void calculateCost() {
		out.println("calculateCost()");
	}
	
	public GraphInstance getGraphInstanceFromSolution() {
		return new GraphInstance(width, height, nodes, edges);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ArrayList<HashMap<String, Integer>> getNodes() {
		return nodes;
	}

	public ArrayList<ArrayList<Integer>> getAdjacencyList() {
		return adjacencyList;
	}

	public int[][] getAdjacencyMatrix() {
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
		String adjacencyListPrint = "\n\tadjacency list: " + adjacencyList;

		String matrixPrint = "\n\tmatrix: [";
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			int[] row = adjacencyMatrix[i];
			matrixPrint += "\n\t\t ";
			for (int j = 0; j < row.length; j++) {
				matrixPrint += row[j] + " ";
			}
		}
		matrixPrint += "\n\t\t]";

		return "Solution:" + feasiblePrint + costPrint + nodesPrint + nodeNumberPrint + edgesPrint + adjacencyListPrint + matrixPrint;
	}

}
