package ASEvent;

import java.util.ArrayList;
import java.util.Iterator;

import GeneStructure.Exon;
import GeneStructure.Strand;
import Utility.SequenceFeatureWrapper.SequenceFeatureExtractor;

public class Fragment {
	Fragment() {
		// chrosome=tchrosome;
	}

	String chrosome = "";
	String organism = "";

	/*
	 * store the exons
	 */
	ArrayList<Exon> exons = new ArrayList<Exon>();

	/*
	 * strand of the sequence
	 */
	public Strand strand;

	Exon getExon(int index) {
		return exons.get(index - 1);

	}

	public void setStrand(Strand tstrand) {
		strand = tstrand;
	}

	public void addExon(Exon exon) {
		exons.add(exon);

	}

	public void addExon(String torganism, String chr, int begPos, int endPos,
			Strand strand, boolean isConstitute) {
		// String dna = GetSequenceUsingRest.getDNASeg(organism, chr, beg, end);

		organism = torganism;
		chrosome = chr;
		String dna;
		dna = SequenceFeatureExtractor.getSequence(organism, chr, begPos,
				endPos);

		this.setStrand(strand);
		Exon x = new Exon(organism, chr, dna);

		x.setExonBegCoorPos(begPos);
		x.setExonEndCoorPos(endPos);
		x.setAlternative(isConstitute);

		this.addExon(x);

	}

	public void addExon(String organism, String chr, int begPos, int endPos,
			Strand strand) {
		// String dna = GetSequenceUsingRest.getDNASeg(organism, chr, beg, end);

		chrosome = chr;
		String dna;
		dna = SequenceFeatureExtractor.getSequence(organism, chr, begPos,
				endPos);

		this.setStrand(strand);
		Exon x = new Exon(organism, chr, dna);

		x.setExonBegCoorPos(begPos);
		x.setExonEndCoorPos(endPos);

		this.addExon(x);

	}

	public int getExonLenght(int i) {
		return exons.get(i - 1).getExonLength();

	}

	public String getSequence() {
		String tseq = "";

		for (int i = 0; i < exons.size(); ++i) {
			tseq += exons.get(i).getSequenceAsString();
		}

		return tseq;

	}

	public String getSequenceWithoutASExons() {
		String result = "";

		for (Exon e : exons) {
			if (!e.getIfAlternative())
				result += e.getSequenceAsString();

		}

		return result;
	}

	public int getSeqBegPos() {
		return exons.get(0).getExonBegCoorPos();
	}

	public int getSeqEndPos() {
		return exons.get(exons.size() - 1).getExonEndCoorPos();

	}

	public Iterator<Exon> getExonsIterator() {
		return exons.iterator();

	}

	public String getSequenceOfTheGene() {
		int begPos = getSeqBegPos();
		int endPos = getSeqEndPos();

		String dna = SequenceFeatureExtractor.getSequence(organism, chrosome,
				begPos, endPos);

		return dna;

	}
	// get exon is 1 bsed index public Exon getExon(int i){

}
