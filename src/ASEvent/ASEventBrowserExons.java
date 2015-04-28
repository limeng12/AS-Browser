package ASEvent;

import java.util.ArrayList;

import GeneStructure.Strand;
import Utility.ASDebug;
import Utility.Structuure.ASTYPE;

public class ASEventBrowserExons extends ASevent {

	public ASEventBrowserExons(String toneEvent, String torganism) {
		super(toneEvent, torganism);
		// TODO Auto-generated constructor stub
	}

	protected void buildGeneFrag() {
		String[] exonPos = oneEvent.split("\\@", 1000);
		for (int i = 0; i < exonPos.length; ++i) {
			String[] oneExon = exonPos[i].split("\\:", 5);

			if (oneExon[0].startsWith("chr"))
				chr = oneExon[0];

			String strandStr = oneExon[3];

			if (strandStr.equals("-")) {
				strand = Strand.NEGATIVE;
			} else {
				strand = Strand.POSITIVE;
			}

			boolean isAlternative = false;
			if (oneExon.length >= 5 && oneExon[4].equalsIgnoreCase("Splice")) {
				isAlternative = true;
			}

			int beg = Integer.parseInt(oneExon[1]);
			int end = Integer.parseInt(oneExon[2]);

			frag.addExon(organism, chr, beg, end, strand, isAlternative);

		}

		// chr=chr.substring(3);
	}

	protected ASTYPE tellASType(String pos) {

		return ASTYPE.UNKNOWN;
	}

	@Override
	protected void translate() {
		// TODO Auto-generated method stub
		ArrayList<String> proSeqs = new ArrayList<String>();
		// ArrayList<Frame> frames = new ArrayList<Frame>();

		// ASTranslateEngine.translate(frag.getExon(2),
		// frag.getExon(1),frag.getExon(3), strand, proSeqs, frames);
		for (int i = 0; i < 2; ++i) {

			String protein = "";
			if (0 == i)
				protein = ASTranslateEngine.translateUniverse(
						frag.getSequence(), strand);
			else
				protein = ASTranslateEngine.translateUniverse(
						frag.getSequenceWithoutASExons(), strand);
			
			int stopCodonIndex=protein.indexOf("*");
			if(stopCodonIndex!=-1)
				protein=protein.substring(0,stopCodonIndex);

			proSeqs.add(protein);

			ASDebug.output(protein);

			EventIsoformFeature a = new EventIsoformFeature(proSeqs.get(i), chr);

			eventIsoforms.add(a);

		}

		/*
		 * for (int i = 0; i < proSeqs.size(); ++i) { //Pair<Integer, Integer>
		 * asPosInProtein = getASPositionInProtein(frames.get(i));
		 * 
		 * ASDebug.output(proSeqs.get(i));
		 * 
		 * EventIsoformFeature a = new EventIsoformFeature(proSeqs.get(i),
		 * frames.get(i), chr);
		 * 
		 * eventIsoforms.add(a);
		 * 
		 * }
		 */

	}

}
