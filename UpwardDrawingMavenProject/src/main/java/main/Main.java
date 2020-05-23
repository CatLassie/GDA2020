package main;

import static java.lang.System.out;

import java.io.FileNotFoundException;
import java.io.IOException;

import util.GraphInstance;
import util.InputParser;

public class Main {

	public static void main(String[] args) {

		String fileName = "test_1";

		String localDir = System.getProperty("user.dir").split("UpwardDrawingMavenProject")[0];
		// String readPath = localDir + "\\instances\\vc-exact_"+instanceN+".gr";
		// String writePath = localDir + "\\solutions\\vc-exact_"+instanceN+".gr";
		String readPath = localDir + "instances\\" + fileName + ".json";
		// out.println(readPath);
		
		GraphInstance inst = null;

		try {
			inst = InputParser.readInstance(readPath);

		} catch (IOException e) {
			out.println("Sorry, file not found!");
			System.exit(-1);
		}

		out.println(inst);
	}

}
