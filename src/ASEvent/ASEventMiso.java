package ASEvent;

import java.util.ArrayList;
import java.util.regex.Pattern;

import GeneStructure.Frame;
import GeneStructure.Strand;
import Utility.ASDebug;
import Utility.Structuure.ASTYPE;
import Utility.Structuure.Pair;

public class ASEventMiso extends ASevent {

	public ASEventMiso(String toneEvent, String torganism) {
		super(toneEvent, torganism);

		// TODO Auto-generated constructor stub
	}
	
	public ASEventMiso(String toneEvent, String torganism,boolean toutputAllIsoform) {
		super(toneEvent, torganism);
		
		outputAllIsoform=toutputAllIsoform;
		// TODO Auto-generated constructor stub
	}
	
	private boolean outputAllIsoform=false;
	
	public static String regexCasseteExon = "chr\\w+:(\\d+):(\\d+):(\\+|-)@chr\\w+:(\\d+):(\\d+):(\\+|-)@chr\\w+:(\\d+):(\\d+):(\\+|-)";

	public static String regexA5SS = "chr\\w+:(\\d+):(\\d+)\\|(\\d+):(\\+|-)@chr(\\w+):(\\d+):(\\d+):(\\+|-)";

	public static String regexA3SS = "chr(\\w+):(\\d+):(\\d+):(\\+|-)@chr\\w+:(\\d+)\\|(\\d+):(\\d+):(\\+|-)";

	public static String regexRetainedIntro = "chr(\\w+):(\\d+)-(\\d+):(\\+|-)@chr(\\w+):(\\d+)-(\\d+):(\\+|-)";

	private void buildGeneFragPostive() {
		String[] args = oneEvent.split("\\@", 20);
		if (asType.equals(ASTYPE.SE)) {

			for (int i = 0; i < args.length; ++i) {
				String[] quer = args[i].split("\\:", 20);
				int beg = Integer.parseInt(quer[1]);
				int end = Integer.parseInt(quer[2]);

				frag.addExon(organism, quer[0], beg, end, strand);
				chr = quer[0];
			}

		}

		if (asType.equals(ASTYPE.RI)) {

			String[] quert = args[0].split("\\:", 20);
			chr = quert[0];

			String[] quer = quert[1].split("-", 20);
			int beg1 = Integer.parseInt(quer[0]);
			int end1 = Integer.parseInt(quer[1]);

			frag.addExon(organism, chr, beg1, end1, strand);

			quert = args[1].split("\\:", 20);
			quer = quert[1].split("-", 20);

			int beg2 = Integer.parseInt(quer[0]);
			int end2 = Integer.parseInt(quer[1]);
			frag.addExon(organism, chr, end1 + 1, beg2 - 1, strand);
			frag.addExon(organism, chr, beg2, end2, strand);

		}

		if (asType.equals(ASTYPE.A5SS)) {

			String[] quer = args[0].split("\\:", 20);
			String[] begs = quer[2].split("\\|");
			int beg = Integer.parseInt(quer[1]);

			int end = Integer.parseInt(begs[1]);
			int endAS = Integer.parseInt(begs[0]);

			frag.addExon(organism, quer[0], beg, endAS, strand);
			frag.addExon(organism, quer[0], endAS + 1, end, strand);

			quer = args[1].split("\\:", 20);
			beg = Integer.parseInt(quer[1]);
			end = Integer.parseInt(quer[2]);
			frag.addExon(organism, quer[0], beg, end, strand);
			chr = quer[0];

		}

		if (asType.equals(ASTYPE.A3SS)) {

			String[] quer = args[0].split("\\:", 20);
			int beg = Integer.parseInt(quer[1]);
			int end = Integer.parseInt(quer[2]);

			frag.addExon(organism, quer[0], beg, end, strand);

			quer = args[1].split("\\:", 20);
			String[] ends = quer[1].split("\\|");

			end = Integer.parseInt(quer[2]);
			beg = Integer.parseInt(ends[0]);
			int begAS = Integer.parseInt(ends[1]);

			frag.addExon(organism, quer[0], beg, begAS - 1, strand);
			frag.addExon(organism, quer[0], begAS, end, strand);
			chr = quer[0];

		}

	}

	private void buildGeneFragNegative() {
		String[] args = oneEvent.split("\\@", 20);
		if (asType.equals(ASTYPE.SE)) {

			for (int i = args.length - 1; i >= 0; --i) {
				String[] quer = args[i].split("\\:", 20);
				int beg = Integer.parseInt(quer[1]);
				int end = Integer.parseInt(quer[2]);

				frag.addExon(organism, quer[0], beg, end, strand);
				chr = quer[0];

			}

		}

		if (asType.equals(ASTYPE.RI)) {

			String[] quert = args[1].split("\\:", 20);
			chr = quert[0];
			String[] quer = quert[1].split("-", 20);

			int beg1 = Integer.parseInt(quer[1]);
			int end1 = Integer.parseInt(quer[0]);

			frag.addExon(organism, chr, beg1, end1, strand);

			quert = args[0].split("\\:", 20);
			quer = quert[1].split("-", 20);

			int beg2 = Integer.parseInt(quer[1]);
			int end2 = Integer.parseInt(quer[0]);
			frag.addExon(organism, chr, end1 + 1, beg2 - 1, strand);
			frag.addExon(organism, chr, beg2, end2, strand);

		}

		if (asType.equals(ASTYPE.A5SS)) {

			String[] quer = args[1].split("\\:", 20);
			int beg = Integer.parseInt(quer[1]);
			int end = Integer.parseInt(quer[2]);
			frag.addExon(organism, quer[0], beg, end, strand);

			quer = args[0].split("\\:", 20);
			String[] begs = quer[2].split("\\|");
			end = Integer.parseInt(quer[1]);

			beg = Integer.parseInt(begs[1]);
			int begAS = Integer.parseInt(begs[0]);

			frag.addExon(organism, quer[0], begAS, beg - 1, strand);
			frag.addExon(organism, quer[0], beg, end, strand);
			chr = quer[0];

		}

		if (asType.equals(ASTYPE.A3SS)) {
			String[] quer = args[1].split("\\:", 20);
			String[] ends = quer[1].split("\\|");

			int beg = Integer.parseInt(quer[2]);
			int endAS = Integer.parseInt(ends[0]);
			int end = Integer.parseInt(ends[1]);

			frag.addExon(organism, quer[0], beg, endAS, strand);
			frag.addExon(organism, quer[0], endAS + 1, end, strand);
			quer = args[0].split("\\:", 20);
			beg = Integer.parseInt(quer[1]);
			end = Integer.parseInt(quer[2]);

			frag.addExon(organism, quer[0], beg, end, strand);
			chr = quer[0];

		}
	}

