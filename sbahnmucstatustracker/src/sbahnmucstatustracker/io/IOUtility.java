package sbahnmucstatustracker.io;

import java.io.BufferedReader;
import java.io.FileReader;

public class IOUtility {

	public static String getFileContent(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String content = sb.toString();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
