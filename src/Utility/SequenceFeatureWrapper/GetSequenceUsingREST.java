package Utility.SequenceFeatureWrapper;

/*
 * get sequence from UCSC DAS server
 */

import Utility.Algorithm.REST;

import java.io.IOException;

public class GetSequenceUsingREST {
	public GetSequenceUsingREST() {
		super();
	}

	static String serverString = "http://genome.ucsc.edu/cgi-bin/das/";

	//
	public static String getDNASeg(String org, String chr, int beg, int end) {
		// String qstr = serverString+"ce6/dna?segment=chrII:1,100000";

		String quer = serverString + org + "/" + "dna?segment=" + chr + ":"
				+ beg + "," + end;
		String result = "";
		result = REST.getResultLoop(quer);
		System.out.println(quer);
		System.out.println(result);

		result = passXML(result, end - beg + 1);

		return result;
	}

	static String passXML(String str, int length) {
		int end = str.indexOf("</DNA>");
		String searchTerm = "<DNA length=\"" + length + "\">";
		int beg = str.indexOf(searchTerm);

		// int beg=end-length;
		str = str.substring(beg + searchTerm.length(), end);
		return str.replaceAll("\\n", "");// cut the return symbol
	}

	public static String getSequenceEnsembl(String chr, int begPos, int endPos) {
		String ensemblStr = "http://beta.rest.ensembl.org/sequence/region/human/";

		String querl = ensemblStr + chr + ":" + begPos + ":" + endPos;
		querl = querl + "?content-type=text/plain";

		System.out.println(querl);

		String dna = REST.getResultLoop(querl);

		return dna.substring(0, dna.length() - 1);
	}

	public static void main(String args[]) throws IOException {

		System.out.println(GetSequenceUsingREST.getDNASeg("hg19", "chr1",
				1100000, 1110000));
		// for( int i=0;i<12;++i)
		// result+=threadfunct(t);
		// ������
		// lock
	}
}
