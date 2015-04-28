package ASEvent.GTFFeature;

import org.broad.tribble.Feature;

import GeneStructure.Strand;

public interface GFFFeature extends Feature {

	Strand getStrand();

	String getSource();

	String getFeature();

	String getTranscriptID();

	void setTranscriptID(String id);

	float getScore();

	int getFrame();

	String getAttribute();
}
