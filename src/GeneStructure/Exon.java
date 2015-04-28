package GeneStructure;

public class Exon {
	public Exon(String torg, String tchr, String tseq, int tbegPos, int tendPos) {
		org = torg;
		tchr = chr;
		sequence = tseq;

		begPos = tbegPos;
		endPos = tendPos;

	}

	public Exon(String torg, String tchr, String tseq) {
		org = torg;
		tchr = chr;
		sequence = tseq;

	}

	public Exon(String tseq) {
		sequence = tseq;

	}

	public void setExonBegCoorPos(int tbegpos) {
		begPos = tbegpos;
	}

	public void setExonEndCoorPos(int tendpos) {
		endPos = tendpos;
	}

	public void setAlternative(boolean tisAlternative) {
		isAlternative = tisAlternative;

	}

	public boolean getIfAlternative() {
		return isAlternative;
	}

	private String name;

	private String sequence;

	private boolean isAlternative = false;

	private String org = "";

	private String chr = "";

	private int begPos = -1;

	private int endPos = -1;

	private int modBy3 = -1;

	public int getModBy3() {

		return (endPos - begPos + 1) % 3;

	}

	public int getExonBegCoorPos() {
		return begPos;

	}

	public int getExonEndCoorPos() {
		return endPos;

	}

	public int getExonLength() {
		return endPos - begPos + 1;

	}

	public String getSequenceAsString() {
		return sequence;

	}
}