	protected void buildGeneFrag() {
		if (oneEvent.endsWith("-")) {
			strand = Strand.NEGATIVE;
			buildGeneFragNegative();
		} else {
			strand = Strand.POSITIVE;
			buildGeneFragPostive();
		}
		// chr=chr.substring(3);
		frag.getExon(2).setAlternative(true);
		
	}

	protected ASTYPE tellASType(String pos) {
		if (Pattern.matches(regexCasseteExon, pos.subSequence(0, pos.length())))
			return ASTYPE.SE;
		else if (Pattern.matches(regexA5SS, pos.subSequence(0, pos.length())))
			return ASTYPE.A5SS;
		else if (Pattern.matches(regexA3SS, pos.subSequence(0, pos.length())))
			return ASTYPE.A3SS;
		else if (Pattern.matches(regexRetainedIntro,
				pos.subSequence(0, pos.length())))
			return ASTYPE.RI;

		return ASTYPE.UNKNOWN;
	}

	
	protected void translate() {
		ArrayList<String> proSeqs = new ArrayList<String>();
		ArrayList<Frame> frames = new ArrayList<Frame>();

		ASTranslateEngine.translate(frag.getExon(2), frag.getExon(1),
				frag.getExon(3), strand, proSeqs, frames);

		for (int i = 0; i < proSeqs.size(); ++i) {
			Pair<Integer, Integer> asPosInProtein =getASPositionInProtein(frames.get(i));

			ASDebug.output(proSeqs.get(i));

			EventIsoformFeature a = new EventIsoformFeature(proSeqs.get(i),
					frames.get(i), chr,asPosInProtein);

			eventIsoforms.add(a);

		}
		if(!outputAllIsoform){
		//get the longest isoform
		int longestIndex=-1;
		int longestLength=-1;
		//ArrayList<Integer> proteinsLength=new ArrayList<Integer>();
		EventIsoformFeature longestIsoform=new EventIsoformFeature();
		
		for(int i=0;i<eventIsoforms.size();++i){
			int l=eventIsoforms.get(i).proteinSequence.length();
			if(l>longestLength){
				longestIndex=i;
			}
			
			
		}
		if(longestIndex>=0){
			longestIsoform=eventIsoforms.get(longestIndex);
		
			eventIsoforms.clear();
			eventIsoforms.add(longestIsoform);
			
		}
		}
		//eventIsoforms.remove(index)
		
		
	}

	private Pair<Integer, Integer> getASPositionInProtein(Frame tframe) {
		int asProteinBegPos = 0;
		int asProteinEndPos = 0;
		if (strand.equals(Strand.POSITIVE)) {
			int asProteinBegLen = frag.getExon(1).getExonLength();
			int asProteinEndLen = frag.getExon(1).getExonLength()
					+ frag.getExon(2).getExonLength();
			// switch(frames.get(i)){
			if (tframe.equals(Frame.ONE)) {
				asProteinBegPos = asProteinBegLen / 3;
			}
			if (tframe.equals(Frame.TWO)) {
				asProteinBegPos = (asProteinBegLen - 1) / 3;
			}
			if (tframe.equals(Frame.THREE)) {
				asProteinBegPos = (asProteinBegLen - 2) / 3;
			}
			asProteinBegPos += 1;
			asProteinEndPos = (asProteinEndLen + 1) / 3;

			return new Pair<Integer, Integer>(asProteinBegPos, asProteinEndPos);

			// asExonProteinPosition.add(new Pair<Integer,
			// Integer>(asProteinBegPos, asProteinEndPos));

		} else {
			int asProteinBegLen = frag.getExon(3).getExonLength();

			int asProteinEndLen = frag.getExon(3).getExonLength()
					+ frag.getExon(2).getExonLength();
			// switch(frames.get(i)){
			if (tframe.equals(Frame.ONE)) {
				asProteinBegPos = asProteinBegLen / 3;
			}
			if (tframe.equals(Frame.TWO)) {
				asProteinBegPos = (asProteinBegLen - 1) / 3;
			}
			if (tframe.equals(Frame.THREE)) {
				asProteinBegPos = (asProteinBegLen - 2) / 3;
			}

			asProteinBegPos += 1;
			asProteinEndPos = (asProteinEndLen + 1) / 3;

			return new Pair<Integer, Integer>(asProteinBegPos, asProteinEndPos);

			// asExonProteinPosition.add(new Pair<Integer,
			// Integer>(asProteinBegPos, asProteinEndPos));

		}
	}

}
