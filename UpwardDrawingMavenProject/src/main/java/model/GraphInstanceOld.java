package model;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphInstanceOld {
	private int width;
	private int height;
	private ArrayList<HashMap<String, Integer>> nodes;
	private ArrayList<HashMap<String, Integer>> edges;

	public GraphInstanceOld() {
	}

	public GraphInstanceOld(int width, int height, ArrayList<HashMap<String, Integer>> nodes,
			ArrayList<HashMap<String, Integer>> edges) {
		this.width = width;
		this.height = height;
		this.nodes = nodes;
		this.edges = edges;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public ArrayList<HashMap<String, Integer>> getNodes() {
		return nodes;
	}
	
	public void setNodes(ArrayList<HashMap<String, Integer>> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<HashMap<String, Integer>> getEdges() {
		return edges;
	}
	
	public void setEdges(ArrayList<HashMap<String, Integer>> edges) {
		this.edges = edges;
	}

	@Override
	public String toString() {
		return "GrapthInstance: \n	width=" + width + ",\n	height=" + height + ",\n	nodes=" + nodes + ",\n	edges="
				+ edges + "]";
	}
}
