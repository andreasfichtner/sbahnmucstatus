package sbahnmucstatustracker;

import java.io.IOException;
import java.util.LinkedList;

public class SBahnMucStatusTracker {

	static boolean stopped = false;
	
	public static void main(String[] args) throws IOException {
		while(!stopped) {
			LinkedList<SBahnStatus> list = new LinkedList<>();
			try { 
				String website = DataReceiver.getStatusInfoFromInternet();
				list.addAll(DataParser.parse(website));
			}
			catch(IOException ioe) {
				System.err.println("Error occured at " + System.currentTimeMillis() + ":" + ioe.getMessage());
			}
			
			stopped = true;
		}
	}
}
