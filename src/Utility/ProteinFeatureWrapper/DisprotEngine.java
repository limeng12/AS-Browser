package Utility.ProteinFeatureWrapper;

/*
 * it have two methods,one is search using web,but it can max 100 proteins in one day.
 * another method is search using local software.
 */

import Utility.ASDebug;
import Utility.Algorithm.ASRandom;
import Utility.Structuure.ConfigureASEvent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DisprotEngine {
	public DisprotEngine() {
		super();
		VSL2Broot = ConfigureASEvent.configurietion.get("VSL2Path");
		VSL2BName = ConfigureASEvent.configurietion.get("VSL2Name");
	}

	/*
	 * run using disprot local
	 */
	static String VSL2Broot = "";
	static String VSL2BName = "";

	static public String getDisProbabilityLocal(String seq,
			ArrayList<Double> disProbability) {
		VSL2Broot = ConfigureASEvent.configurietion.get("VSL2Path");
		VSL2BName = ConfigureASEvent.configurietion.get("VSL2Name");
		String javaPath = ConfigureASEvent.configurietion.get("java");

		int index = seq.indexOf("*");

		if (index != -1)
			seq = seq.substring(0, index);

		String result = "";

		String path = VSL2Broot + ASRandom.randomCharGenerator(50);
		String faPath = path + ".flat";
		try {
			FileWriter fw = new FileWriter(faPath, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(seq);

			pw.close();
			fw.close();
			String command = javaPath + "java -jar " + VSL2Broot + VSL2BName
					+ " -s:" + faPath;
			ASDebug.output(command);

			Runtime run = Runtime.getRuntime();
			Process p = run.exec(command);

			Thread.sleep(1000 * 1);
			int times = 0; // becareful here
			while (p.getInputStream().available() <= 0) {
				Thread.sleep(1000 * 2);
				times++;
				if (times > 10000)
					break;
			}

			BufferedInputStream in = new BufferedInputStream(p.getInputStream());

			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

			String lineStr;
			String begStr = "--------------------";
			while ((lineStr = inBr.readLine()) != null) {
				if (lineStr.contains(begStr.substring(0)))
					break;
			}
			// lineStr = inBr.readLine();
			String endStr = "==============";

			while ((lineStr = inBr.readLine()) != null) {
				if (lineStr.contains(endStr.subSequence(0, endStr.length())))
					break;
				lineStr = lineStr.trim();
				result += lineStr.charAt(lineStr.length() - 1);
				String[] lineArray = lineStr.split("\\s");
				disProbability.add(Double
						.parseDouble(lineArray[lineArray.length - 2]));
			}
			inBr.close();
			in.close();

			p.destroy();

			File file = new File(faPath);
			file.deleteOnExit();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		return result;

	}

	public static void main(String args[]) throws IOException {

		String seq = "MMASTENNEKDNFMRDTASRSKKSRRRSLWIAAGAVPTAIALSLSLASPA"
				+ "AVAQSSFGSSDIIDSGVLDSITRGLTDYLTPRDEALPAGEVTYPAIEGLP"
				+ "AGVRVNSAEYVTSHHVVLSIQSAAMPERPIKVQLLLPRDWYSSPDRDFPE"
				+ "IWALDGLRAIEKQSGWTIETNIEQFFADKNAIVVLPVGGESSFYTDWNEP"
				+ "NNGKNYQWETFLTEELAPILDKGFRSNGERAITGISMGGTAAVNIATHNP"
				+ "EMFNFVGSFSGYLDTTSNGMPAAIGAALADAGGYNVNAMWGPAGSERWLE"
				+ "NDPKRNVDQLRGKQVYVSAGSGADDYGQDGSVATGPANAAGVGLELISRM"
				+ "TSQTFVDAANGAGVNVIANFRPSGVHAWPYWQFEMTQAWPYMADSLGMSR"
				+ "EDRGADCVALGAIADATADGSLGSCLNNEYLVANGVGRAQDFTNGRAYWS"
				+ "PNTGAFGLFGRINARYSELGGPDSWLGFPKTRELSTPDGRGRYVHFENGS"
				+ "IYWSAATGPWEIPGDMFTAWGTQGYEAGGLGYPVGPAKDFNGGLAQEFQG"
				+ "GYVLRTPQNRAYWVRGAISAKYMEPGVATTLGFPTGNERLIPGGAFQEFT"
				+ "NGNIYWSASTGAHYILRGGIFDAWGAKGYEQGEYGWPTTDQTSIAAGGET"
				+ "ITFQNGTIRQVNGRIEESR";
		ArrayList<Double> dis = new ArrayList<Double>();
		System.out.println(DisprotEngine.getDisProbabilityLocal(seq, dis));
		// for(Double i:dis){
		// System.out.println(i);
		// }
	}

}
