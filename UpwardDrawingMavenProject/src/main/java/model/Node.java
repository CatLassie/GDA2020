package model;

import java.util.List;
import java.util.ArrayList;

public class Node {
	public int id;
	public int x;
	public int y;

	public transient List<Node> pred = new ArrayList<Node>();
	public transient List<Node> succ = new ArrayList<Node>();
	
	@Override
	public String toString() {
		return "{id=" + id + ", x=" + x + ", y=" + y + "}";
	}
	
	/*
	@Override
	public String toString() {
		List<Integer> predId = new ArrayList<Integer>();
		if(pred != null) {
			for(int i = 0; i < pred.size(); i++) {
				predId.add(pred.get(i).id);
			}			
		}
		return "{id=" + id + ", x=" + x + ", y=" + y + ", preds=" + predId + "}";
	}
	*/
}
