package sbahnmucstatustracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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

	public static void main(String[] args) throws SQLException {
		new DataExport().start();
	}

	public void start() throws SQLException {
		DatabasePersister.ensureDatabaseCreated();

		List<String> lines = fetchLines();
		Object loadedData = fetchData(lines);
		storeToCsvFile(null, loadedData);
	}

	protected void storeToCsvFile(String path, Object data) {
		// TODO
	}

	protected Object fetchData(List<String> lines) {
		String queryStatement = createQueryStatementToLoadData(lines);
		// TODO
		return null;
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
			lines.add(result.getString("LINE"));
		}

		return lines;
	}

	// TODO rni: Refactor: close the connection and statement? but not as long
	// as the result set is in use...
	protected ResultSet executeQuery(String sqlQuery) throws SQLException {
		Connection connection = DriverManager
				.getConnection(DatabasePersister.DB_CONNECTION_STRING);
		Statement statement = connection.createStatement();
		return statement.executeQuery(sqlQuery);
	}
}
