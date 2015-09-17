package sbahnmucstatustracker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

public class SBahnMucStatusTracker {

	// 1 Minute interval
	private static final long INTERVAL = 60000; 
	static boolean stopped = false;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		SBahnMucStatusTracker tracker = new SBahnMucStatusTracker();
		tracker.work();
	}
	
	public void work() {
		
		// create db if it does not exist
		DatabasePersister.ensureDatabaseCreated();
		
		while(!stopped) {
			LinkedList<SBahnStatus> list = new LinkedList<>();
			try { 
				String website = DataReceiver.getStatusInfoFromInternet();
				list.addAll(DataParser.parse(website));
			}
			catch(IOException ioe) {
				System.err.println("Error occured at " + System.currentTimeMillis() + ":" + ioe.getMessage());
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
	}

	private void fail(Exception e) {
		System.err.println(e.getLocalizedMessage());
		System.exit(1);
	}
}
