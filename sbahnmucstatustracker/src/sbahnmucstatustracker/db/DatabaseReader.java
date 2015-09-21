package sbahnmucstatustracker.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseReader {

	private static final String SQL_SELECT_LINES = "SELECT DISTINCT LINE FROM HISTORY ORDER BY LINE";

	public static List<String> fetchLines() throws SQLException {
		List<String> lines = new ArrayList<>();

		try (Statement statement = Database.GET().createStatement();
				ResultSet result = statement.executeQuery(SQL_SELECT_LINES)) {
			while (result.next()) {
				lines.add(result.getString("LINE"));
			}
		}

		return lines;
	}

	public static List<Object[]> fetchLineData(List<String> lines)
			throws SQLException {
		List<Object[]> result = new ArrayList<>();

		String queryStatement = createQueryStatementToLoadData(lines);
		try (Statement statement = Database.GET().createStatement();
				ResultSet resultSet = statement.executeQuery(queryStatement);) {

			final int columnCount = resultSet.getMetaData().getColumnCount();

			while (resultSet.next()) {
				Object[] currentRowData = new Object[columnCount];

				for (int dbColIndex = 1; dbColIndex <= columnCount; dbColIndex++) {
					int arrayIndex = dbColIndex - 1;
					currentRowData[arrayIndex] = resultSet
							.getObject(dbColIndex);
				}

				result.add(currentRowData);
			}
		}

		return result;
	}

	// TODO Rainer: vielleicht noch Binds verwenden, damit die Bahn nicht die DB
	// löschen kann, indem sie eine S-Bahnlinie komisch benennt ;).
	// Eigentlich fängt der Regex das ab, aber sicher ist sicher.
	protected static String createQueryStatementToLoadData(List<String> lines) {
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
}
