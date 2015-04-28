package GeneStructure;

import java.util.ArrayList;
import java.util.Iterator;

public class Transcript {
	public Transcript() {

	}

	public void setTranscriptName(String tname) {
		name = tname;

	}

	public String getTranscriptName() {
		return name;

	}

	private String name;

	private ArrayList<Exon> exons = new ArrayList<Exon>();

	public void addExon(Exon texon) {
		exons.add(texon);

	}

	public Iterator<Exon> getExonIterator() {

		return exons.iterator();

	}

}
