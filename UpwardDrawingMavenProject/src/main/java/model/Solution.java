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
	
	
	public void positionGraphOnGrid() {
		ArrayList<Integer> nextLayerNodes = new ArrayList<Integer>();
		for (int i = 0; i <= height; i++) {
			nextLayerNodes = getNextLayerNodes(nextLayerNodes);
			
			// currently occupied position on layer
			boolean[] occupiedX = new boolean[width+1];
			
			// straight lines get priority assignment
			for (int j = 0; j <= width; j++) {
				if (j < nextLayerNodes.size()) {
					Node node = nodes.get(nextLayerNodes.get(j)); //TODO: more like find by id but ok for now
					
					// DEBUG
					// if not equal, the alg. will break!!!
					// boolean equal = nextLayerNodes.get(j) == node.id;
					// out.println("node idx: " + nextLayerNodes.get(j) + " == node id: " + node.id +" "+ equal );
					//
					
					if (!node.assigned && node.dummy && node.pred.dummy) {
						int diff = node.pred.x - node.pred.pred.x;
						int x = node.pred.x + diff;
						out.println(i+1 + " " + node.pred.x + " " + node.pred.pred.x);
						if(occupiedX[x] || x > width || x < 0) {
							out.println("Sorry, couldnt position dummy node " + node.id + " on straight line");
							System.exit(-1);
						}
						node.x = x;
						occupiedX[x] = true;
						node.y = i;
						node.assigned = true;
					}
				}
			}
			
			// dummy nodes get second priority assignment
			for (int j = 0; j <= width; j++) {
				if (j < nextLayerNodes.size()) {
					Node node = nodes.get(nextLayerNodes.get(j)); //TODO: more like find by id but ok for now
					
					// DEBUG
					// if not equal, the alg. will break!!!
					// boolean equal = nextLayerNodes.get(j) == node.id;
					// out.println("node idx: " + nextLayerNodes.get(j) + " == node id: " + node.id +" "+ equal );
					//
					
					if (!node.assigned && node.dummy && !node.pred.dummy) {				
						// find a position for dummy node as close to pred x as possible
						int x = node.pred.x;
						int offset = 1;
						while(occupiedX[x] || x > width || x < 0) {
							x = offset % 2 == 1 ? x + offset : x - offset;
							if(offset > width) {
								out.println("Sorry, couldnt find a position for dummy node " + node.id);
								System.exit(-1);
							}
						}
						node.x = x;
						occupiedX[x] = true;
						node.y = i;
						node.assigned = true;
					}
				}
			}
			
			// normal nodes get assigned last
			for (int j = 0; j <= width; j++) {
				if (j < nextLayerNodes.size()) {
					Node node = nodes.get(nextLayerNodes.get(j)); //TODO: more like find by id but ok for now
					
					// DEBUG
					// if not equal, the alg. will break!!!
					// boolean equal = nextLayerNodes.get(j) == node.id;
					// out.println("node idx: " + nextLayerNodes.get(j) + " == node id: " + node.id +" "+ equal );
					//
					
					if (!node.assigned && !node.dummy) {
						// first check all dummy nodes and addthem (predecessor x has to be known to calculatenew x)
						int x = 0;
						while(occupiedX[x]) {
							if(x > width) {
								out.println("Sorry, couldnt find a position for node " + node.id);
								System.exit(-1);
							}
							x++;
						}
						node.x = x;
						occupiedX[x] = true;
						node.y = i;
						node.assigned = true;
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
				int sourceId = sources.get(i);

				List<Integer> row = adjacencyMatrix.get(sourceId);

				for (int j = 0; j < row.size(); j++) {
					if (row.get(j) == 1) {
						boolean allSourcesAssigned = true;
						for (int k = 0; k < adjacencyMatrix.size(); k++) {
							if (adjacencyMatrix.get(k).get(j) == 1 && !nodes.get(k).assigned) {
								allSourcesAssigned = false;
								// add dummy node here for each unassigned node; NOOOOO!!!!
							}
						}
						if(!newSources.contains(j)) {
							if (allSourcesAssigned) {
								// add normal node
								newSources.add(j);
							} else {
								// add dummy node
								addDummyNode(newSources, sourceId, j);
							}
						}
					}
				}
			}

		} else {
			// get first layer
			for (int i = 0; i < nodes.size(); i++) {
				int nodeId = nodes.get(i).id;
				boolean isTarget = false;
				for (int j = 0; j < adjacencyMatrix.size(); j++) {
					List<Integer> row = adjacencyMatrix.get(j);
					if (row.get(nodeId) == 1) {
						isTarget = true;
					}
				}
				if (!isTarget) {
					newSources.add(nodeId);
				}
			}
		}

		// out.println(newSources);

		return newSources;
	}
	
	private void addDummyNode(ArrayList<Integer> newSources, int sourceId, int targetId) {
		// create dummy node and add it to nodes and sources
		// TODO: should find by id!!!
		Node source = nodes.get(sourceId);
		Node newDummy = new Node();
		newDummy.id = nodes.size();
		newDummy.dummy = true;
		
		// all dummy nodes have a pred, and all except last dummy have a succ
		newDummy.pred = source;
		if(source.dummy) {
			source.succ = newDummy;
		}
		
		nodes.add(newDummy);
		newSources.add(newDummy.id);
		
		// update adjacency matrix by rerouting edge through dummy node
		// add new column
		for(int i = 0; i < adjacencyMatrix.size(); i++) {
			adjacencyMatrix.get(i).add(0);
		}
		// add new row
		List<Integer> newRow = new ArrayList<Integer>();
		for(int i = 0; i < nodes.size(); i++) {
			newRow.add(0);
		}
		adjacencyMatrix.add(newRow);
		// re-arrange adjacency matrix
		adjacencyMatrix.get(sourceId).set(targetId, 0);
		adjacencyMatrix.get(sourceId).set(newDummy.id, 1);
		adjacencyMatrix.get(newDummy.id).set(targetId, 1);
	}
	

	private void calculateFeasibility() {
		out.println("calculateFeasibility()");
	}

	private void calculateCost() {
		out.println("calculateCost()");
	}
	
	public GraphInstance getGraphInstanceFromSolution() {
		// filter out dummy nodes
		ArrayList<Node> originalNodes = new ArrayList<Node>();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(!node.dummy) {
				originalNodes.add(node);
			} 
		}
		return new GraphInstance(width, height, originalNodes, edges);
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
