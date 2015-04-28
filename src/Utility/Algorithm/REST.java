package Utility.Algorithm;

/*
 * REST service.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

import Utility.ASDebug;

public class REST {

	public static void main(String[] args) {

		String serverString = "http://genome.ucsc.edu/cgi-bin/das/";
		String qstr = serverString + "ce6/dna?segment=chrII:1,100000";

		try {

			String s = REST.getResultLoop(qstr);

			ASDebug.output(s);

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static String getResultOnce(String qstr) throws IOException,
			InterruptedException {
		String urlStr = qstr;
		String result = "";

		URL url;

		url = new URL(urlStr);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(1000000);
		// conn.
		int i = 0;
		while (conn.getResponseCode() != 200) {

			Thread.sleep(1000);

			// ASDebug.output(conn.getResponseCode());
			// ASDebug.output(conn.getResponseMessage());
			conn.disconnect();
			conn = (HttpURLConnection) url.openConnection();

			// ASDebug.output(i);
			if (i > 500)
				break;
			i++;
		}

		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line + "\n");
			// ASDebug.output(line);
		}
		rd.close();

		conn.disconnect();
		result = sb.toString();

		return result;
	}

	public static String getResultLoop(String qstr) {
		String result = "";

		boolean runSuccessfull = false;
		while (!runSuccessfull) {

			try {
				runSuccessfull = true;

				result = getResultOnce(qstr);

			} catch (Exception e) {
				System.out.println(e.getMessage());
				runSuccessfull = false;

			}
		}
		return result;
	}
}
