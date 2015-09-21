package sbahnmucstatustracker.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import sbahnmucstatustracker.SBahnStatus;
import sbahnmucstatustracker.io.IOUtility;

public class DatabasePersister {

	private static final String INSERT_STATEMENT_PATH = "sql/insert_statement.sql";

	/*
	 * PERSISTING
	 */
	public static void insertStatus(SBahnStatus status)
			throws ClassNotFoundException, SQLException {
		String insertStatement = IOUtility
				.getFileContent(INSERT_STATEMENT_PATH);

		try (PreparedStatement statement = Database.GET().getConnection()
				.prepareStatement(insertStatement)) {
			statement.setString(1, status.getLine());
			statement.setString(2,
					SBahnStatus.getTimestampAsString(status.getTimestamp()));
			statement.setInt(3, status.getPercent());
			statement.execute();
		}
	}
}
