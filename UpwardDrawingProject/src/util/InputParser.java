package util;

import static java.lang.System.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class InputParser {
	private int width;
	private int height;
	// private List<List<Integer>> adjacencyList = new ArrayList<>();
	// private boolean[][] adjacencyMatrix;
	
	public InputParser() { }
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	
	public void readInstance(String path) throws FileNotFoundException {
		
		
		File file = new File(path);
		
		
		InputStream inputStream = new FileInputStream(file);
		 //JsoParser parser; // = Json.createParser(new StringReader("[]"));

		
		/*
		Scanner s = new Scanner(new File(path));
		
		s.skip("p td ");
		inst.numVertices = s.nextInt();
		s.nextLine();
		
		for(int i=0; i<inst.numVertices; ++i) {
			inst.adjacencyList.add(new ArrayList<Integer>());
		}
		
		inst.adjacencyMatrix = new boolean[inst.numVertices][inst.numVertices];
		for(int i=0; i<inst.numVertices; ++i) {
			Arrays.fill(inst.adjacencyMatrix[i], false);
		}
				
		while(s.hasNext()) {
			s.skip("(#.*[\r\n]+)*");
			int a = s.nextInt() - 1; // VERTICES START FROM 0!!!
			int b = s.nextInt() - 1;
			inst.adjacencyList.get(a).add(b);
			inst.adjacencyList.get(b).add(a);
			inst.adjacencyMatrix[a][b] = true;
			inst.adjacencyMatrix[b][a] = true;
		}
		
		s.close();
		
		for(int i=0; i<inst.numVertices; ++i) {
			List<Integer> al = inst.adjacencyList.get(i);
			inst.adjacencyList.set(i, al.stream().distinct().collect(Collectors.toList()));
		}
				
		return inst;
		*/
	}
	
	/*
	public int getK() {
		return K;
	}

	public int getNumVertices() {
		return numVertices;
	}

	public List<List<Integer>> getAdjacencyList() {
		return adjacencyList;
	}

	public final boolean[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}
	*/
}
