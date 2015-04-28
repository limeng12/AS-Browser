package Utility.SequenceFeatureWrapper;

import ASEvent.GTFFeature.GTFCode;
import ASEvent.GTFFeature.GTFFeature;
import Utility.Structuure.ConfigureASEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.broad.tribble.AbstractFeatureReader;
import org.broad.tribble.FeatureReader;
import org.broad.tribble.index.Index;
import org.broad.tribble.index.IndexFactory;

public class GetFeatureGTF {

	public GetFeatureGTF() {
		super();
		annoRoot = ConfigureASEvent.getConfigure().get("annoPath");
		// createIndex("mm9");

	}

	String annoRoot = "";
	Index index;
	private FeatureReader<GTFFeature> source;

	// static ComparatorGTF comGTF=new ComparatorGTF();

	private class ComparatorGTFLines implements Comparator<Object> {

		@Override
		public int compare(Object object1, Object object2) {
			String line1 = (String) object1;
			String line2 = (String) object2;
			String[] arrayLine1 = line1.split("\\t");
			String[] arrayLine2 = line2.split("\\t");
			String chr1 = arrayLine1[0];
			String chr2 = arrayLine2[0];
			if (chr1.length() != chr2.length())
				return chr1.length() - chr2.length();

			if (!chr1.equals(chr2))
				for (int i = 0; (i < chr1.length()) && (i < chr2.length()); ++i) {
					if (chr1.charAt(i) != chr2.charAt(i))
						return chr1.charAt(i) - chr2.charAt(i);
				}

			long beg1 = Long.parseLong(arrayLine1[3]);
			long beg2 = Long.parseLong(arrayLine2[3]);
			if (beg1 != beg2)
				return (int) (beg1 - beg2);

			// if (chr1.equals(chr2) && beg1 == beg2)
			return 0;
		}

	}

	private class ComparatorGTF implements Comparator<GTFFeature> {

		@Override
		public int compare(GTFFeature object1, GTFFeature object2) {

			return object1.getStart() - object2.getStart();

			// if (chr1.equals(chr2) && beg1 == beg2)
			// return 0;

		}

	}

