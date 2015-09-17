package sbahnmucstatustracker;

import java.util.Calendar;

public class SBahnStatus {
	int percent;
	String line;
	Calendar timestamp;
	
	public SBahnStatus(String line, long time, int percent) {
		this.line = line;
		this.timestamp = Calendar.getInstance();
		this.timestamp.setTimeInMillis(time);
		this.percent = percent;
	}
	
	public String shortInfo() {
		return String.format("SBahnStatus: Line %s: %d", line, percent);
	}
}
