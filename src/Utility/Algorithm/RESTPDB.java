package Utility.Algorithm;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Utility.ASDebug;
import Utility.Structuure.AminoAcidMap;

public class RESTPDB {
	public RESTPDB() {
		super();
	}

	public static final String SERVICELOCATION = "http://www.rcsb.org/pdb/rest/search";

	public static final String serviceDes = "http://www.rcsb.org/pdb/rest/describePDB?structureId=";
	public static final String entityDes = "http://www.rcsb.org/pdb/rest/describeMol?structureId=";

	public static HashMap<String, String> idFiles = new HashMap<String, String>();

	public static String getDescription(String pdbId) {
		String result = "";

		String searchString = serviceDes + pdbId;
		try {
			URL url;

			url = new URL(searchString);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			// conn.
			int i = 0;
			while (conn.getResponseCode() != 200) {

				Thread.sleep(1000);

				System.out.println(conn.getResponseCode());
				System.out.println(conn.getResponseMessage());
				conn.disconnect();
				conn = (HttpURLConnection) url.openConnection();

				System.out.println(i);
				if (i > 10)
					continue;
				i++;
			}

			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
			}

			// Buffer the result into a string
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
				ASDebug.output(line);
			}
			rd.close();

			conn.disconnect();

			result = sb.toString();
			int start = result.indexOf("title");
			int end = result.indexOf("pubmedId");
			result = pdbId + "\t" + result.substring(start, end);

		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("interrupt error");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}

	public static String getEntityDescription(String pdbId) {
		String result = "";

		String searchString = entityDes + pdbId;
		try {
			URL url;

			url = new URL(searchString);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			// conn.
			int i = 0;
			while (conn.getResponseCode() != 200) {

				Thread.sleep(1000);

				ASDebug.output(conn.getResponseCode());
				ASDebug.output(conn.getResponseMessage());
				conn.disconnect();
				conn = (HttpURLConnection) url.openConnection();

				ASDebug.output(i);
				if (i > 10)
					continue;
				i++;
			}

			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
			}

			// Buffer the result into a string
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
				// System.out.println(line);
			}
			rd.close();

			conn.disconnect();

			result = sb.toString();
			int start = result.indexOf("Taxonomy name=");
			// int end=result.indexOf("pubmedId");
			int end = start + 200;
			result = result.substring(start, end);

			result = result.substring(15, result.indexOf("id") - 2);

			ASDebug.output(result);

			// result=pdbId+"\t"+result.substring(start, end);

		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("interrupt error");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}

	public static ArrayList<String> getDescription(List<String> pdbIds) {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> ids = pdbIds.iterator();
		while (ids.hasNext()) {
			result.add(getDescription(ids.next()));

		}
		return result;

	}

	/**
	 * do a POST to a URL and return the response stream for further processing
	 * elsewhere.
	 * 
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static InputStream doPOST(URL url, String data) throws IOException {

		// Send data

		URLConnection conn = url.openConnection();

		conn.setDoOutput(true);

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

		wr.write(data);
		wr.flush();

		// Get the response
		return conn.getInputStream();

	}

	/**
	 * post am XML query (PDB XML query format) to the RESTful RCSB web service
	 * 
	 * @param xml
	 * @return a list of PDB ids.
	 */
	public static ArrayList<String> postQuery(String xml) throws IOException {

		URL u = new URL(SERVICELOCATION);

		String encodedXML = URLEncoder.encode(xml, "UTF-8");

		InputStream in = doPOST(u, encodedXML);

		ArrayList<String> pdbIds = new ArrayList<String>();

		BufferedReader rd = new BufferedReader(new InputStreamReader(in));

		String line;
		while ((line = rd.readLine()) != null) {

			pdbIds.add(line);

		}
		rd.close();

		return pdbIds;

	}

	public static ArrayList<String> fetchPDBId(String nucliedSequence) {

		/*
		 * String xml = "<orgPdbCompositeQuery version=\"1.0\">" +
		 * 
		 * " <queryRefinement>" +
		 * 
		 * "  <queryRefinementLevel>0</queryRefinementLevel>" +
		 * 
		 * "  <orgPdbQuery>" +
		 * "    <queryType>org.pdb.query.simple.SequenceQuery</queryType>" +
		 * 
		 * "    <description><![CDATA[Sequence Search (Structure:Chain = 1HIV:A, Expectation Value = 10.0, Search Tool = blast)]]></description>"
		 * + "    <structureId><![CDATA[1HIV]]></structureId>" +
		 * "    <chainId><![CDATA[A]]></chainId>" +
		 * 
		 * "    <sequence><![CDATA["+proteinSeq+"]]></sequence>" +
		 * "    <eCutOff><![CDATA[10.0]]></eCutOff>" +
		 * "    <searchTool><![CDATA[blast]]></searchTool>" +
		 * 
		 * "  </orgPdbQuery>" +
		 * 
		 * " </queryRefinement>" + " <queryRefinement>" +
		 * 
		 * "  <queryRefinementLevel>1</queryRefinementLevel>" +
		 * "  <conjunctionType>and</conjunctionType>" +
		 * 
		 * "  <orgPdbQuery>" +
		 * 
		 * "    <queryType>org.pdb.query.simple.ExpTypeQuery</queryType>" +
		 * "    <description><![CDATA[Experimental Method Search : Experimental Method=X-RAY]]></description>"
		 * + "    <runtimeMilliseconds>1389</runtimeMilliseconds>" +
		 * 
		 * "    <mvStructure.expMethod.value><![CDATA[X-RAY]]></mvStructure.expMethod.value>"
		 * + "  </orgPdbQuery>" +
		 * 
		 * " </queryRefinement>" +
		 * 
		 * "</orgPdbCompositeQuery>";
		 */

		String xml = "<orgPdbQuery>\n"
				+ "\n"
				+ "<queryType>org.pdb.query.simple.BlastXQuery</queryType>\n"
				+ "\n"
				+ "<description>BLASTX Search (Sequence = ATGCCAGGGGCAGTGGAAGGCCCCAGGTGGAAGCAGGCAGAAGACATTA), Expectation Value = 10.0)</description>\n"
				+ "\n" + "<sequence>" + nucliedSequence + "</sequence>\n"
				+ "\n" + "<eCutOff>0.0001</eCutOff>\n" + "\n"
				+ "</orgPdbQuery>\n";

		// PostXMLQuery t = new PostXMLQuery();
		ArrayList<String> pdbIds = new ArrayList<String>();

		try {
			pdbIds = postQuery(xml);
			for (String string : pdbIds) {

				ASDebug.output(string);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return pdbIds;

	}

	public static String fetchPDBFile(String pdbId) {
		if (idFiles.containsKey(pdbId))
			return idFiles.get(pdbId);

		String domain = "http://www.rcsb.org/pdb/files/";
		String tail = ".pdb";
		String searchStr = domain + pdbId + tail;

		URL url;
		String pdb = new String("");
		try {
			url = new URL(searchStr);
			InputStream webIS = url.openStream();
			BufferedInputStream in = new BufferedInputStream(webIS);

			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = inBr.readLine()) != null) {
				// c = webIS.read();
				// System.out.println("==============> "+c);
				// if(c!=-1) fo.write((byte)c);
				// if(c!=-1) pdb+=(byte)c;
				pdb += (line + "\n");
				// MDebug.output(line);
			}

		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());

		} // image on server

		if (!idFiles.containsKey(pdbId))
			idFiles.put(pdbId, pdb);

		return pdb;
	}

	public static String fecthPDBSeq(String tpdbFile) {
		String aminoAcid = "";
		String[] lines = tpdbFile.split("\n");

		for (int i = 0; i < lines.length; ++i) {
			if (lines[i].startsWith("SEQRES")) {
				String line = lines[i].substring(19);
				String[] aminos = line.split(" ", 20);
				for (int j = 0; j < aminos.length; ++j) {
					if (aminos[j].length() < 3)
						continue;
					String aa = aminos[j];
					if (AminoAcidMap.haveAminoAcid(aa)) {
						aminoAcid += AminoAcidMap.getAminoAcid(aa);
					}
				}

			}

		}

		return aminoAcid;
	}

	public static ArrayList<String> fetchPDBFiles(List<String> pdbIds) {
		ArrayList<String> pdbFiles = new ArrayList<String>();

		Iterator<String> ite = pdbIds.iterator();
		while (ite.hasNext()) {
			pdbFiles.add(fetchPDBFile(ite.next()));

		}

		return pdbFiles;
	}

	public static ArrayList<String> fetchPDBFiles(String nucliedSequence) {
		return fetchPDBFiles(fetchPDBId(nucliedSequence));

	}

	// this function below doesn't work
	@SuppressWarnings("resource")
	public static String readParsePDBFile(String fileName) {
		String str = "";

		File file = new File(fileName);
		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader(file));

			String text;

			while ((text = input.readLine()) != null)
				str += text + "\\n";

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return str;
	}

	public static void main(String[] args) {
		List<String> a = fetchPDBId("ACTCTTCTGTTTAGCCACATTTGCGTAAGTATTTTCAATTTCTTTGTCTTCAGGACACGTGTGGGTAAATTCTTCACGGGCATAGGCATTGTGGAGATAACGCCAGACTCCTGAGAATTCTGCTGGAATGTCAAAGTCACGATATTTCTTGGCAGCAACTTTAATAATGTTCAGCTTGGGTAACAAGCTACAATCAGCCAGTGTTAGCTGGTCCCCATCCAAGAATAGTCTTCTGGAAACTGGGGGTTCCTCAGCACTGTCTGGATCAATTTCATCCAGAAGTGGGGTGTTTAAGTAGTCATCCAGACGCTTGAATTCTTTGAGCAGAGATTTTTCAAAATTCTTATTTGCCTCCTTTTGTGTATTCTTAATGTATGCAGAAAACTTGGCAAAGAGGTTACAGCCCACATCAAAAGACTCCTTGTACTTGGGACTCAGGTGAGGGTACCTTGGAGGAGCCAGGGTTTGTTCTAAAAACTCCTCAATTTTAATGAAGTCTGTTTTCAACTCCTTGTTATACACCAGGAACGGAGGATTGGTACCTGGGGCTAAGTCCTTTAGTTCTTCAGGCTTTCTGGTCATGTCAACAGTTGTCACATTAAATTTAACTCCTTTAAGCCAGAGGATCATGAAAAGGCGTTGGCAAAAGGGACAGTTTCCAATACTCTCTCCATCACTTCCAGCCTTTACAAAAAGCTCAATCTCAGGGTCCACTTGAGTGCCGGGCCGCAGGCCTGACAT");
		// System.out.println(fecthPDBSeq(a.get(0)));
		// fetchPDBFile("3KZ4");

		// System.out.println(getDescription("1CLM"));
		Iterator<String> ite = a.iterator();
		while (ite.hasNext()) {
			System.out.println(ite.next());
		}
		// System.out.println(readParsePDBFile("F:\\AS-pipeline\\ASproject\\ASHtmlpageNotServer\\1CLM.pdb\\pdb1clm.ent"));

	}

}
