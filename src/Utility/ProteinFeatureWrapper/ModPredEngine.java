package Utility.ProteinFeatureWrapper;

/*
 * this function doesn't support multithread
 * be carefull.
 */

import GeneStructure.Strand;
import Utility.ASDebug;
import Utility.Algorithm.ASRandom;
import Utility.Structuure.ConfigureASEvent;
import Utility.Structuure.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class ModPredEngine {
	public ModPredEngine() {
		super();
		// modPredRoot = ConfigureASEvent.configurietion.get("ModPredPath");
		// modPredName = ConfigureASEvent.configurietion.get("ModPredPathName");
		// mcrPath = ConfigureASEvent.configurietion.get("mcr");

	}

	static String modPredRoot = "";
	static String modPredName = "";
	static String mcrPath = "";

	static public ArrayList<String> getPTMTable(String seq) {
		modPredRoot = ConfigureASEvent.configurietion.get("ModPredPath");
		modPredName = ConfigureASEvent.configurietion.get("ModPredPathName");
		mcrPath = ConfigureASEvent.configurietion.get("mcr");

		// modPredRoot = "F:\\AS-pipeline\\code\\ModPred_win64\\";
		// modPredName = "ModPred_win64.exe";
		int index = seq.indexOf("*");
		if (index != -1)
			seq = seq.substring(0, index);

		ArrayList<String> result = new ArrayList<String>();

		String path = modPredRoot + ASRandom.randomCharGenerator(50);
		String faPath = path + ".fasta";
		// File fa = new File(faPath);
		// /fa.deleteOnExit();

		try {
			FileWriter fw = new FileWriter(faPath, true);
			PrintWriter pw = new PrintWriter(fw);

			pw.println(">test");
			pw.println(seq);

			pw.close();
			// bw.close();
			fw.close();
			// String command=modPredRoot+"ModPred_win64.exe -i "+path;
			File f = new File(path + ".txt");
			// f.deleteOnExit();

			String command = modPredRoot + modPredName + " " + mcrPath + " "
					+ " -i " + faPath + " -o " + path + ".txt";
			ASDebug.output(command);
			Runtime run = Runtime.getRuntime();
			Process p = run.exec(command);
			// p.wait(2);
			Thread.sleep(1000 * 2);
			// p.getInputStream().
			// ASDebug.output("prepare input");
			while (!f.exists()) {
				Thread.sleep(1000 * 2);

			}

			FileInputStream fis = new FileInputStream(path + ".txt");
			int times = 0;
			while (fis.available() <= 10) {
				Thread.sleep(1000 * 2);
				times++;
				if (times > 30000000) {

					break;

				}
			}

			// while(System.in.available()<100){
			// Thread.sleep(1000 * 2);

			// }
			// System.setOut(System.out);

			// while (p.getInputStream().available() <= 0) {
			// System.setOut(System.out);

			// Thread.sleep(1000 * 2);
			// }
			// ASDebug.output("can input");

			BufferedInputStream in = new BufferedInputStream(fis);

			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			// int i = 0;

			String lineStr;
			lineStr = inBr.readLine(); // skip first line
			while ((lineStr = inBr.readLine()) != null) {
				if (lineStr.contains("Not modified"))
					continue;

				result.add(lineStr);

				// String[] dataLine=lineStr.split("\\t");
				// ASDebug.output(i++);
			}
			inBr.close();
			in.close();
			// Runtime.getRuntime().exec(command);

			// ASDebug.output(command);

			p.destroy();
			File file = new File(faPath);
			file.deleteOnExit();
			f.deleteOnExit();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ASDebug.output(e.getMessage());
		} catch (InterruptedException e) {
			ASDebug.output(e.getMessage());
		}

		return result;
	}

	static public HashMap<String, ArrayList<Pair<Integer, Double>>> parseModPredAdjustStrand(
			ArrayList<String> ptmStrings, Strand strand, int proteinLen) {
		HashMap<String, ArrayList<Pair<Integer, Double>>> ptms = new HashMap<String, ArrayList<Pair<Integer, Double>>>();

		for (String index : ptmStrings) {
			String[] arrayLine = index.split("\\t");
			if (arrayLine[arrayLine.length - 1].equals("Not modified")
					|| (arrayLine.length < 6))
				continue;

			String posStr = arrayLine[0].substring(1, arrayLine[0].length());
			int pos = Integer.parseInt(posStr);
			if (strand.equals(Strand.NEGATIVE))
				pos = proteinLen - pos;

			double score = Double.parseDouble(arrayLine[4]);

			Pair<Integer, Double> posScore = new Pair<Integer, Double>(
					new Integer(pos), new Double(score));

			// ptms.put(arrayLine[1], posScore);
			String name = arrayLine[1];
			name = name.replace("-", "_");

			if (ptms.containsKey(name)) {
				ptms.get(name).add(posScore);
			} else {
				ArrayList<Pair<Integer, Double>> tmp = new ArrayList<Pair<Integer, Double>>();
				tmp.add(posScore);
				ptms.put(name, tmp);
			}

			// ptms.put(arrayLine[1],)

		}

		return ptms;
	}

	public static void main(String args[]) throws IOException {

		String seq = "YCEKEKASSKDLRHTHGKGEPSRPARRLSESLHSADENKTESKVEREYKRRTSTPVILEGAQEETDTRDGKKQPERSETNVEETQKQKSTLKNEKYQKKDDPETHGKGLPKKEAKSAKERPEKEKAQSEDKPSSKHKHKGDSVHKMSDETELHSSEKGETEESVRKQGQQTKLSSDDRTERKSKHKSERRLSVLGRDGKPVSEYTIKTDEHARKDNKKEKHLSSEKSKAEHKSRRSSDSKLQKDALSSKQHSVTSQKRSESCSEDKCETDSTNADSSFKPEELPHKERRRTKSLLEDKVVSKSKSKGQSKQTKAAETEAQEGVTRQVTTPKPDKEKNTEDNDTERQRKFKLEDRTSEETVTDPALENTVSSAHSAQKDSGHRAKLASIKEKHKTDKDSTSSKLERKVSDGHRSRSLKHSNKDMKKKEENKPDDKNGKEVDSSHEKGRGNGPVTEKKLSRRLCENRRGSTSQEMAKEDKLVANMSGTTSSSSLQRPKKSTETTSIPEQEPMEIDSEAAVENVSELSKTEDISSNSSQQDTDFENVTKHKATAGVLKDEFRTSMVDSKPAAAVTCKSGRGLAVTSISERHADHKSTLTKKVHSQGNPSKAAPREREPIQRGAQEVSVDSEVSRKALSRAPSENEKGQKNLKGMSKTTEECGTHRNASLEYSTDSDLLSSSGSVTVVPQKESHNSNTIPVIDREAISEGGRASTSLANHSDVPNQYSTVKKSEVHKTNGSKEGNDGFTVDMPTKANGGSKRHLSEDSQATLLYSKESKISIPLADKSMSVTGDNKNINKQRSLMGTAKRESDLKVNPDIKQDSAAGEHVVDLSTRKEAETVRRKHNKEIPTDVERKTENSEVDTSARRDSAPVPQQRHGKMERGAAGSGRRDKAFIATSTEGTDKGIMLNTVKTGDATTTSSEVGEKGTALPCTSIEADEGFMMGACPKKHPLQVGAEASECTVFAAAEEGKGVVTEGFAESEILLTSSKEGESGECAVAESEDRVAGPLAAHTVQAEANVNSITTEEKDDAVTSAGSEEKCGGSACTVEGTATFIGEVESDGAVTSAGTEIRAGSLSSEDVDGSQENRIQVGPKKETEGTVTCTETKGRNDNFICLVTRVETQEQRVVTGADVVQVNAAKPQEANANQGDGSGTDGAEGESAVTSTGITEEDGEASANCTGSEDNREGCAISSETEESAESAMDSTEAKDITNAPLVAAGPCDDEGIVTSTGAKEEDDEDEGVVTSTGRGNEPGHASACTGIEESEGMMVCESGEGGAQIGPTIDHVNAEAGAATVNTNDSNVDSMSGAEKEIKDTNICSSAKGIVESSVTSALAGNSDRPPVLCGSEGPMASASSHHSDSQLTRKETVEDTTISTGLVKGSDDVLVSGEVPECEVGHMSPRKNEECDGLMASTASCDVSNKDSLAGSKSQGNGLMISTSTNACTPQISAVIDVRGGHLSTLSTEEIRDGVRVHREGFEAPMPSAVSGENSQLTASRSEEKDECAMISTSIGEEFELPISSAVTVTCAERQQPVAAVEESTTGPALVSTEDFEVPMPSAPTEAESPLASTSKEEKDECALISTSIAEECEASVFGVSRNAPSVTDGNAVISTSSVEDCEGSVSSAVPQESVCPSVIPVEETGDTAMISTSTSEGREAVMVGTIPTDDDQATTVRGEDLSDAAIISTSTAECVLTCTSLSRHEENQQATHNPEGNGGHLATKQSKCELPMPSLVAERNCKCPGPFRMGKGVGPLMAVGTRGEHDRLPVCEPSVGQGQPGTALCLGEEESHGMDCPGQDLNAKERNTLLSSVQRESKSAEAEAAGDSSTARRTVRKDSERNANSLSETNCLREPEQKPAEDTSGSTHCLTAVNPGAEADGMLPITHAALEYPDHQEPESNLKTTTKCITGQESQMPSSHTGVLSAVCHVAPCASEQEGGLPTKSDHSGTWTSEGSPEKMGHVAGARQSFHREGNLDVTLPPEDNGCGVVLLGNEESPPKGIGGLELSTGLTT";
		// modPredRoot = "F:\\AS-pipeline\\code\\ModPred_win64\\";
		// modPredName = "ModPred_win64.exe";
		String configureFileName = "/home/limeng/alternative/Configure.txt";

		// String configureFileName=args[0];
		// String org=args[1];
		// String fileName=args[2];

		ConfigureASEvent.readConfigureFromFile(configureFileName);

		ArrayList<String> t = ModPredEngine.getPTMTable(seq);
		// parseModPredAdjustStrand(t,Strand.POSITIVE,seq.length());
		for (String index : t) {
			System.out.println(index);
		}

		// System.out.println(ModPredEngine.getPTM(seq));

	}

}
