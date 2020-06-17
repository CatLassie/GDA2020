package model;

import java.util.ArrayList;

public class GraphInstance {
	private int width;
	private int height;
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;

	public GraphInstance() {
	}

	public GraphInstance(int width, int height, ArrayList<Node> nodes, ArrayList<Edge> edges) {
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

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	@Override
	public String toString() {
		return "GrapthInstance: \n	width=" + width + ",\n	height=" + height + ",\n	nodes=" + nodes + ",\n	edges="
				+ edges + "]";
	}
}
