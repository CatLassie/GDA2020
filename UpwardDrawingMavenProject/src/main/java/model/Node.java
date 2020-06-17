package model;

public class Node {
	public int id;
	public int x;
	public int y;

	public boolean dummy = false;
	public int predX = -1;
	
	@Override
	public String toString() {
		return "{id=" + id + ", x=" + x + ", y=" + y + "}";
	}
	
	/*
	@Override
	public String toString() {
		return "{id=" + id + ", x=" + x + ", y=" + y + ", dummy=" + dummy + ", predX=" + predX + "}";
	}
	*/
}
