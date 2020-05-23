package util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import com.google.gson.Gson;

import model.GraphInstance;

public class InputParser {

	public static GraphInstance readInstance(String path) throws IOException {
		Gson gson = new Gson();
		Reader reader = new FileReader(path);

		// Convert JSON File to Java Object
		GraphInstance inst = gson.fromJson(reader, GraphInstance.class);
		return inst;
	}
}
