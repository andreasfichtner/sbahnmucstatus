package sbahnmucstatustracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SBahnStatus {
	int percent;
	String line;
	Calendar timestamp;
	
	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public SBahnStatus(String line, long time, int percent) {
		this.line = line;
		this.timestamp = Calendar.getInstance();
		this.timestamp.setTimeInMillis(time);
		this.percent = percent;
	}
	
	public String shortInfo() {
		return String.format("SBahnStatus: Line %s: %d", line, percent);
	}
	
	public static String getTimestampAsString(Calendar c) {	
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
		return format.format(c.getTime());
	}
}
