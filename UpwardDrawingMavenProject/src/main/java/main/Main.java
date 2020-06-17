package main;

import static java.lang.System.out;
import java.io.IOException;

import model.GraphInstance;
import model.Solution;
import util.InputParser;
import util.SolutionWriter;

public class Main {

	public static void main(String[] args) {

		String fileName = "test_1_not_upward";

		String localDir = System.getProperty("user.dir").split("UpwardDrawingMavenProject")[0];
		// String readPath = localDir + "\\instances\\vc-exact_"+instanceN+".gr";
		// String writePath = localDir + "\\solutions\\vc-exact_"+instanceN+".gr";
		String readPath = localDir + "instances\\" + fileName + ".json";
		String writePath = localDir + "solutions\\" + fileName + "_solution.json";
		// out.println(readPath);
		
		GraphInstance inst = null;

		try {
			inst = InputParser.readInstance(readPath);
		} catch (IOException e) {
			out.println("Sorry, couldnt open file!");
			System.exit(-1);
		}
		
		//Solution solution = new Solution(inst);
		//solution.positionGraphOnGrid();
		
		// Write solution to file
		try {
			// SolutionWriter.writeSolution(writePath, solution.getGraphInstanceFromSolution());
			SolutionWriter.writeSolution(writePath, inst);
		} catch (IOException e) {
			out.println("Sorry, couldnt write to file!");
			System.exit(-1);
		}
		
		
		out.println(inst);
		//out.println("\n" + solution);
		// out.println("\n"+ solution.getGraphInstanceFromSolution());
	}

}
