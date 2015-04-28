package Utility.Structuure;

import java.util.HashMap;

public class AminoAcidMap {
	public AminoAcidMap() {
		super();

	}

	static boolean init = false;
	static HashMap<String, String> aaTable = new HashMap<String, String>();

	static public String getAminoAcid(String aa) {
		if (!init) {
			aaTable.put("PHE", "F");
			aaTable.put("LEU", "L");
			aaTable.put("ILE", "I");
			aaTable.put("MET", "M");
			aaTable.put("VAL", "V");
			aaTable.put("SER", "S");
			aaTable.put("PRO", "P");
			aaTable.put("THR", "T");
			aaTable.put("ALA", "A");
			aaTable.put("TYR", "Y");
			aaTable.put("*", "*");
			aaTable.put("HIS", "H");
			aaTable.put("GLN", "Q");
			aaTable.put("ASN", "N");
			aaTable.put("LYS", "K");
			aaTable.put("ASP", "D");
			aaTable.put("GLU", "E");
			aaTable.put("CYS", "C");
			aaTable.put("ARG", "R");
			aaTable.put("SER", "S");
			aaTable.put("GLY", "G");

			init = true;
		}

		return aaTable.get(aa);
	}

	static public boolean haveAminoAcid(String aa) {
		if (!init) {
			aaTable.put("PHE", "F");
			aaTable.put("LEU", "L");
			aaTable.put("ILE", "I");
			aaTable.put("MET", "M");
			aaTable.put("VAL", "V");
			aaTable.put("SER", "S");
			aaTable.put("PRO", "P");
			aaTable.put("THR", "T");
			aaTable.put("ALA", "A");
			aaTable.put("TYR", "Y");
			aaTable.put("*", "*");
			aaTable.put("HIS", "H");
			aaTable.put("GLN", "Q");
			aaTable.put("ASN", "N");
			aaTable.put("LYS", "K");
			aaTable.put("ASP", "D");
			aaTable.put("GLU", "E");
			aaTable.put("CYS", "C");
			aaTable.put("ARG", "R");
			aaTable.put("SER", "S");
			aaTable.put("GLY", "G");

			init = true;
		}

		return aaTable.containsKey(aa);
	}

}
