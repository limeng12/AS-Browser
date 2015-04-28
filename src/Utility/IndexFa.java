package Utility;

import java.io.File;
import java.io.IOException;

import Utility.Structuure.ConfigureASEvent;

public class IndexFa {

	public static void indexAll() {
		//ConfigureASEvent.readConfigureFromFile(fileName);
		// String path=ConfigureASEvent.getConfigure().get("conservationPath");
		String path = "/home/limeng/alternative/hg19fasta/";

		File gtf = new File(path);
		String[] names = gtf.list();
		for (String onePath : names) {

			String oneFileName = path + onePath;
			System.out.println(oneFileName);
			if (oneFileName.endsWith("fa")) {
				Process p;
				try {
					p = Runtime.getRuntime().exec(
							"/usr/bin/samtools faidx " + oneFileName);

					Thread.sleep(2 * 1000);

					//System.out.println(oneFileName);
					p.waitFor();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			// index(oneFileName);
			// System.

		}

	}

	public static void main(String[] args) {
		indexAll();
		
		
	}

}
