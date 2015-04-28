package Utility.Algorithm;

import org.biojava3.alignment.Alignments;
import org.biojava3.alignment.SimpleGapPenalty;
import org.biojava3.alignment.SimpleSubstitutionMatrix;
import org.biojava3.alignment.template.SequencePair;
import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.alignment.Alignments.PairwiseSequenceAlignerType;

import Utility.ASDebug;
import Utility.Structuure.Tris;

public class ProteinAlign {
	public ProteinAlign() {
		super();
	}

	public static String alignProteins(String templete, String alignProtein) {

		return align(templete, alignProtein).toString();

	}

	public static SequencePair<ProteinSequence, AminoAcidCompound> align(
			String protein1, String protein2) {
		ProteinSequence s1 = new ProteinSequence(protein1), s2 = new ProteinSequence(
				protein2);
		SubstitutionMatrix<AminoAcidCompound> matrix = new SimpleSubstitutionMatrix<AminoAcidCompound>();
		SequencePair<ProteinSequence, AminoAcidCompound> pair = Alignments
				.getPairwiseAlignment(s1, s2,
						PairwiseSequenceAlignerType.GLOBAL,
						new SimpleGapPenalty(), matrix);
		// pair.get
		System.out.printf("%n%s vs %s%n%s", pair.getQuery().getAccession(),
				pair.getTarget().getAccession(), pair);
		// pair.
		return pair;
	}

	public static Tris<String, Integer, Integer> getASSite(String protein1,
			String protein2) {
		SequencePair<ProteinSequence, AminoAcidCompound> pair = align(protein1,
				protein2);
		String searchStr = pair.toString().split("\n")[0].trim();
		int begPos = protein1.indexOf(searchStr);
		int endPos = begPos + searchStr.length();
		System.out.println("proteinAlign");
		System.out.println(protein1);
		System.out.println(searchStr);

		return new Tris<String, Integer, Integer>(pair.toString(), begPos,
				endPos);

	}

	/*
	 * public static int getSNPPosition(String proteinSequence, String
	 * pdbProtein, int tsnpPos) { // TODO Auto-generated method stub
	 * SequencePair<ProteinSequence, AminoAcidCompound>
	 * pair=align(proteinSequence,pdbProtein); String
	 * searchStr=pair.toString().split("\n")[1].trim(); int
	 * pos=pdbProtein.indexOf(searchStr); //pdbProtein[tsnpPos];
	 * if(tsnpPos+pos<pdbProtein.length()) return pos+tsnpPos; else return -1; }
	 */

	public static int getSNPPosition(String proteinSequence, String pdbProtein,
			int pos) {
		// TODO Auto-generated method stub
		int index = 0;
		String alignStatus;

		// if(protein.length()>pdbProtein.length())
		// alignStatus=ProteinAlign.alignProteins(pdbProtein, protein);

		// else
		// alignStatus=ProteinAlign.alignProteins(protein,pdbProtein);
		alignStatus = ProteinAlign.alignProteins(pdbProtein, proteinSequence);
		System.out.println(alignStatus);// .output(alignStatus);
		System.out.println("snp in transcript=" + pos);

		String align2[] = alignStatus.split("\n");

		ASDebug.output("pos=" + pos);

		int snpPosInAlign = pos;
		for (int i = 0; i < snpPosInAlign; ++i) {
			if (align2[1].charAt(i) == '-')
				snpPosInAlign++;

		}

		if (align2[0].charAt(snpPosInAlign) == '-'
				|| align2[1].charAt(snpPosInAlign) == '-') {
			// proteinHaveSNP=false;
			return -1;
		}

		int pdbShift = 0;

		for (int i = 0; i < snpPosInAlign; ++i) {
			if (align2[0].charAt(i) == '-')
				pdbShift++;
		}

		int snpPdbPos = snpPosInAlign - pdbShift;

		System.out.println("snp position in pdb=" + snpPdbPos);
		// if(align2[0].charAt(snpPdbPos)=='-')
		// return -1;
		if (snpPdbPos == 0)
			return -1;

		return snpPdbPos;

	}

	public static void main(String[] args) {
		// SequencePair<ProteinSequence, AminoAcidCompound>
		// pair=align("REGVRFAQSPAGEEARGDQSEKYMEFDLNNEGEIDLMSLKRMMEKLGVPKTHLEMKKMISEVTGGVSDTISYRDFVNMMLGKRSAVLKL","EKLGVPKTHLEMKKAAAMISEVTGGV"
		// );

		String proteinSequence = "NSKKKTKKLRMKRSHRVEPVNTDESTPKSPTPPQPPPPVGWGTPKVTRLPKLEPLGETRHNDFYGKPLPPLAVRQRPNGDAQDTIS";
		String uniprotSequence = "MFSLMANCCNLFKRWREPVRKVTLVMVGLDNAGKTATAKGIQGEHPEDVAPTVGFSKIDLRQGKFQVTIFDLGGGKRIRGIWKNYYAESYGVIFVVDSSDEERMEETKETMSEVLRHPRISGKPILVLANKQDKEGALGEADVIECLSLEKLVNEHKCLCQIEPCSAVLGYGKKIDKSIKKGLYWLLHIIAKDFDALSERIQKDTTEQRALEEQEKRERAERVRKLREEREREQTELDGTSGLAEIDSGPVLANPFQPIAAVIIENEKKQEKEKKKQTVEKDSDVGLLEHKVEPEQAAPQSEADCCLQNPDERVVDSYREALSQQLDSEDEQDQRGSESGENSKKKTKKLRMKRSHRVEPVNTDESTPKSPTPPQPPPPVGWGTPKVTRLPKLEPLGETRHNDFYGKPLPPLAVRQRPNGDAQDTIS";
		System.out
				.println(getSNPPosition(uniprotSequence, proteinSequence, 342));

		// System.out.println(pair.toString());
		// String
		// str="AEQLTEEQIAEFKEAFALFDKDGDGTITTKELGTVMRSLGQNPTEAELQDMINEVDADGNGTIDFPEFLSLMARKMKEQDSEEELIEAFKVFDRDGNGLISAAELRHVMTNLGEKLTDDEVDEMIREADIDGDGHINYEEFVRMMVSKAEQLTEEQIAEFKEAFALFDKDGDGTITTKELGTVMRSLGQNPTEAELQDMINEVDADGNGTIDFPEFLSLMARKMKEQDSEEELIEAFKVFDRDGNGLISAAELRHVMTNLGEKLTDDEVDEMIREADIDGDGHINYEEFVRMMVSK";
		// System.out.println(str.indexOf("EEQIAEFKEAFALFDKDGDGTITTKELGTVMRSLGQNPTEAELQDMINEVDADGNGTIDFPEFLSLMARK"));

		// REGVRFAQSPAGEEARGDQSEKYMEFDLNNEGEIDLMSLKRMMEKLGVPKTHLEMKKMISEVTGGVSDTISYRDFVNMMLGKRSAVLKL
	}

}