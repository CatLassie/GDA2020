package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphInstance {
	private int width;
	private int height;
	private ArrayList<HashMap<String, Integer>> nodes;
	private ArrayList<HashMap<String, Integer>> edges;

	public GraphInstance() {
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<HashMap<String, Integer>> getNodes() {
		return nodes;
	}

	public List<HashMap<String, Integer>> getEdges() {
		return edges;
	}

	@Override
	public String toString() {
		return "GrapthInstance: \n	width=" + width + ",\n	height=" + height + ",\n	nodes=" + nodes + ",\n	edges="
				+ edges + "]";
	}
}
