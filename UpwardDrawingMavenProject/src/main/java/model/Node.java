package model;

import java.util.List;
import java.util.ArrayList;

public class Node {
	public int id;
	public int x;
	public int y;

	public transient List<Integer> pred = new ArrayList<Integer>();
	public transient List<Integer> succ = new ArrayList<Integer>();
	
	// deep copy of a node
	public Node copy() {
		Node copy = new Node();
		copy.id = id;
		copy.x = x;
		copy.y = y;
		
		List<Integer> copyPred = new ArrayList<Integer>();
		for(int i = 0; i < pred.size(); i++) {
			copyPred.add(pred.get(i));
		}
		copy.pred = copyPred;
		
		List<Integer> copySucc = new ArrayList<Integer>();
		for(int i = 0; i < succ.size(); i++) {
			copySucc.add(succ.get(i));
		}
		copy.succ = copySucc;
		
		return copy;
	}
	
	@Override
	public String toString() {
		return "{id=" + id + ", x=" + x + ", y=" + y + "}";
	}
}
