package ASEvent;

import java.util.ArrayList;
import GeneStructure.Exon;
import GeneStructure.Frame;
import GeneStructure.Strand;
import Utility.Algorithm.Translate;
import Utility.SequenceFeatureWrapper.SequenceFeatureExtractor;

public class ASTranslateEngine {
	public ASTranslateEngine() {
		super();
	}

	/*
	 * this method is the realy work method
	 */
	public static void translate(Exon exon2, Exon exon1, Exon exon3,
			Strand strand, ArrayList<String> proSeqs, ArrayList<Frame> frames) {

		proSeqs.clear();
		// ArrayList<Integer>orfs=new ArrayList<Integer>();
		if (exon1.getExonLength() < 30 || exon3.getExonLength() < 30) {
			// orfs.add(-1);
			return;// orfs;
		}

		String seq;

		if (strand.equals(Strand.POSITIVE)) {
			int seqBeg = exon1.getExonBegCoorPos();
			seq = exon1.getSequenceAsString() + exon2.getSequenceAsString()
					+ exon3.getSequenceAsString();

			// DNASequence dna=new DNASequence(seq);
			int relativeExonBeg = exon1.getExonEndCoorPos()
					- exon1.getExonBegCoorPos() + 1 + 1;// 1-based index
			int relativeExonEnd = relativeExonBeg + exon2.getExonEndCoorPos()
					- exon2.getExonBegCoorPos();// 1-based index
			// frame one
			// String
			// s=dna.getRNASequence(Frame.ONE).getProteinSequence().getSequenceAsString();

			String s = Translate.translate(seq);
			if (testFrame(relativeExonBeg, relativeExonEnd, s, proSeqs, strand)) {
				frames.add(Frame.ONE);
			}

			// s=dna.getRNASequence(Frame.TWO).getProteinSequence().getSequenceAsString();
			s = Translate.translate(seq.substring(1));
			if (testFrame(relativeExonBeg + 1, relativeExonEnd + 1, s, proSeqs,
					strand)) {
				frames.add(Frame.TWO);
			}

			// s=dna.getRNASequence(Frame.THREE).getProteinSequence().getSequenceAsString();
			s = Translate.translate(seq.substring(2));
			if (testFrame(relativeExonBeg + 1, relativeExonEnd + 1, s, proSeqs,
					strand)) {
				frames.add(Frame.THREE);
			}

		}
		if (strand.equals(Strand.NEGATIVE)) {
			int seqEnd = exon3.getExonEndCoorPos();
			seq = exon1.getSequenceAsString() + exon2.getSequenceAsString()
					+ exon3.getSequenceAsString();
			seq = Translate.getReverseCompliment(seq);
			// DNASequence dna=new DNASequence(seq);

			// 3-1 4-2 5-1
			int relativeExonBeg = exon3.getExonEndCoorPos()
					- exon3.getExonBegCoorPos() + 2;// 1-based index
			int relativeExonEnd = relativeExonBeg + exon2.getExonEndCoorPos()
					- exon2.getExonBegCoorPos();// 1-based index
			// frame one
			// String
			// s=dna.getRNASequence(Frame.REVERSED_ONE).getProteinSequence().getSequenceAsString();
			String s = Translate.translate(seq);
			// s = new StringBuffer(s).reverse().toString();
			if (testFrame(relativeExonBeg, relativeExonEnd, s, proSeqs, strand)) {
				frames.add(Frame.ONE);
			}
			// s=dna.getRNASequence(Frame.REVERSED_TWO).getProteinSequence().getSequenceAsString();
			s = Translate.translate(seq.substring(1));
			// s = new StringBuffer(s).reverse().toString();
			if (testFrame(relativeExonBeg - 1, relativeExonEnd - 1, s, proSeqs,
					strand)) {
				frames.add(Frame.TWO);
			}

			// s=dna.getRNASequence(Frame.REVERSED_THREE).getProteinSequence().getSequenceAsString();
			s = Translate.translate(seq.substring(2));
			// s = new StringBuffer(s).reverse().toString();
			if (testFrame(relativeExonBeg - 2, relativeExonEnd - 2, s, proSeqs,
					strand)) {
				frames.add(Frame.THREE);
			}
			/*
			 * for(int i=0;i<orfs.size();++i){
			 * 
			 * if(orfs.get(i)==1) orfs.set(i, 3); }
			 * 
			 * for(int i=0;i<orfs.size();++i){ if(orfs.get(i)==0) orfs.set(i,
			 * 1); }
			 */
		}

		// return orfs;
		// return true;
	}

