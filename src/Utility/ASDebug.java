package Utility;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ASDebug {
	public ASDebug() {
		super();

	}

	public static void close() {
		// pw.close();
	}

	static void init() {
		/*
		 * try { //pw= new PrintWriter(new FileWriter("debug.txt"));
		 * 
		 * //pw= new PrintWriter(new
		 * FileWriter("/home/limeng/software/alternativeEngine/debug.txt")); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * 
		 * e.printStackTrace(); }
		 */
		init = true;
	}

	static boolean init = false;
	static PrintWriter pw;

	public static void output(String e) {
		if (!init)
			init();

		// pw.println(e);
		System.out.println(e);
	}

	public static void output(int e) {
		if (!init)
			init();

		// pw.println(e);

		System.out.println(e);
	}
}
