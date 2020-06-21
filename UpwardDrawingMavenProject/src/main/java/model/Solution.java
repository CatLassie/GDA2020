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
	// a list of nodes for each Y coordinate
	private List<List<Node>> layerList;
	private int topLayer = 0;
	private boolean[][] positionOccupied;
	private int cost;
	private boolean isFeasible;
	private boolean verbose;

	public Solution(GraphInstance inst, boolean verbose) {
		this.verbose = verbose;
		if(verbose) {
			out.println("initializing solution instance...\n");
		}
		
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
		for (int i = 0; i < nodes.size(); i++) {
			List<Integer> row = new ArrayList<Integer>();
			for (int j = 0; j < nodes.size(); j++) {
				row.add(0);
			}
			adjacencyMatrix.add(row);
		}
		for (int i = 0; i < inst.getEdges().size(); i++) {
			Edge edge = inst.getEdges().get(i);
			adjacencyMatrix.get(edge.source).set(edge.target, 1);
		}
	}

	// assign Y coordinate to all nodes and add nodes to layerList
	// initialize positionOccupied matrix
	public void computeLayersForNodes() {
		if(verbose) {
			out.println("computing layers (Y coordinate) for all " + nodes.size() + " nodes:");
		}
		
		layerList = new ArrayList<List<Node>>();
		for (int i = 0; i < height + 1; i++) {
			layerList.add(new ArrayList<Node>());
		}
		
		for (int i = 0; i < nodes.size(); i++) {
			if(verbose) {
				if(nodes.size() >= 20) {
					if((i+1) % 20 == 0) {
						if((i+1) % 600 == 0) {
							out.println(i+1);	
						} else {
							out.print((i+1) + ", ");
						}					
					}		
				} else {
					if(i == nodes.size() -1) {
						out.print((i+1));	
					}
				}
			}
			
			Node node = nodes.get(i);
			int y = yRecursion(node.id, 0);
			node.y = y;
			layerList.get(y).add(node);
			
			// find last layer
			if(y > topLayer) {
				topLayer = y;
			}
		}
		
		if(verbose) {
			out.println("\n");
		}

		positionOccupied = new boolean[topLayer + 1][width + 1];
	}

	// recursivley compute the length of longest [source -> node] path
	private int yRecursion(int nodeId, int currentDepth) {
		int maxDepth = currentDepth;

		for (int i = 0; i < adjacencyMatrix.size(); i++) {
			List<Integer> row = adjacencyMatrix.get(i);
			if (row.get(nodeId) == 1) {

				// add predecessor
				Node node = nodes.get(nodeId);
				Node pred = nodes.get(i);
				if (!node.pred.contains(pred)) {
					node.pred.add(pred);
				}

				int newDepth = yRecursion(i, currentDepth + 1);
				if (newDepth > maxDepth) {
					maxDepth = newDepth;
				}
			}
		}

		return maxDepth;
	}

	public void positionGraphOnGrid() {
		if(verbose) {
			out.println("assigning X coordinate for nodes for all " + topLayer + " layers:");
		}
		
		for (int i = 0; i <= topLayer; i++) {
			if(verbose) {
				if((i+1) % 30 == 0 || i == topLayer) {
					out.println(i);	
				} else {
					out.print(i + ", ");
				}
			}
			
			List<Node> nextLayerNodes = layerList.get(i);

			for (int j = 0; j <= width; j++) {
				if (j < nextLayerNodes.size()) {
					Node node = nodes.get(nextLayerNodes.get(j).id); // TODO: more like find by id but ok for now

					// OBSOLETE COMMENT!
					// DEBUG
					// if not equal, the alg. will break!!!
					// boolean equal = nextLayerNodes.get(j) == node.id;
					// out.println("node idx: " + nextLayerNodes.get(j) + " == node id: " + node.id +" "+ equal );

					if (!node.assigned) {
						int x = 0;
						searchX:
						while (true) {
							if (x > width) {
								out.println("Sorry, couldnt find a position for node " + node.id);
								System.exit(-1);
							}

							if (!positionOccupied[node.y][x]) {
								boolean validPosition = true;
								// check for each edge if it can be positioned with current x
								for (int k = 0; k < node.pred.size(); k++) {
									Node pred = node.pred.get(k);
									validPosition = validPosition && isEdgeFeasible(pred.x, pred.y, x, node.y);
								}

								if (validPosition) {
									node.x = x;
									node.assigned = true;
									positionOccupied[node.y][x] = true;
									break searchX;
								}
							}

							x++;
						}
					}
				}
			}

		}
		if(verbose) {
			out.println("");
		}
	}

	// compute all points where edge crosses grid and check if they are occupied
	private boolean isEdgeFeasible(int x1, int y1, int x2, int y2) {
		boolean isFeasible = true;
		double invSlope;

		if (x1 == x2) {
			invSlope = 0;
		} else {
			double slope = (double) (y2 - y1) / (double) (x2 - x1);
			invSlope = 1 / slope;
		}

		for (int yi = y1 + 1; yi < y2; yi++) {
			double xi = x1 + ((yi - y1) * invSlope);
			// round to 4 decimal places
			xi = Math.round(xi * 10000.0) / 10000.0;
			if (xi % 1 == 0) {
				int xi_int = (int) xi;
				isFeasible = isFeasible && !positionOccupied[yi][xi_int];
			}
		}

		return isFeasible;
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

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public List<List<Integer>> getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public List<List<Node>> getLayerList() {
		return layerList;
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

		return "Solution:" + feasiblePrint + costPrint + nodesPrint + nodeNumberPrint + edgesPrint + matrixPrint;
	}
}
