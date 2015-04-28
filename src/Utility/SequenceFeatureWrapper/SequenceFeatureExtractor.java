package Utility.SequenceFeatureWrapper;

import java.util.ArrayList;

import ASEvent.GTFFeature.GTFFeature;
import Utility.Structuure.ConfigureASEvent;
import Utility.Structuure.Tris;

public class SequenceFeatureExtractor {
	/*
	 * fai index
	 */
	public static boolean faisInit = false;
	public static GetSequenceUsingFasta fais;

	/*
	 * gtf index
	 */
	public static boolean gtfInit = false;
	public static GetFeatureGTF gtf;

	public static void init() {
		if (!gtfInit) {
			gtf = new GetFeatureGTF();
			gtf.createIndex("");
			gtfInit = true;
		}
		if (!faisInit) {
			fais = new GetSequenceUsingFasta();
			fais.generateFaiGenome();
			faisInit = true;
		}
	}

	public static String getSequence(String org, String chrosome, int begPos,
			int endPos) {
		init();

		if (ConfigureASEvent.configurietion.get("FetchSeqOnline").equals(
				"FALSE")) {

			return fais.getSequence(chrosome, begPos, endPos);

		} else {
			return GetSequenceUsingREST.getSequenceEnsembl(chrosome, begPos,
					endPos);

		}

	}

	public static String getGeneName(String chromsome, int begPos, int endPos) {
		init();
		String id = gtf.geneName("chr" + chromsome, begPos, endPos);

		return id;
	}

	public static ArrayList<String> getCDSInformation(String org,
			String chrosome, int begPos, int endPos) {
		init();

		return gtf.getCDSInformation(chrosome, begPos, endPos);

	}

	public static ArrayList<String> getTranscriptIDs(String chr, int beg,
			int end) {
		// TODO Auto-generated method stub
		init();

		return gtf.getTranscriptIDs("chr" + chr, beg, end);

	}

	public static ArrayList<GTFFeature> getTranscriptsOfATranscript(
			String transcriptID) {
		init();
		Tris<String, Integer, Integer> geneRegion = DatabaseUCSC
				.getTranscriptRegion(transcriptID);
		System.out.println(geneRegion.getValue1()+"\t"+geneRegion.getValue2()+"\t"+geneRegion.getValue3());
		// geneRegion.setValue1("chr"+geneRegion.getValue1());
		return gtf.getGTFFeaturesCDS(geneRegion.getValue1(),
				geneRegion.getValue2(), geneRegion.getValue3());

		// String gffContent=RESTEnsembl.getGFF(geneEnsemblID);
		// ArrayList<GTFFeature>
		// gtfFeatures=GetFeatureGTF.getGTFFeatures(gffContent);
		// return gtfFeatures;

	}

	public static ArrayList<Tris<String,Integer,Integer>> getASRegions(String chr,int begPos,int endPos){
		return DatabaseUCSC.getASRegion(chr,begPos,endPos);
		
	}
	
	public static ArrayList<Float> getConservationScore(String chr,int begPos,int endPos){
		return GetConversation.getConservationScore(chr, begPos, endPos);
		
	}
	
	public static void main(String[] args) {
		String configureFileName = "/home/limeng/alternative/Configure.txt";

		ConfigureASEvent.readConfigureFromFile(configureFileName);

		ArrayList<GTFFeature> gtfFeatures = SequenceFeatureExtractor
				.getTranscriptsOfATranscript("ENST00000392322");
		for (int i = 0; i < gtfFeatures.size(); ++i) {
			GTFFeature fea = gtfFeatures.get(i);

			System.out.println(fea.getChr());
			System.out.println(fea.getStart());
			System.out.println(fea.getEnd());
			System.out.println(fea.getAttribute());
			// System.out.println(fea.getChr());

		}

	}

}
