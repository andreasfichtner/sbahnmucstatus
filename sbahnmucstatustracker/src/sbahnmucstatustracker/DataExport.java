package sbahnmucstatustracker;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import sbahnmucstatustracker.db.Database;
import sbahnmucstatustracker.db.DatabaseReader;

public class DataExport {

	private static final String FILENAME = "export.csv";
	private static final String DELIMITER = ";";

	public static void main(String[] args) throws SQLException,
			FileNotFoundException, UnsupportedEncodingException {
		new DataExport().start();
	}

	public void start() throws SQLException, FileNotFoundException,
			UnsupportedEncodingException {

		List<String> lines = DatabaseReader.fetchLines();
		writeData(lines);
		Database.GET().closeConnection();
	}

	protected void writeData(List<String> lines) throws FileNotFoundException,
			UnsupportedEncodingException, SQLException {

		try (PrintWriter writer = new PrintWriter(FILENAME, "UTF-8")) {
			writeDataHeader(writer, lines);
			writeDataBody(writer, lines);
		}
	}

	private void writeDataHeader(PrintWriter writer, List<String> lines) {
		writer.print("\"Time\"");
		for (String line : lines) {
			writer.print(DELIMITER + "\"" + line + "\"");
		}
		writer.println(DELIMITER);
	}

	private void writeDataBody(PrintWriter writer, List<String> lines)
			throws SQLException {
		List<Object[]> lineData = DatabaseReader.fetchLineData(lines);

		if (lineData.isEmpty()) {
			return;
		}

		final int columnCount = lineData.get(0).length;

		for (Object[] currentLine : lineData) {
			writer.print("\"");
			writer.print((String) currentLine[0]);
			writer.print("\"");

			for (int i = 1; i < columnCount; i++) {
				writer.print(DELIMITER);
				writer.print("\"");
				writer.print((Integer) currentLine[i]);
				writer.print("\"");
			}

			writer.println(DELIMITER);
		}
	}
}
