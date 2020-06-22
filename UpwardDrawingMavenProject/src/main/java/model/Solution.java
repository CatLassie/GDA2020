package model;

import static java.lang.System.out;

import java.awt.geom.Line2D;
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
	private int[][] positionOccupied; // 0 -> free, 1 -> line, 2 -> node
	private int cost;
	private boolean verbose;
	
	// Copy Constructor
	// deep copy (kind of) of solution (for keeping track of best solution)
	public Solution(Solution s) {
		width = s.width;
		height = s.height;
		cost = s.cost;
		topLayer = s.topLayer;
		verbose = s.verbose;
		edges = s.edges;
		adjacencyMatrix = s.adjacencyMatrix;
		
		nodes = new ArrayList<Node>();
		for(int i = 0; i < s.nodes.size(); i++) {
			nodes.add(s.nodes.get(i).copy());
		}
				
		layerList = new ArrayList<List<Node>>();
		for (int i = 0; i < topLayer + 1; i++) {
			List<Node> layer = new ArrayList<Node>();
			for(int j = 0; j < s.layerList.get(i).size(); j++) {
				layer.add(s.layerList.get(i).get(j).copy());
			}
			layerList.add(layer);
		}
		
		positionOccupied = new int[topLayer + 1][width + 1];
		for(int i = 0; i < topLayer + 1; i++) {
			for(int j = 0; j < width + 1; j++) {
				positionOccupied[i][j] = s.positionOccupied[i][j];
			}
		}	
	}
	
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
		
		// add pred to nodes
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);

			for (int j = 0; j < nodes.size(); j++) {				
				if(adjacencyMatrix.get(j).get(i) == 1) {
					node.pred.add(nodes.get(j).id);
				}
			}
		}
		
		// add succ to nodes
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			List<Integer> row = adjacencyMatrix.get(i);
			
			for (int j = 0; j < row.size(); j++) {
				if(row.get(j) == 1) {
					node.succ.add(nodes.get(j).id);
				}
			}
		}
	}
	
	public void computeInitialFeasibleSolution() {
		computeLayersForNodes();
		positionGraphOnGrid();
		calculateCost();
		if(verbose) {
			out.println("initial cost is: "+ cost +"\n");
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
				verboseInitY(i);
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

		positionOccupied = new int[topLayer + 1][width + 1];
	}

	// recursivley compute the length of longest [source -> node] path
	private int yRecursion(int nodeId, int currentDepth) {
		int maxDepth = currentDepth;

		for (int i = 0; i < adjacencyMatrix.size(); i++) {
			List<Integer> row = adjacencyMatrix.get(i);
			if (row.get(nodeId) == 1) {
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
				verboseInitX(i);
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

					int x = 0;
					searchX:
					while (true) {
						if (x > width) {
							out.println("Sorry, couldnt find a position for node " + node.id);
							System.exit(-1);
						}

						if (positionOccupied[node.y][x] == 0) {
							boolean validPosition = true;
							// check for each edge if it can be positioned with current x
							for (int k = 0; k < node.pred.size(); k++) {
								Node pred = nodes.get(node.pred.get(k));
								validPosition = validPosition && isEdgeFeasible(pred.x, pred.y, x, node.y);
							}

							if (validPosition) {
								for (int k = 0; k < node.pred.size(); k++) {
									Node pred = nodes.get(node.pred.get(k));
									updateEdgeOccupyValue(pred.x, pred.y, node.x, node.y, 0);
								}
								for (int k = 0; k < node.pred.size(); k++) {
									Node pred = nodes.get(node.pred.get(k));
									updateEdgeOccupyValue(pred.x, pred.y, x, node.y, 1);
								}
								node.x = x;
								positionOccupied[node.y][x] = 2;								
								break searchX;
							}
						}

						x++;
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
			// xi = Math.round(xi * 10000.0) / 10000.0;
			if (xi % 1 == 0) {
				int xi_int = (int) xi;
				isFeasible = isFeasible && positionOccupied[yi][xi_int] != 2;
			}
		}

		return isFeasible;
	}
	
	// compute all points where edge crosses grid and occupied value
	private void updateEdgeOccupyValue(int x1, int y1, int x2, int y2, int newValue) {
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
			// xi = Math.round(xi * 10000.0) / 10000.0;
			if (xi % 1 == 0) {
				int xi_int = (int) xi;
				positionOccupied[yi][xi_int] = newValue;
			}
		}
	}
	
	// check if node can be put on new X coordinate
	public boolean isMoveFeasible(Node node, int newX) {
		boolean isFeasible = true;
		
		if (positionOccupied[node.y][newX] == 0) {
			// check for each edge if it can be positioned with current x
			for (int i = 0; i < node.pred.size(); i++) {
				Node pred = nodes.get(node.pred.get(i));
				isFeasible = isFeasible && isEdgeFeasible(pred.x, pred.y, newX, node.y);
			}
			for (int i = 0; i < node.succ.size(); i++) {
				Node succ = nodes.get(node.succ.get(i));
				isFeasible = isFeasible && isEdgeFeasible(newX, node.y, succ.x, succ.y);
			}
		} else {
			isFeasible = false;
		}
		
		return isFeasible;
	}
	
	// calculate how move will change solution cost with incremental evaluation
	// edges only taken whose coordinates are in a small rectangle 
	public int computeMoveCost(Node node, int newX) {
		int minX = Math.min(node.x, newX);
		int maxX = Math.max(node.x, newX);
		
		int minY = node.y;
		for(int i = 0; i < node.pred.size(); i++) {
			int predY = nodes.get(node.pred.get(i)).y;
			if(predY < minY) {
				minY = predY;
			}
		}
		
		int maxY = node.y;
		for(int i = 0; i < node.succ.size(); i++) {
			int succY = nodes.get(node.succ.get(i)).y;
			if(succY > maxY) {
				maxY = succY;
			}
		}
		
		// completely above/under/leftOf/rightOf
		ArrayList<Edge> relevantEdges = new ArrayList<Edge>();
		for(int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			Node source = nodes.get(edge.source);
			Node target = nodes.get(edge.target);
			boolean above = source.y >= maxY && target.y >= maxY;
			boolean under = source.y <= minY && target.y <= minY;
			boolean leftOf = source.x <= minX && target.x <= minX;
			boolean rightOf = source.x >= maxX && target.x >= maxX;
			if(!(above || under || leftOf || rightOf)) {
				relevantEdges.add(edge);
			}
		}
		
		int currentCost = 0;
		for(int i = 0; i < relevantEdges.size() - 1; i++) {
			for(int j = i + 1; j < relevantEdges.size(); j++) {
				Edge edge1 = relevantEdges.get(i);
				Edge edge2 = relevantEdges.get(j);
				Node source1 = nodes.get(edge1.source);
				Node target1 = nodes.get(edge1.target);
				Node source2 = nodes.get(edge2.source);
				Node target2 = nodes.get(edge2.target);
				if(doEdgesIntersect(source1.x,source1.y, target1.x,target1.y, source2.x,source2.y, target2.x,target2.y)) {
					currentCost = currentCost + 1;
				}
			}
		}
		
		int nextCost = 0;
		for(int i = 0; i < relevantEdges.size() - 1; i++) {
			for(int j = i + 1; j < relevantEdges.size(); j++) {
				Edge edge1 = relevantEdges.get(i);
				Edge edge2 = relevantEdges.get(j);
				Node source1 = nodes.get(edge1.source);
				Node target1 = nodes.get(edge1.target);
				Node source2 = nodes.get(edge2.source);
				Node target2 = nodes.get(edge2.target);
				if(doEdgesIntersect(source1.id == node.id ? newX : source1.x, source1.y,
									target1.id == node.id ? newX : target1.x, target1.y,
									source2.id == node.id ? newX : source2.x, source2.y,
									target2.id == node.id ? newX : target2.x, target2.y)) {
					nextCost = nextCost + 1;
				}
			}
		}
		
		return nextCost - currentCost;
	}
	
	// re-assign X coord of node, update positionOccupied and cost
	public void performMove(Node node, int newX, int costChange) {
		positionOccupied[node.y][node.x] = 0;
		positionOccupied[node.y][newX] = 2;
		
		for (int i = 0; i < node.pred.size(); i++) {
			Node pred = nodes.get(node.pred.get(i));
			updateEdgeOccupyValue(pred.x, pred.y, node.x, node.y, 0);
		}
		for (int i = 0; i < node.succ.size(); i++) {
			Node succ = nodes.get(node.succ.get(i));
			updateEdgeOccupyValue(node.x, node.y, succ.x, succ.y, 0);
		}
		for (int i = 0; i < node.pred.size(); i++) {
			Node pred = nodes.get(node.pred.get(i));
			updateEdgeOccupyValue(pred.x, pred.y, newX, node.y, 1);
		}
		for (int i = 0; i < node.succ.size(); i++) {
			Node succ = nodes.get(node.succ.get(i));
			updateEdgeOccupyValue(newX, node.y, succ.x, succ.y, 1);
		}
		
		node.x = newX;
		cost = cost + costChange;
	}
		
	// calculate total edge crossings from scratch
	public void calculateCost() {
		cost = 0;
		for(int i = 0; i < edges.size() - 1; i++) {
			for(int j = i + 1; j < edges.size(); j++) {
				Edge edge1 = edges.get(i);
				Edge edge2 = edges.get(j);
				Node source1 = nodes.get(edge1.source);
				Node target1 = nodes.get(edge1.target);
				Node source2 = nodes.get(edge2.source);
				Node target2 = nodes.get(edge2.target);
				if(doEdgesIntersect(source1.x,source1.y, target1.x,target1.y, source2.x,source2.y, target2.x,target2.y)) {
					cost = cost + 1;
				}
			}
		}
	}
	
	private boolean doEdgesIntersect(int source1x,int source1y,int target1x,int target1y,int source2x,int source2y,int target2x,int target2y) {
		boolean shareSource = source1x == source2x && source1y == source2y;
		boolean shareTarget = target1x == target2x && target1y == target2y;
		boolean sequential1 = target1x == source2x && target1y == source2y;
		boolean sequential2 = source1x == target2x && source1y == target2y;
		if(shareSource || shareTarget || sequential1 || sequential2) {
			return false;
		}
		return Line2D.linesIntersect(source1x,source1y, target1x,target1y, source2x,source2y, target2x,target2y);
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

	@Override
	public String toString() {
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

		return "Solution:" + costPrint + nodesPrint + nodeNumberPrint + edgesPrint + matrixPrint;
	}
	
	public void verboseInitY(int i) {
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
	
	public void verboseInitX(int i) {
		if((i+1) % 30 == 0 || i == topLayer) {
			out.println(i);	
		} else {
			out.print(i + ", ");
		}
	}
}
