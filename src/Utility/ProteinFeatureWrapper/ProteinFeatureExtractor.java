package Utility.ProteinFeatureWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import GeneStructure.Strand;
import Utility.Algorithm.ProteinAlign;
import Utility.Algorithm.RESTPDB;
import Utility.SequenceFeatureWrapper.SequenceFeatureExtractor;
import Utility.Structuure.ConfigureASEvent;
import Utility.Structuure.Pair;
import Utility.Structuure.Quar;
import Utility.Structuure.Tris;

public class ProteinFeatureExtractor {

	public static String getDisProbability(String proteinSequence,
			Strand strand, ArrayList<Double> dis) {
		String disStr = DisprotEngine.getDisProbabilityLocal(proteinSequence,
				dis);

		if (strand.equals(Strand.NEGATIVE)) {
			ArrayList<Double> reverseDis = new ArrayList<Double>();
			for (int i = dis.size() - 1; i >= 0; --i) {
				reverseDis.add(dis.get(i));

			}

			dis = reverseDis;

			String reverseDisStr = "";
			for (int i = disStr.length() - 1; i > 0; --i) {
				reverseDisStr += disStr.charAt(i);

			}

			disStr = reverseDisStr;

		}

		return disStr;
	}

	// get pfam information

	public static ArrayList<Tris<Integer, Integer, String>> getPfamName(
			String proteinSenquence, Strand strand) {
		int len = proteinSenquence.length();
		ArrayList<Tris<Integer, Integer, String>> pfamValues = new ArrayList<Tris<Integer, Integer, String>>();
		ArrayList<String> pfams = PfamEngine.getProteinXML(proteinSenquence);
		for (String ite : pfams) {
			String[] lpfam = ite.split("\\t");

			if (strand.equals(Strand.POSITIVE))
				pfamValues.add(new Tris<Integer, Integer, String>(Integer
						.parseInt(lpfam[0]), Integer.parseInt(lpfam[1]),
						lpfam[2]));
			else
				pfamValues.add(new Tris<Integer, Integer, String>(len
						- Integer.parseInt(lpfam[1]) + 1, len
						- Integer.parseInt(lpfam[0]) + 1, lpfam[2]));

		}
		return pfamValues;
	}

	// get modpred information
	public static ArrayList<String> getPTMs(String proteinSequence) {
		ArrayList<String> t = ModPredEngine.getPTMTable(proteinSequence);
		return t;
		// parseModPredAdjustStrand(t,Strand.Positive,seq.length());
	}

	// get modpred information for plot
	public static HashMap<String, ArrayList<Pair<Integer, Double>>> getPTMsAdjustStrand(
			String proteinSequence, Strand strand) {
		ArrayList<String> t = ModPredEngine.getPTMTable(proteinSequence);

		return ModPredEngine.parseModPredAdjustStrand(t, strand,
				proteinSequence.length());

	}

	public static ArrayList<String> getPDBFiles(String dnaSequence) {
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> ids = RESTPDB.fetchPDBId(dnaSequence);
		ArrayList<String> humanIds = new ArrayList<String>();

		for (String id : ids) {
			String org = RESTPDB.getEntityDescription(id);
			if (org.equals("Homo sapiens"))
				humanIds.add(id);
		}

		files = RESTPDB.fetchPDBFiles(humanIds);

		// files=RESTPDB.fetchPDBFiles(dnaSequence);

		return files;

	}

