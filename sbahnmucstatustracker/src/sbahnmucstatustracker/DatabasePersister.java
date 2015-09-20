package sbahnmucstatustracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabasePersister {

	public static final String DB_CONNECTION_STRING = "jdbc:sqlite:database.db";
	private final static String HISTORY_TABLE = "HISTORY";
	private static final String CREATE_STATEMENT_PATH = "sql/create_statement.sql";
	private static final String INSERT_STATEMENT_PATH = "sql/insert_statement.sql";

	/*
	 * CREATION
	 */

	public static void ensureDatabaseCreated() {

		try (Connection connection = DriverManager
				.getConnection(DB_CONNECTION_STRING);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM "
						+ HISTORY_TABLE);) {
		} catch (Exception e) {
			createDatabase();
		}
	}

	private static void createDatabase() {
		try (Connection connection = DriverManager
				.getConnection(DB_CONNECTION_STRING);
				Statement statement = connection.createStatement();) {
			String createStatement = getFileContent(CREATE_STATEMENT_PATH);

			if (createStatement != null) {
				statement.execute(createStatement);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private static String getFileContent(String filename) {
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

	/*
	 * PERSISTING
	 */
	public static void insertStatus(SBahnStatus status) throws ClassNotFoundException, SQLException {
		String insertStatement = getFileContent(INSERT_STATEMENT_PATH);

		Connection connection = DriverManager.getConnection(DB_CONNECTION_STRING);
		PreparedStatement statement = connection.prepareStatement(insertStatement);
		statement.setString(1, status.getLine());
		statement.setString(2, SBahnStatus.getTimestampAsString(status.getTimestamp()));
		statement.setInt(3, status.getPercent());
		statement.execute();
	}
}