	private static boolean testFrame(int relativeExonBeg, int relativeExonEnd,
			String s, ArrayList<String> proSeqs, Strand strand) {
		int proteinBeg = (int) Math.ceil(relativeExonBeg / 3.0);
		int proteinEnd = (int) Math.floor(relativeExonEnd / 3.0);
		// int beg=0;
		// int end=s.length()-1;
		/*
		 * for(int i=0;i<proteinBeg;++i){ if(s.charAt(i)=='*') beg=i+1; }
		 * for(int i=s.length()-1;i>proteinEnd;--i){ if(s.charAt(i)=='*')
		 * end=i-1; }
		 * 
		 * 
		 * if(proteinBeg-beg<9||end-proteinEnd<9) return false;
		 * 
		 * 
		 * 
		 * for(int i=proteinBeg;i<proteinEnd;++i) if(s.charAt(i)=='*') return
		 * false;
		 * 
		 * for(int i=0;i<proteinEnd+10;++i) if(s.charAt(i)=='*') return false;
		 */

		if (proteinBeg < 9)
			return false;

		for (int i = 0; i < proteinEnd + 10; ++i) {
			if (s.charAt(i) == '*')
				return false;
		}

		int stopIndex = s.indexOf('*');
		if (stopIndex != -1) {
			s = s.substring(0, stopIndex);
		}

		if (strand.equals(Strand.POSITIVE)) {
			proSeqs.add(s);
			// proSeqs.add(new ProteinSequence(s.substring(beg, end)));

		} else {
			// for(int i=s.length()-1;i>proteinBeg-10;--i)
			// if(s.charAt(i)=='*')
			// return false;

			// s=s.substring(beg, end);
			// s = new StringBuffer(s).reverse().toString();
			proSeqs.add(s);

		}

		return true;
	}

	public static String translateARegion(String torg, String tchr, int tbeg,
			int tend, int tframe, Strand tstrand) {
		String dna = "";
		String protein = "";

		// dna = fais.getSequence(tchr, tbeg, tend).getSequenceAsString();
		dna = SequenceFeatureExtractor.getSequence(torg, tchr, tbeg, tend);

		if (dna.length() - tframe >= 3) {
			if (tstrand.equals(Strand.POSITIVE)) {
				// /String dnaStr=dna.substring(tframe,dna.length());
				// protein=(new
				// DNASequence(dna.substring(0,dna.length()))).getRNASequence(FrameConvertToInt.convertintToFrame(tframe)).getProteinSequence().getSequenceAsString();
				protein = Translate.translate(dna.substring(tframe));

			} else {
				// protein=(new
				// DNASequence(dna.substring(0,dna.length()))).getRNASequence(FrameConvertToInt.convertintToFrame(3+tframe)).getProteinSequence().getSequenceAsString();
				dna = Translate.getReverseCompliment(dna);
				protein = Translate.translate(dna.substring(tframe));

			}
		} else
			protein = dna;

		return protein;// +"\t"+dna;
	}

	public static void main(String[] args) {
		// DNASequence seq = new
		// DNASequence("ttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
		// seq.setBioBegin(1000);
		// seq.setBioEnd(1168);
		// DNASequence exon1=new DNASequence();
		// DNASequence exonAS=new DNASequence();

		// seq.setBioEnd();
		// ArrayList<Integer> x = translateASEvnet(seq, 1000, 1000 +
		// seq.getLength(), 1040, 1080, Strand.POSITIVE);
		// System.out.println(x.get(0));
		// System.out.println(ASTranslateEngine.testHasStopCodon(seq));

		// ASDebug.output(seq.getRNASequence().getProteinSequence().getSequenceAsString());
	}

	private static boolean testIfHaveStopCodon(String proStr, int exonLength) {
		int proteinLength = proStr.length();
		if (proteinLength * 3 + 2 < exonLength)
			return true;
		for (int i = 0; i < proteinLength; ++i) {
			if (proStr.charAt(i) == '*') {
				return true;
			}
		}

		return false;
	}

	public static void translateOnlyExon(Exon exon, Strand strand,
			ArrayList<String> proSeqs, ArrayList<Frame> frames) {
		Frame[] frame = { Frame.ONE, Frame.TWO, Frame.THREE };
		int exonLen = exon.getExonLength();

		if (strand.equals(Strand.POSITIVE)) {
			int exonBegPos = exon.getExonBegCoorPos();
			for (int i = 0; i < 3; ++i) {
				// ProteinSequence
				// protein=exon.getRNASequence(frame[i]).getProteinSequence();
				// ordinal is 0 based index

				String protein = Translate.translate(exon.getSequenceAsString()
						.substring(frame[i].ordinal()));
				String proStr = protein;
				if (exonLen - i < 3)
					continue;

				if (!testIfHaveStopCodon(proStr, exonLen - i)) {
					proSeqs.add(protein);
					frames.add(frame[i]);

				}

			}

		}

		if (strand.equals(Strand.NEGATIVE)) {
			int exonEndPos = exon.getExonEndCoorPos();
			for (int i = 0; i < 3; ++i) {
				String protein = Translate.getReverseCompliment(exon
						.getSequenceAsString().substring(frame[i].ordinal()));

				protein = Translate.translate(protein);

				// String proStr=protein.getSequenceAsString();

				if (exonLen - i + 3 < 3)
					continue;

				if (!testIfHaveStopCodon(protein, exonLen - i)) {
					proSeqs.add(protein);
					frames.add(frame[i]);

				}

			}

		}

	}

	public static String translateUniverse(String tsequence, Strand tstrand) {
		String seq = "";
		if (tstrand.equals(Strand.NEGATIVE)) {
			seq = Translate.getReverseCompliment(tsequence);
		} else {
			seq = tsequence;
		}

		String protein = Translate.translate(seq);

		return protein;

	}

}