	@SuppressWarnings("resource")
	public static ArrayList<String> getPDBFilesUsingGeneName(String geneName) {
		ArrayList<String> files = new ArrayList<String>();
		InputStream fis;
		BufferedReader br;
		String line;

		String pdbFigurePath = ConfigureASEvent.getConfigure().get(
				"pdbFilePath")
				+ geneName + "/";

		File file = new File(pdbFigurePath);
		String[] pathList = file.list();
		String filePath = "";
		try {

			for (int i = 0; i < pathList.length; ++i) {
				pathList[i] = pdbFigurePath + pathList[i];

				filePath = pathList[i];

				fis = new FileInputStream(filePath);

				br = new BufferedReader(new InputStreamReader(fis,
						Charset.forName("UTF-8")));

				String oneFile = "";
				while ((line = br.readLine()) != null) {
					oneFile += line + "\n";

				}

				files.add(oneFile);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return files;

	}

	public static ArrayList<String> getPDBProteins(ArrayList<String> files) {
		ArrayList<String> pdbProteins = new ArrayList<String>();

		for (int i = 0; i < files.size(); ++i) {
			String pdbProtein = RESTPDB.fecthPDBSeq(files.get(i));

			pdbProteins.add(pdbProtein);
		}

		return pdbProteins;

	}

	public static ArrayList<Pair<Integer, String>> getPDBFilesAnsSnpPostion(
			String dnaSequence, String proteinSequence, int tsnpPos) {
		ArrayList<Pair<Integer, String>> pdbPosFile = new ArrayList<Pair<Integer, String>>();

		ArrayList<String> files = new ArrayList<String>();
		ArrayList<Integer> snpPositions = new ArrayList<Integer>();

		files = RESTPDB.fetchPDBFiles(dnaSequence);

		ArrayList<String> pdbProteins = new ArrayList<String>();

		for (int i = 0; i < files.size(); ++i) {
			String pdbProtein = pdbProteins.get(i);

			pdbProteins.add(pdbProtein);
			int snpPos = ProteinAlign.getSNPPosition(proteinSequence,
					pdbProtein, tsnpPos);
			snpPositions.add(snpPos);
			pdbPosFile.add(new Pair<Integer, String>(snpPos, files.get(i)));

		}

		return pdbPosFile;
	}

	public static ArrayList<Pair<Integer, String>> getPTMUsingGeneName(
			String chr, int beg, int end, String asProteinSequence) {
		ArrayList<Pair<Integer, String>> uniPTMs = new ArrayList<Pair<Integer, String>>();

		// Quar<String, String, Integer, Integer> en = RESTEnsembl.getGeneName(
		// chr, beg, end);
		// ensemble gene id
		// System.out.println("ids");
		ArrayList<String> transcriptIDs = SequenceFeatureExtractor
				.getTranscriptIDs(chr, beg, end);

		if (transcriptIDs.size() == 0) {
			// System.out.println("number of ids="+transcriptIDs.size());
			return uniPTMs;
		}
		// String ensemblGeneID = transcriptIDs;
		// for(int i=0;i<transcriptIDs.size();++i)
		// System.out.println("ucsc gene id="+transcriptIDs.get(i));

		HashSet<String> ids = new HashSet<String>();

		for (int i = 0; i < transcriptIDs.size(); ++i) {

			String ensemblGeneID = transcriptIDs.get(i);

			ArrayList<String> tIds = RESTUniprot
					.convertEnsembleTransIDToUniprotIDs(ensemblGeneID);

			for (int j = 0; j < tIds.size(); ++j)
				ids.add(tIds.get(j));

		}
		// for(String id:ids)
		// System.out.println("id="+id);

		int i = 0;

		for (String id : ids) {
			ArrayList<Pair<Integer, String>> ptms = RESTUniprot
					.getPTMsUingUniprot(id);
			if (ptms.size() != 0) {
				i++;
				String sequence = RESTUniprot.getSequenceUniprot(id);
				for (Pair<Integer, String> a : ptms) {
					int p = a.getValue1();
					int newPos = ProteinAlign.getSNPPosition(sequence,
							asProteinSequence, p);
					if (newPos != -1) {
						uniPTMs.add(new Pair<Integer, String>(newPos, a
								.getValue2()));
					}

				}

			}

		}
		System.out.println("number of gene=" + i);
		return uniPTMs;
	}

	public static void main(String[] args) {
		String s = "MEEPQSDPSVEPPLSQETFSDLWKLLPENNVLSPLPSQAMDDLMLSPDDIEQWFTEDPGPDEAPRMPEAA"
				+ "PPVAPAPAAPTPAAPAPAPSWPLSSSVPSQKTYQGSYGFRLGFLHSGTAKSVTCTYSPALNKMFCQLAKT"
				+ "CPVQLWVDSTPPPGTRVRAMAIYKQSQHMTEVVRRCPHHERCSDSDGLAPPQHLIRVEGNLRVEYLDDRN"
				+ "TFRHSVVVPYEPPEVGSDCTTIHYNYMCNSSCMGGMNRRPILTIITLEDSSGNLLGRNSFEVRVCACPGR"
				+ "DRRTEEENLRKKGEPHHELPPGSTKRALPNNTSSSPQPKKKPLDGEYFTLQIRGRERFEMFRELNEALEL"
				+ "KDAQAGKEPGGSRAHSSHLKSKKGQSTSRHKKLMFKTEGPDSD";

		ArrayList<Pair<Integer, String>> t = getPTMUsingGeneName("17", 7565097,
				7590856, s);

		for (Pair<Integer, String> a : t) {
			System.out.println(a.getValue1() + "\t" + a.getValue2());

		}

	}

	public static ArrayList<String> getPfam(String proteinSenquence) {
		// TODO Auto-generated method stub
		// int len=proteinSenquence.length();
		// ArrayList<Tris<Integer,Integer,String>> pfamValues=new
		// ArrayList<Tris<Integer,Integer,String>>();

		ArrayList<String> pfams = PfamEngine.getProteinXML(proteinSenquence);

		return pfams;
	}

	// start position&end position&protein family&description
	public static ArrayList<Quar<Integer, Integer, String, String>> getPfamQuar(
			String proteinSenquence, Strand strand) {
		// int len=proteinSenquence.length();
		ArrayList<Quar<Integer, Integer, String, String>> pfamValues = new ArrayList<Quar<Integer, Integer, String, String>>();
		ArrayList<String> pfams = PfamEngine.getPfamLocally(proteinSenquence);
		System.out.println("get pfam end");
		for (String ite : pfams) {
			// System.out.println(ite);
			String[] lpfam = ite.split("&");
			pfamValues.add(new Quar<Integer, Integer, String, String>(Integer
					.parseInt(lpfam[0]), Integer.parseInt(lpfam[1]), lpfam[2],
					lpfam[3]));

			// pfamValues.add(new
			// Quar<Integer,Integer,String,String>(len-Integer.parseInt(lpfam[1])+1,len-Integer.parseInt(lpfam[0])+1
			// ,lpfam[2],lpfam[3]) );

		}
		return pfamValues;
	}

}
