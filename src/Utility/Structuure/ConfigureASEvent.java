package Utility.Structuure;

/*
 * 
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;

import java.nio.charset.Charset;

import java.util.HashMap;

public class ConfigureASEvent {
	public ConfigureASEvent() {
		super();
	}

	static public HashMap<String, String> configurietion = new HashMap<String, String>();

	public static HashMap<String, String> getConfigure() {
		return configurietion;
	}

	public static void readConfigureFromFile(String fileName) {
		// String fileName="Configure.txt";
		InputStream fis;
		BufferedReader br;
		String line;

		try {
			fis = new FileInputStream(fileName);
			br = new BufferedReader(new InputStreamReader(fis,
					Charset.forName("UTF-8")));

			while ((line = br.readLine()) != null) {
				if (line.startsWith("#"))
					continue;
				if (line.length() == 0)
					continue;
				String[] pair = line.split("\\t");
				configurietion.put(pair[0], pair[1]);

			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public static void main(String[] args) {
		readConfigureFromFile("F:\\AS-pipeline\\ASproject\\Configure.txt");
		configurietion.get("UseVSL2Server");
	}

}
