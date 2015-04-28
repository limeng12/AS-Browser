package Utility.ProteinFeatureWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import Utility.Algorithm.REST;
import Utility.Structuure.Pair;
import Utility.Structuure.Quar;

public class RESTUniprot {
	private static final String UNIPROT_SERVER = "http://www.uniprot.org/";
	private static final Logger LOG = Logger.getAnonymousLogger();

	private static String run(String tool, ParameterNameValue[] params)
			throws Exception {
		StringBuilder locationBuilder = new StringBuilder(UNIPROT_SERVER + tool
				+ "/?");
		for (int i = 0; i < params.length; i++) {
			if (i > 0)
				locationBuilder.append('&');
			locationBuilder.append(params[i].name).append('=')
					.append(params[i].value);
		}
		String location = locationBuilder.toString();
		String s = REST.getResultLoop(location);
		// System.out.println(s);
		return s;
	}

	public static ArrayList<String> convertEnsembleToUniprot(
			String ensembleGeneName) {
		ArrayList<String> ids = new ArrayList<String>();
		try {
			String result = run("mapping", new ParameterNameValue[] {
					new ParameterNameValue("from", "ENSEMBL_ID"),
					new ParameterNameValue("to", "ACC"),
					new ParameterNameValue("format", "tab"),
					new ParameterNameValue("query", ensembleGeneName), });

			String[] lines = result.split("\n", 20);
			for (int i = 0; i < lines.length; ++i) {
				if (i == 0)
					continue;
				String[] line = lines[i].split(" ", 20);
				ids.add(line[1]);

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;
	}

	public static String convertEnsembleProteinToUniprot(
			String ensembleProteinId) {
		String id = "";
		try {
			String result = run("mapping", new ParameterNameValue[] {
					new ParameterNameValue("from", "ENSEMBL_PRO_ID"),
					new ParameterNameValue("to", "ACC"),
					new ParameterNameValue("format", "tab"),
					new ParameterNameValue("query", ensembleProteinId), });

			String[] lines = result.split("\n", 20);
			for (int i = 0; i < lines.length; ++i) {
				if (i == 0)
					continue;
				if (lines[i].length() < 1)
					continue;
				String[] line = lines[i].split("\t", 20);
				id = (line[1]);

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return id;
	}

	public static ArrayList<String> convertUniprotIdToPDBID(String uniprotId) {
		ArrayList<String> ids = new ArrayList<String>();
		try {
			String result = run("mapping", new ParameterNameValue[] {
					new ParameterNameValue("from", "ACC"),
					new ParameterNameValue("to", "PDB_ID"),
					new ParameterNameValue("format", "tab"),
					new ParameterNameValue("query", uniprotId), });

			String[] lines = result.split("\n", 20);
			for (int i = 0; i < lines.length; ++i) {
				if (i == 0)
					continue;
				if (lines[i].length() < 1)
					continue;
				String[] line = lines[i].split("\t", 20);
				ids.add(line[1]);

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;
	}

	public static ArrayList<String> convertEnsembleGeneIDToUniprotIDs(
			String geneEnsembleId) {
		ArrayList<String> uniprotIDs = new ArrayList<String>();

		try {
			String result = run("mapping", new ParameterNameValue[] {
					new ParameterNameValue("from", "ENSEMBL_ID"),
					new ParameterNameValue("to", "ACC"),
					new ParameterNameValue("format", "tab"),
					new ParameterNameValue("query", geneEnsembleId), });

			String[] lines = result.split("\n", 20);
			for (int i = 0; i < lines.length; ++i) {
				if (i == 0)
					continue;
				if (lines[i].length() < 1)
					continue;
				String[] line = lines[i].split("\t", 20);
				String id = (line[1]);
				uniprotIDs.add(id);

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return uniprotIDs;

	}

	public static ArrayList<String> convertEnsembleProteinIDToPDBId(
			String ensembleId) {
		ArrayList<String> pdbIds = new ArrayList<String>();
		String t = convertEnsembleProteinToUniprot(ensembleId);

		// for (int i = 0; i < t.size(); ++i) {
		pdbIds.addAll(convertUniprotIdToPDBID(t));
		// }

		return pdbIds;

	}

	public static String fetchUniprotXML(String uniprotID) {
		String querl = UNIPROT_SERVER + "uniprot/" + uniprotID + ".xml";
		String result = REST.getResultLoop(querl);

		return result;

	}

	public static ArrayList<Pair<Integer, String>> getPTMsUingUniprot(
			String uniprotID) {
		String xml = fetchUniprotXML(uniprotID);

		return getPTMsUingUniprotXML(xml);

	}

	public static String getSequenceUniprot(String uniprotID) {
		String fasta = fetchUniprotFasta(uniprotID);

		return passFaste(fasta);

	}

	private static String fetchUniprotFasta(String uniprotID) {
		// TODO Auto-generated method stub
		String querl = UNIPROT_SERVER + "uniprot/" + uniprotID + ".fasta";
		String result = REST.getResultLoop(querl);

		return result;
	}

	private static String passFaste(String fasta) {
		// TODO Auto-generated method stub
		String sequence = "";

		String[] lines = fasta.split("\\n");
		for (int i = 0; i < lines.length; ++i) {
			if (lines[i].startsWith(">"))
				continue;

			sequence += lines[i];

		}

		return sequence;

	}

	public static ArrayList<Pair<Integer, String>> getPTMsUingUniprotXML(
			String xml) {
		ArrayList<Pair<Integer, String>> ptms = new ArrayList<Pair<Integer, String>>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		String result = "";
		try {
			db = dbf.newDocumentBuilder();

			Document doc = db.parse(new InputSource(new ByteArrayInputStream(
					xml.getBytes("utf-8"))));
			// System.out.println(x);
			// MDebug.output(xml);

			NodeList rootList;

			doc.getDocumentElement().normalize();
			rootList = doc.getElementsByTagName("entry");
			Node tmpNode = rootList.item(0);
			Element e = (Element) tmpNode;
			if (e.getAttribute("dataset").equals("TrEMBL")) {
				return ptms;
			}

			rootList = doc.getElementsByTagName("feature");
			for (int i = 0; i < rootList.getLength(); ++i) {
				tmpNode = rootList.item(i);
				e = (Element) tmpNode;

				String featureType = e.getAttribute("type");
				if (featureType.contains("modified residue")
						|| featureType.contains("glycosylation site")) {
					String discription = e.getAttribute("description");

					NodeList t = e.getElementsByTagName("position");
					Element t1 = (Element) t.item(0);
					t1.getAttribute("position");

					// Node t2=t.item(1);

					// Element te = (Element) (e.getFirstChild());
					String pos = t1.getAttribute("position");
					int position = Integer.parseInt(pos);

					ptms.add(new Pair<Integer, String>(position, discription));

				}

			}

		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return ptms;
	}

	private static class ParameterNameValue {
		private final String name;
		private final String value;

		public ParameterNameValue(String name, String value)
				throws UnsupportedEncodingException {
			this.name = URLEncoder.encode(name, "UTF-8");
			this.value = URLEncoder.encode(value, "UTF-8");
		}
	}

	public static ArrayList<Quar<Integer, Integer, String, String>> getPfam(
			String tuniprot) {
		String querl = UNIPROT_SERVER + "uniprot/" + tuniprot + ".xml";
		String xml = REST.getResultLoop(querl);

		return passXML(xml);

	}

	private static ArrayList<Quar<Integer, Integer, String, String>> passXML(
			String xml) {

		ArrayList<Quar<Integer, Integer, String, String>> pfams = new ArrayList<Quar<Integer, Integer, String, String>>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		String result = "";
		try {
			db = dbf.newDocumentBuilder();

			Document doc = db.parse(new InputSource(new ByteArrayInputStream(
					xml.getBytes("utf-8"))));
			// System.out.println(x);
			// MDebug.output(xml);

			NodeList rootList;

			doc.getDocumentElement().normalize();
			rootList = doc.getElementsByTagName("feature");
			for (int i = 0; i < rootList.getLength(); ++i) {
				Node tmpNode = rootList.item(i);
				Element e = (Element) tmpNode;
				String featureType = e.getAttribute("type");
				if (featureType.contains("transmembrane region")) {
					String discription = e.getAttribute("description");

					NodeList t = e.getElementsByTagName("begin");
					Element t1 = (Element) t.item(0);
					String beg = t1.getAttribute("position");

					t = e.getElementsByTagName("end");
					Element t2 = (Element) t.item(0);
					String end = t2.getAttribute("position");

					// Node t2=t.item(1);

					// Element te = (Element) (e.getFirstChild());
					// String pos = t1.getAttribute("position");
					// int position = Integer.parseInt(pos);
					int begPos = Integer.parseInt(beg);
					int endPos = Integer.parseInt(end);

					pfams.add(new Quar<Integer, Integer, String, String>(
							begPos, endPos, "transmembrane region", discription));

				}

			}

		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return pfams;
	}

	public static void main(String[] args) throws Exception {
		// String t = convertEnsembleProteinToUniprot("ENSP00000391478");
		// ArrayList<String> t2 = convertUniprotIdToPDBID("P04637");

		// ArrayList<String>
		// t=convertUcscGeneIDToUniprotIDs("uc007aeu.1");//P68510
		ArrayList<Pair<Integer, String>> s = getPTMsUingUniprot("P68510");

		// String s=getSequenceUniprot("E9PMR3");
		// System.out.println(s);
		for (Pair<Integer, String> a : s) {
			System.out.println(a.getValue1());

		}

		// System.out.println(t.get(0).getValue1());
		// System.out.println(t.get(0).getValue2());

		// System.out.println(t2.get(0));

	}

	public static ArrayList<String> convertUcscGeneIDToUniprotIDs(
			String unscGeneId) {
		// TODO Auto-generated method stub
		ArrayList<String> uniprotIDs = new ArrayList<String>();

		try {
			String result = run("mapping", new ParameterNameValue[] {
					new ParameterNameValue("from", "UCSC_ID"),
					new ParameterNameValue("to", "ACC"),
					new ParameterNameValue("format", "tab"),
					new ParameterNameValue("query", unscGeneId), });

			String[] lines = result.split("\n", 20);
			for (int i = 0; i < lines.length; ++i) {
				if (i == 0)
					continue;
				if (lines[i].length() < 1)
					continue;
				String[] line = lines[i].split("\t", 20);
				String id = (line[1]);
				uniprotIDs.add(id);

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return uniprotIDs;

	}

	public static ArrayList<String> convertEnsembleTransIDToUniprotIDs(
			String ensemblGeneID) {
		// TODO Auto-generated method stub
		ArrayList<String> uniprotIDs = new ArrayList<String>();

		try {
			String result = run("mapping", new ParameterNameValue[] {
					new ParameterNameValue("from", "ENSEMBL_TRS_ID"),
					new ParameterNameValue("to", "ACC"),
					new ParameterNameValue("format", "tab"),
					new ParameterNameValue("query", ensemblGeneID), });

			String[] lines = result.split("\n", 20);
			for (int i = 0; i < lines.length; ++i) {
				if (i == 0)
					continue;
				if (lines[i].length() < 1)
					continue;
				String[] line = lines[i].split("\t", 20);
				String id = (line[1]);
				uniprotIDs.add(id);

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return uniprotIDs;

	}

}