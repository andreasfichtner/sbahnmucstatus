package sbahnmucstatustracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataReceiver {

	private static String statusUrl = "http://s-bahn-muenchen.hafas.de/bin/540/query.exe/dn?statusWidget";
	
	public static String getStatusInfoFromInternet() throws IOException {
		  StringBuilder result = new StringBuilder();
	      URL url = new URL(statusUrl);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         result.append(line);
	      }
	      rd.close();
	      return result.toString();
	   }
}
