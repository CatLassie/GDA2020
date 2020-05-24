package util;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import model.GraphInstance;

public class SolutionWriter {
	
	public static void writeSolution(String path, GraphInstance inst) throws IOException {
		Gson gson = new Gson();
		FileWriter writer = new FileWriter(path);
        gson.toJson(inst, writer);
        writer.flush(); //flush data to file   <---
        writer.close(); //close write
	}

}
