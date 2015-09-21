package sbahnmucstatustracker;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Don Burleson
 *
 */
public class DataExport {

	private static final String SQL_SELECT_LINES = "SELECT DISTINCT LINE FROM HISTORY ORDER BY LINE";
	private static final String FILENAME = "export.csv";
	private static final String DELIMITER = ";";
	Connection connection;
	
	public static void main(String[] args) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
		new DataExport().start();
	}

	public void start() throws SQLException, FileNotFoundException, UnsupportedEncodingException {
		connection = DriverManager
				.getConnection(DatabasePersister.DB_CONNECTION_STRING);
		DatabasePersister.ensureDatabaseCreated();

		List<String> lines = fetchLines();
		writeData(lines);
		connection.close();
		}

	protected void writeData(List<String> lines) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(FILENAME, "UTF-8");
		
		String queryStatement = createQueryStatementToLoadData(lines);
		try (	Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(queryStatement);) {
			
			// Write header row
			writer.print("\"Time\"");
			for (String line : lines) {
				writer.print(DELIMITER + "\"" + line + "\"");
			}
			writer.println(DELIMITER);
			
			// Write content
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			
			while (resultSet.next()) {
				writer.print("\"");
				writer.print(resultSet.getString(1));
				writer.print("\"");
				
				for(int i = 2; i <= columnsNumber; i++) {
					writer.print(DELIMITER);
					writer.print("\"");
					writer.print(resultSet.getString(i));
					writer.print("\"");
				}
				
				writer.println(DELIMITER);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		writer.close();
	}

	protected String createQueryStatementToLoadData(List<String> lines) {
		StringBuilder statement = new StringBuilder();
		statement.append("SELECT X.TIME, ");

		statement.append(lines.stream()
				.map(s -> "MAX(" + s + "_PERCENT" + ") AS " + s + "_PERCENT")
				.collect(Collectors.joining(", ")));

		statement.append(" FROM (");

		for (int index = 0; index < lines.size(); index++) {
			String currentLineName = lines.get(index);

			statement.append("SELECT TIME");

			for (int index2 = 0; index2 < lines.size(); index2++) {
				statement.append(", ");

				if (index == index2) {
					statement.append("PERCENT");
				} else {
					statement.append("0");
				}

				statement.append(" AS ");
				statement.append(lines.get(index2));
				statement.append("_PERCENT");
			}

			statement.append(" FROM HISTORY WHERE LINE = '");
			statement.append(currentLineName);
			statement.append("'");

			if (index < (lines.size() - 1)) {
				statement.append(" UNION ALL ");
			}
		}

		statement.append(") X ");
		statement.append("GROUP BY X.TIME");

		return statement.toString().replace("  ", " ");
	}

	protected List<String> fetchLines() throws SQLException {
		List<String> lines = new ArrayList<>();

		try (ResultSet result = executeQuery(SQL_SELECT_LINES)) {
			while(result.next()) {
				lines.add(result.getString("LINE"));
			}
		}
		
		return lines;
	}

	protected ResultSet executeQuery(String sqlQuery) throws SQLException {
		Statement statement = connection.createStatement();
		return statement.executeQuery(sqlQuery);
	}
}
