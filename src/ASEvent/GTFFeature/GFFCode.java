package ASEvent.GTFFeature;

import org.broad.tribble.AsciiFeatureCodec;
import org.broad.tribble.readers.LineIterator;

import GeneStructure.Strand;

public class GFFCode extends AsciiFeatureCodec<GTFFeature> {

	public GFFCode(Class<GTFFeature> fre) {
		super(fre);
	}

	public GFFCode() {
		// this();
		super(GTFFeature.class);
	}

	@Override
	public GTFFeature decode(String string) {
		String[] arrayLine = string.split("\\t", 20);
		SimpleGTFFeature gtf = new SimpleGTFFeature();
		gtf.setChr(arrayLine[0]);
		gtf.setSource(arrayLine[2]);
		gtf.setFeature(arrayLine[1]);
		gtf.setStart(Integer.parseInt(arrayLine[3]));
		gtf.setEnd(Integer.parseInt(arrayLine[4]));
		// gtf.setScore(Float.parseFloat(arrayLine[5]));
		gtf.setScore(0);
		if (arrayLine[7].equals("."))
			gtf.setFrame(0);
		else
			gtf.setFrame(Integer.parseInt(arrayLine[7]));

		if (arrayLine[6].equals("+"))
			gtf.setStrand(Strand.POSITIVE);
		else if (arrayLine[6].equals("-"))
			gtf.setStrand(Strand.NEGATIVE);
		else
			gtf.setStrand(Strand.UNKNOWN);

		gtf.setAttribute(arrayLine[8]);

		// TODO Implement this method
		return gtf;
	}

	@Override
	public Object readActualHeader(LineIterator lineIterator) {
		// TODO Implement this method
		return null;
	}
}
