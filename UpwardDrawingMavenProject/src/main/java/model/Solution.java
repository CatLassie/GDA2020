package model;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;

public class Solution {
	private int width;
	private int height;
	private ArrayList<HashMap<String, Integer>> nodes;
	private ArrayList<ArrayList<Integer>> adjacencyList;
	private int[][] adjacencyMatrix;
	private int cost;
	private boolean isFeasible;

	public Solution(GraphInstance inst) {
		width = inst.getWidth();
		height = inst.getHeight();

		nodes = new ArrayList<HashMap<String, Integer>>();
		for (int i = 0; i < inst.getNodes().size(); i++) {
			HashMap<String, Integer> node = inst.getNodes().get(i);
			HashMap<String, Integer> newNode = new HashMap<String, Integer>();
			newNode.put("id", node.get("id"));
			newNode.put("x", 0);
			newNode.put("y", 0);
			nodes.add(newNode);
		}

		adjacencyList = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < inst.getNodes().size(); i++) {
			adjacencyList.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < inst.getEdges().size(); i++) {
			HashMap<String, Integer> edge = inst.getEdges().get(i);
			adjacencyList.get(edge.get("source")).add(edge.get("target"));
		}

		adjacencyMatrix = new int[nodes.size()][nodes.size()];

		for (int i = 0; i < inst.getEdges().size(); i++) {
			HashMap<String, Integer> edge = inst.getEdges().get(i);
			adjacencyMatrix[edge.get("source")][edge.get("target")] = 1;
		}
	}

	public void positionGraphOnGrid() {
		ArrayList<Integer> nextLayerNodes = new ArrayList<Integer>();
		for (int i = 0; i <= height; i++) {
			nextLayerNodes = getNextLayerNodes(nextLayerNodes);
			for (int j = 0; j <= width; j++) {
				// out.print(nextLayerNodes);
			}

		}
	}

	private ArrayList<Integer> getNextLayerNodes(ArrayList<Integer> sources) {
		ArrayList<Integer> newSources = new ArrayList<Integer>();

		// get subsequent layers
		if (sources.size() > 0) {
			for (int i = 0; i < sources.size(); i++) {
				ArrayList<Integer> targets = adjacencyList.get(sources.get(i));
				for (int j = 0; j < targets.size(); j++) {
					if (!newSources.contains(targets.get(j))) {
						newSources.add(targets.get(j));
					}
				}
			}
			// get first layer
		} else {
			for (int i = 0; i < nodes.size(); i++) {
				int nodeIdx = nodes.get(i).get("id");
				boolean isNotTarget = true;
				for (int j = 0; j < adjacencyList.size(); j++) {
					ArrayList<Integer> targets = adjacencyList.get(j);
					for (int k = 0; k < targets.size(); k++) {
						if (targets.get(k) == nodeIdx) {
							isNotTarget = false;
						}
					}
				}
				if (isNotTarget) {
					newSources.add(nodeIdx);
				}
			}
		}

		out.println(newSources);

		return newSources;
	}

	private void calculateFeasibility() {
		out.println("calculateFeasibility()");
	}

	private void calculateCost() {
		out.println("calculateCost()");
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
		String edgesPrint = "\n\tedges: " + adjacencyList;

		String matrixPrint = "\n\tmatrix: [";
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			int[] row = adjacencyMatrix[i];
			matrixPrint += "\n\t\t ";
			for (int j = 0; j < row.length; j++) {
				matrixPrint += row[j] + " ";
			}
		}
		matrixPrint += "\n\t\t]";

		return "Solution:" + feasiblePrint + costPrint + nodesPrint + nodeNumberPrint + edgesPrint + matrixPrint;
	}

}
