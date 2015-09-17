package sbahnmucstatustracker;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {

	public static Collection<? extends SBahnStatus> parse(String website) {
		LinkedList<SBahnStatus> list = new LinkedList<>();
		long timestamp = System.currentTimeMillis();
		
		Pattern p = Pattern.compile("([\\d]+) %<\\/div><\\/td><td class=\"disturbancesAmount\"><div id=\"disturbances_(S[\\d]+)");
		Matcher m = p.matcher(website);
		while (m.find()) {
			list.add(new SBahnStatus(m.group(2), timestamp, Integer.parseInt(m.group(1))));
		}
		
		return list;
	}
	
}
