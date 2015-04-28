package ASEvent.GTFFeature;

import GeneStructure.Strand;

public class SimpleGTFFeature implements GTFFeature {
	public SimpleGTFFeature() {
		super();
	}

	private Strand strand;
	private String chr;
	private int startPos;
	private int endPos;
	private String source;
	private String featureName;
	private float score;
	private String attrStr;
	private int frame;
	private String transcriptID = "";

	@Override
	public Strand getStrand() {
		// TODO Implement this method
		return strand;
	}

	@Override
	public String getSource() {
		// TODO Implement this method
		return source;
	}

	@Override
	public String getFeature() {
		// TODO Implement this method
		return featureName;
	}

	@Override
	public float getScore() {
		// TODO Implement this method
		return score;
	}

	@Override
	public int getFrame() {
		// TODO Implement this method
		return frame;
	}

	@Override
	public String getAttribute() {
		// TODO Implement this method
		return attrStr;
	}

	@Override
	public String getChr() {
		// TODO Implement this method
		return chr;
	}

	@Override
	public int getStart() {
		// TODO Implement this method
		return startPos;
	}

	@Override
	public int getEnd() {
		// TODO Implement this method
		return endPos;
	}

	public void setStart(int tstartPosition) {
		startPos = tstartPosition;
	}

	public void setEnd(int tendPosition) {
		endPos = tendPosition;
	}

	public void setChr(String tchr) {
		chr = tchr;
	}

	public void setAttribute(String tattr) {
		attrStr = tattr;
	}

	public void setFrame(int tframe) {
		frame = tframe;
	}

	public void setFeature(String tFeature) {
		featureName = tFeature;
	}

	public void setStrand(Strand tstrand) {
		strand = tstrand;
	}

	public void setScore(float tscore) {
		score = tscore;
	}

	public void setSource(String tsource) {
		source = tsource;
	}

	@Override
	public String getTranscriptID() {
		// TODO Auto-generated method stub

		return transcriptID;
	}

	@Override
	public void setTranscriptID(String id) {

		transcriptID = id;

		// TODO Auto-generated method stub

	}

}
