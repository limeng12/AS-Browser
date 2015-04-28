package ASEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import ASEvent.GTFFeature.GTFFeature;
import Utility.SequenceFeatureWrapper.DatabaseUCSC;
import Utility.SequenceFeatureWrapper.SequenceFeatureExtractor;

public class ASEventBrowserTranscriptID extends ASEventBrowserExons {

	public ASEventBrowserTranscriptID(String toneEvent, String torganism) {

		super(toneEvent, torganism);
		oneEvent = parseDIToExons(toneEvent);

		// TODO Auto-generated constructor stub
	}

	private class ComparatorGTFFeature implements Comparator<GTFFeature> {

		@Override
		public int compare(GTFFeature o1, GTFFeature o2) {
			// TODO Auto-generated method stub
			return o1.getStart() - o2.getStart();

			// return 0;
		}

	}

	private String idConvertor(String id) {
		if (id.startsWith("uc"))
			return DatabaseUCSC.ucscTranscriptNameToEnsemblName(id);

		if (id.startsWith("NM"))
			return DatabaseUCSC.refseqTNameToEnsemblName(id);

		return id;
	}

	private String parseDIToExons(String toneEvent) {
		// TODO Auto-generated method stub
		String[] a = toneEvent.split("@");

		String transcriptId = idConvertor(a[0]);
		this.id = transcriptId;

		String[] positions = a[1].split(":");
		HashSet<Integer> pos = new HashSet<Integer>();

		for (int i = 0; i < positions.length; ++i) {
			pos.add(Integer.parseInt(positions[i]));

		}

		ArrayList<GTFFeature> gtfFeatures = SequenceFeatureExtractor
				.getTranscriptsOfATranscript(transcriptId);
		
		System.out.println(transcriptId);
		//System.out.println(gtfFeatures.get(0).getTranscriptID());
		
		/*
		 * ArrayList<GTFFeature> gtfFeatures = new ArrayList<GTFFeature>();
		 * 
		 * String[] gffs = gff.split("\n"); GTFCode c = new GTFCode(); for (int
		 * i = 0; i < gffs.length; ++i) { if (gffs[i].startsWith("#")) continue;
		 * 
		 * gtfFeatures.add(c.decode(gffs[i]));
		 * 
		 * }
		 */

		Collections.sort(gtfFeatures, new ComparatorGTFFeature());

		String exons = "";

		int exonIndex = 1;
		for (GTFFeature itegtf : gtfFeatures) {
			String attr = itegtf.getAttribute();
			String[] attLine = attr.split(";");

			String ttranscriptId = "";
			for (int i = 0; i < attLine.length; ++i) {
				if (attLine[i].contains("transcript_id")) {
					// cds+=attLine[i];
					ttranscriptId = attLine[i].substring(16, 31);

				}
				// cdss.add(cds);
			}
			if (transcriptId.equalsIgnoreCase(ttranscriptId)) {
				exons += "@" + itegtf.getChr() + ":" + itegtf.getStart() + ":"
						+ itegtf.getEnd() + ":" + itegtf.getStrand().toSymbol();// //here
																				// bug

				if (pos.contains(exonIndex)) {
					exons += ":splice";
				}
				exonIndex++;
			}

		}

		if (exons.length() == 0)
			return "";

		return exons.substring(1);// cut the first @
	}

}
