package Utility.Algorithm;

import org.biojava3.core.sequence.transcription.Frame;

public class FrameConvertToInt {
	public FrameConvertToInt() {
		super();
	}

	static public int convertFrameToInt(Frame frame) {
		if (frame.equals(Frame.ONE))
			return 1;
		if (frame.equals(Frame.TWO))
			return 2;
		if (frame.equals(Frame.THREE))
			return 3;

		if (frame.equals(Frame.REVERSED_ONE))
			return 1;
		if (frame.equals(Frame.REVERSED_TWO))
			return 2;
		if (frame.equals(Frame.REVERSED_THREE))
			return 3;

		return -1;
	}

	public static Frame convertintToFrame(int t) {
		if (t == 0)
			return Frame.ONE;
		if (t == 1)
			return Frame.TWO;
		if (t == 2)
			return Frame.THREE;

		if (t == 3)
			return Frame.REVERSED_ONE;
		if (t == 4)
			return Frame.REVERSED_TWO;
		if (t == 5)
			return Frame.REVERSED_THREE;

		return Frame.ONE;

	}

}
