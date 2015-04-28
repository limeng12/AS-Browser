package Utility.SequenceFeatureWrapper;

/*
 * get gene feature from bed
 */
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
import java.util.List;

import org.broad.tribble.AbstractFeatureReader;
import org.broad.tribble.FeatureReader;
import org.broad.tribble.bed.BEDCodec;
import org.broad.tribble.bed.BEDFeature;
import org.broad.tribble.bed.FullBEDFeature;
import org.broad.tribble.index.Index;
import org.broad.tribble.index.IndexFactory;
import org.broad.tribble.index.linear.LinearIndex;

public class GetFeatureBED {
	public GetFeatureBED() {
		super();
	}

	String annoRoot = "";
	Index index;
	private FeatureReader<BEDFeature> source;

	public class ComparatorBED implements Comparator<Object> {

		@Override
		public int compare(Object object1, Object object2) {
			// TODO Implement this method
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

			long beg1 = Long.parseLong(arrayLine1[1]);
			long beg2 = Long.parseLong(arrayLine2[1]);
			if (beg1 != beg2)
				return (int) (beg1 - beg2);

			// if (chr1.equals(chr2) && beg1 == beg2)
			return 0;
		}

	}

	void sortBed(String inputFileName, String outputFileName) {
		try {

			File in = new File(inputFileName);
			File out = new File(outputFileName);
			BufferedReader input = new BufferedReader(new FileReader(in));

			ArrayList<String> content = new ArrayList<String>();
			String line;
			while ((line = input.readLine()) != null) {
				content.add(line);
			}

			Collections.sort(content, (new ComparatorBED()));

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

	public void test(String fileName) {
		// String fileName = "F:\\AS-pipeline\\data\\anno\\sortmm9.bed";
		LinearIndex t = (LinearIndex) IndexFactory.createLinearIndex(new File(
				fileName), new BEDCodec());
		System.out.println(t.containsChromosome("chr1"));

		try {
			Iterator<BEDFeature> iter = (Iterator<BEDFeature>) source.query(
					"chr2", 1, 100000000);
			System.out.println(iter.next().getName());

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

	public void createIndex(String org) {
		File bed = new File(annoRoot);
		String[] names = bed.list();
		String fileName = annoRoot + names[0];
		index = IndexFactory.createIntervalIndex(new File(fileName),
				new BEDCodec());
		source = (FeatureReader<BEDFeature>) AbstractFeatureReader
				.getFeatureReader(fileName, new BEDCodec(), index);

	}

	public String getGeneName(String chr, long beg, long end) {
		String result = "";

		try {
			Iterator<BEDFeature> iter = source.query(chr, (int) beg, (int) end);
			while (iter.hasNext()) {
				result += iter.next().getName() + "\n";
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return result;

	}

	public ArrayList<List<FullBEDFeature.Exon>> getExons(String chr, long beg,
			long end) {
		// String result="";
		ArrayList<List<FullBEDFeature.Exon>> exons = new ArrayList<List<FullBEDFeature.Exon>>();
		try {
			Iterator<BEDFeature> iter = source.query(chr, (int) beg, (int) end);
			while (iter.hasNext()) {
				// result+=iter.next().getName()+"\n";
				exons.add(iter.next().getExons());

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return exons;

	}

	public static void main(String[] args) {

		GetFeatureBED bed = new GetFeatureBED();
		// bed.sortBed("F:\\AS-pipeline\\data\\anno\\mm9.bed","F:\\AS-pipeline\\data\\anno\\sortmm9.bed");
		bed.test("F:\\AS-pipeline\\data\\anno\\sortmm9.bed");

	}

}
