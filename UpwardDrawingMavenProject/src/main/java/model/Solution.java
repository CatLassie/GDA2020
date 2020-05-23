package model;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;

public class Solution {
	private ArrayList<HashMap<String, Integer>> nodes;
	private ArrayList<ArrayList<Integer>> adjacencyList;
	private int cost;
	private boolean isFeasible;
	
	public Solution(GraphInstance inst) {
		nodes = new ArrayList<HashMap<String, Integer>>();
		for(int i = 0; i < inst.getNodes().size(); i++) {
			HashMap<String, Integer> node = inst.getNodes().get(i);
			HashMap<String, Integer> newNode = new HashMap<String, Integer>();
			newNode.put("id", node.get("id"));
			newNode.put("x", 0);
			newNode.put("y", 0);
			nodes.add(newNode);
		}
		
		adjacencyList = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < inst.getNodes().size(); i++) {
			adjacencyList.add(new ArrayList<Integer>());
		}
		for(int i = 0; i < inst.getEdges().size(); i++) {
			HashMap<String, Integer> edge = inst.getEdges().get(i);
			adjacencyList.get(edge.get("source")).add(edge.get("target"));
		}
	}
	
	private void calculateCost() {
		out.println("calculateCost()");
	}
	
	public int getNodeNumber() {
		return nodes.size();
	}
	
	@Override
	public String toString() {
		String feasiblePrint = "\n\tfeasible: " + isFeasible;
		String costPrint = "\n\tcost: " + cost;
		String nodeNumberPrint = "\n\tnode number: " + getNodeNumber();
		String nodesPrint = "\n\tnodes: " + nodes;
		String edgesPrint = "\n\tedges: " + adjacencyList;
		return "Solution:" + feasiblePrint + costPrint + nodesPrint + nodeNumberPrint + edgesPrint;
	}

}
