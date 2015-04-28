package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import Utility.Algorithm.ASRandom;
import Utility.Algorithm.Translate;
import Utility.Structuure.ConfigureASEvent;

public class Test2014_6_9 {

	public static void main(String[] args) {
		try {
			String configureFileName = "/home/limeng/alternative/Configure.txt";

			ConfigureASEvent.readConfigureFromFile(configureFileName);

			PrintWriter pw = new PrintWriter(new FileWriter(
					"refseq.hg19.genomic.fa.result"));

			String fileName = "/home/limeng/Downloads/refseq.hg19.genomic.fa";

			File file = new File(fileName);
			BufferedReader input;
			input = new BufferedReader(new FileReader(file));

			String text;
			String seq = "";

			text = input.readLine();
			int lineNum = 0;

			while ((text = input.readLine()) != null) {
				// lineNum++;

				// System.out.println(lineNum);
				// if(lineNum>1000)
				// break;
				if (lineNum++ % 100 == 0) {
					System.out.println(lineNum);

				}

				if (text.startsWith(">")) {
					String protein = Translate.translate(seq);
					seq = "";

					String newName = "";

					String[] arrLine = text.split(" ");
					for (int i = 0; i < arrLine.length; ++i) {
						newName += arrLine[i];
						if (i == 0) {
							newName += "_" + ASRandom.randomCharGenerator(20)
									+ " ";
						} else {
							newName += " ";

						}

					}

					// System.out.println(protein);
					pw.println(newName);
					pw.println(protein);

					// ArrayList<String>
					// pfams=PfamEngine.getPfamLocally(protein);
					// pw.println(text);
					// for(String ite:pfams){
					// pw.println(ite);
					// }
					// pw.flush();

					// continue;
				}

				// seq+=text.substring(0, text.length());
				else {
					seq += text;
				}
			}
			pw.close();
			input.close();

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
