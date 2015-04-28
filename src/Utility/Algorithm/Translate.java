package Utility.Algorithm;

import java.util.HashMap;

public class Translate {
	public Translate() {
		super();
	}

	static HashMap<String, String> transLateMapOneWord = new HashMap<String, String>();

	static HashMap<String, String> transLateMapThreeWord = new HashMap<String, String>();

	static void init() {

		initTranslate();

	}

	static void initTranslate() {
		transLateMapOneWord.put("TTT", "F");
		transLateMapOneWord.put("TTC", "F");
		transLateMapOneWord.put("TTA", "L");
		transLateMapOneWord.put("TTG", "L");

		transLateMapOneWord.put("CTT", "L");
		transLateMapOneWord.put("CTC", "L");
		transLateMapOneWord.put("CTA", "L");
		transLateMapOneWord.put("CTG", "L");

		transLateMapOneWord.put("ATT", "I");
		transLateMapOneWord.put("ATC", "I");
		transLateMapOneWord.put("ATA", "I");
		transLateMapOneWord.put("ATG", "M");

		transLateMapOneWord.put("GTT", "V");
		transLateMapOneWord.put("GTC", "V");
		transLateMapOneWord.put("GTA", "V");
		transLateMapOneWord.put("GTG", "V");

		transLateMapOneWord.put("TCT", "S");
		transLateMapOneWord.put("TCC", "S");
		transLateMapOneWord.put("TCA", "S");
		transLateMapOneWord.put("TCG", "S");

		transLateMapOneWord.put("CCT", "P");
		transLateMapOneWord.put("CCC", "P");
		transLateMapOneWord.put("CCA", "P");
		transLateMapOneWord.put("CCG", "P");

		transLateMapOneWord.put("ACT", "T");
		transLateMapOneWord.put("ACC", "T");
		transLateMapOneWord.put("ACA", "T");
		transLateMapOneWord.put("ACG", "T");

		transLateMapOneWord.put("GCT", "A");
		transLateMapOneWord.put("GCC", "A");
		transLateMapOneWord.put("GCA", "A");
		transLateMapOneWord.put("GCG", "A");

		transLateMapOneWord.put("TAT", "Y");
		transLateMapOneWord.put("TAC", "Y");
		transLateMapOneWord.put("TAA", "*");
		transLateMapOneWord.put("TAG", "*");

		transLateMapOneWord.put("CAT", "H");
		transLateMapOneWord.put("CAC", "H");
		transLateMapOneWord.put("CAA", "Q");
		transLateMapOneWord.put("CAG", "Q");

		transLateMapOneWord.put("AAT", "N");
		transLateMapOneWord.put("AAC", "N");
		transLateMapOneWord.put("AAA", "K");
		transLateMapOneWord.put("AAG", "K");

		transLateMapOneWord.put("GAT", "D");
		transLateMapOneWord.put("GAC", "D");
		transLateMapOneWord.put("GAA", "E");
		transLateMapOneWord.put("GAG", "E");

		transLateMapOneWord.put("TGT", "C");
		transLateMapOneWord.put("TGC", "C");
		transLateMapOneWord.put("TGA", "*");
		transLateMapOneWord.put("TGG", "W");

		transLateMapOneWord.put("CGT", "R");
		transLateMapOneWord.put("CGC", "R");
		transLateMapOneWord.put("CGA", "R");
		transLateMapOneWord.put("CGG", "R");

		transLateMapOneWord.put("AGT", "S");
		transLateMapOneWord.put("AGC", "S");
		transLateMapOneWord.put("AGA", "R");
		transLateMapOneWord.put("AGG", "R");

		transLateMapOneWord.put("GGT", "G");
		transLateMapOneWord.put("GGC", "G");
		transLateMapOneWord.put("GGA", "G");
		transLateMapOneWord.put("GGG", "G");
	}

	static char complement(char tc) {
		if (tc == 'A')
			return 'T';
		if (tc == 'C')
			return 'G';
		if (tc == 'G')
			return 'C';
		if (tc == 'T')
			return 'A';

		return '*';
	}

	static boolean isInit = false;

	static public String translate(String tnucliedSequence) {
		if (!isInit)
			init();
		tnucliedSequence = tnucliedSequence.toUpperCase();

		String result = "";

		for (int i = 0; i < tnucliedSequence.length() / 3; ++i) {
			String aa = "";
			aa += tnucliedSequence.charAt(i * 3);
			aa += tnucliedSequence.charAt(i * 3 + 1);
			aa += tnucliedSequence.charAt(i * 3 + 2);
			aa = aa.toUpperCase();
			result += transLateMapOneWord.get(aa);
			// System.out.println(transLateMapOneWord.get(aa)+"\t"+aa);

		}
		
		return result;
	}

	static public String getReverseCompliment(String tnucliedSeq) {
		String reverseString = "";
		tnucliedSeq = tnucliedSeq.toUpperCase();

		for (int i = tnucliedSeq.length() - 1; i > 0; --i) {
			reverseString += complement(tnucliedSeq.charAt(i));
		}

		return reverseString;
	}

	public static void main(String[] args) {
		String a = "ATGGCTGGCTACCTGAGTGAATCGGACTTTGTGATGGTGGAGGAGGGCTT";
		String t = translate(getReverseCompliment(a).substring(2));

		System.out.println(t);
	}

}