	void sortGTF(String inputFileName, String outputFileName) {
		try {

			File in = new File(inputFileName);
			File out = new File(outputFileName);
			BufferedReader input = new BufferedReader(new FileReader(in));

			ArrayList<String> content = new ArrayList<String>();
			String line;
			while ((line = input.readLine()) != null) {
				content.add(line);
			}

			Collections.sort(content, (new ComparatorGTFLines()));

			FileWriter fileWriter = new FileWriter(out);

			for (String ite : content) {
				fileWriter.write(ite + "\n");

			}
			input.close();

			fileWriter.close();

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public void createIndex(String org) {
		File gtf = new File(annoRoot);
		String[] names = gtf.list();
		String fileName = annoRoot + names[0];
		index = IndexFactory.createIntervalIndex(new File(fileName),
				new GTFCode());
		source = (FeatureReader<GTFFeature>) AbstractFeatureReader
				.getFeatureReader(fileName, new GTFCode(), index);

	}

	public ArrayList<String> getCDSInformation(String chr, int beg, int end) {
		ArrayList<String> cdss = new ArrayList<String>();

		try {
			Iterator<GTFFeature> iter = source.query(chr, beg, end);
			while (iter.hasNext()) {
				GTFFeature feature = iter.next();
				String name = feature.getFeature();
				if (name.equals("CDS")) {
					String cds = "";
					cds += feature.getStart() + "\t" + feature.getEnd() + "\t"
							+ feature.getFrame() + "\t";

					String attr = feature.getAttribute();
					String[] attLine = attr.split("\\;");
					for (int i = 0; i < attLine.length; ++i) {
						if (attLine[i].contains("transcript_id"))
							cds += attLine[i];
					}

					cdss.add(cds);
				}

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return cdss;
	}

	public ArrayList<GTFFeature> getGTFFeaturesCDS(String chr, int beg, int end) {
		ArrayList<GTFFeature> features = new ArrayList<GTFFeature>();

		try {
			Iterator<GTFFeature> iter = source.query(chr, beg, end);
			while (iter.hasNext()) {
				GTFFeature f = iter.next();
				String feature = f.getFeature();
				if ("CDS".equalsIgnoreCase(feature)){
					features.add(f);
					//System.out.println(f.getChr());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return features;

	}

	public String geneName(String chr, int beg, int end) {
		// TODO Auto-generated method stub
		String geneName = "";

		try {
			Iterator<GTFFeature> iter = source.query(chr, beg, end);
			while (iter.hasNext()) {
				GTFFeature feature = iter.next();
				// String name=feature.getFeature();

				String attr = feature.getAttribute();
				String[] attLine = attr.split("\\;");
				for (int i = 0; i < attLine.length; ++i) {
					if (attLine[i].contains("gene_id")) {
						// cds+=attLine[i];
						geneName = attLine[i].substring(7);

					}
					// cdss.add(cds);
				}

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// System.out.println("gene name="+geneName);
		geneName = geneName.substring(1, geneName.length() - 1);
		return geneName;
	}

	public ArrayList<String> getTranscriptIDs(String chr, int beg, int end) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ArrayList<String> transcriptIDs = new ArrayList<String>();

		try {
			Iterator<GTFFeature> iter = source.query(chr, beg, end);
			while (iter.hasNext()) {
				GTFFeature feature = iter.next();
				// String name=feature.getFeature();

				String attr = feature.getAttribute();
				String[] attLine = attr.split("\\;");
				for (int i = 0; i < attLine.length; ++i) {
					if (attLine[i].contains("transcript_id")) {
						// cds+=attLine[i];
						String t = attLine[i].substring(15);
						t = t.substring(1, t.length() - 1);

						transcriptIDs.add(t);

					}
					// cdss.add(cds);
				}

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// System.out.println("gene name="+geneName);
		return transcriptIDs;

	}

	public void test(String fileName) {
		// String fileName = "F:\\AS-pipeline\\data\\anno\\sortmm9.bed";
		// LinearIndex t = (LinearIndex)IndexFactory.createLinearIndex(new
		// File(fileName), new GTFCode());
		index = IndexFactory.createIntervalIndex(new File(fileName),
				new GTFCode());

		source = (FeatureReader<GTFFeature>) AbstractFeatureReader
				.getFeatureReader(fileName, new GTFCode(), index);

		System.out.println(index.containsChromosome("1"));

		try {
			Iterator<GTFFeature> iter = source.query("2", 1, 100000);
			System.out.println(iter.next().getFeature());

			// List<Block> x = t.getBlocks("chr2", 1, 10027979);
			// t.getBlocks(arg0, arg1, arg2)
			// System.out.println();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		// LinkedHashSet<String> x=t.getSequenceNames();
		// Iterator itr = x.iterator();
		// while(itr.hasNext()){
		// System.out.println(itr.next());
		// }
	}

	public static void main(String[] args) {
		ConfigureASEvent
				.readConfigureFromFile("/home/limeng/alternative/Configure.txt");

		GetFeatureGTF gtf = new GetFeatureGTF();

		// gtf.sortGTF("/home/limeng/alternative/anno/ensemble-mm9.gtf",
		// "/home/limeng/alternative/anno/ensemble-mm9-sort.gtf");
		String inputFileName="/home/limeng/drugresistent/gtf/hg19_ccds.gtf";
		String outputFileName="/home/limeng/drugresistent/gtf/hg19_ccds_sort.gtf";
		
		//gtf.createIndex("");
		//ArrayList<String> ids = gtf
		//		.getTranscriptIDs("chr2", 22802096, 22812638);
		//for (int i = 0; i < ids.size(); ++i) {
		//	System.out.println(ids.get(i));
		//}
		gtf.sortGTF(inputFileName, outputFileName);
		// gtf.test(outputFileName);
		// gtf.createIndex("");
		// Iterator<String> ite=gtf.getCDSInformation("chr2",
		// 149945561,149945687).iterator();
		// while(ite.hasNext())
		// System.out.println(ite.next());
	}

	public static ArrayList<GTFFeature> getGTFFeatures(String gff) {

		// ArrayList<String> gffLines=new ArrayList<String>();
		ArrayList<GTFFeature> gtfFeatures = new ArrayList<GTFFeature>();
		GTFCode GFFCode = new GTFCode();

		String[] lines = gff.split("\n");
		for (int i = 0; i < lines.length; ++i) {
			// gffLines.add(lines[i]);
			if (lines[i].startsWith("#"))
				continue;

			gtfFeatures.add(GFFCode.decode(lines[i]));

		}

		Collections
				.sort(gtfFeatures, (new GetFeatureGTF().new ComparatorGTF()));

		return gtfFeatures;

	}

}
