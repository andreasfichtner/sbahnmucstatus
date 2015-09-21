package sbahnmucstatustracker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import sbahnmucstatustracker.db.Database;
import sbahnmucstatustracker.db.DatabasePersister;

public class SBahnMucStatusTracker {

	// 1 Minute interval
	private static final long INTERVAL = 60000;
	static boolean stopped = false;

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		Class.forName("org.sqlite.JDBC");
		SBahnMucStatusTracker tracker = new SBahnMucStatusTracker();
		tracker.work();
	}

	public void work() throws SQLException {

		while (!stopped) {
			LinkedList<SBahnStatus> list = new LinkedList<>();
			try {
				String website = DataReceiver.getStatusInfoFromInternet();
				list.addAll(DataParser.parse(website));
			} catch (IOException ioe) {
				System.err.println("Error occured at "
						+ System.currentTimeMillis() + ":" + ioe.getMessage());
			}

			for (SBahnStatus status : list) {
				try {
					DatabasePersister.insertStatus(status);
				} catch (ClassNotFoundException e) {
					fail(e);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				fail(e);
			}
		}

		Database.GET().closeConnection();
	}

	private void fail(Exception e) {
		System.err.println(e.getLocalizedMessage());
		System.exit(1);
	}
}
