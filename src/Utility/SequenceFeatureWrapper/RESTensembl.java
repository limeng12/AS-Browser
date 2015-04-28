package Utility.SequenceFeatureWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
import Utility.Structuure.Quar;

public class RESTensembl {

	static String querl1 = "http://beta.rest.ensembl.org/lookup/symbol/homo_sapiens/";
	static String querl2 = "?content-type=text/xml";

	static String querlRegionGene = "http://beta.rest.ensembl.org/feature/region/human/";

	static String querlGeneID = "http://beta.rest.ensembl.org/feature/id/";// ENST00000288602?feature=gene;content-type=text/xml;

	public static Quar<String, String, Integer, Integer> getEnsembleGeneNameUsingGenericGeneName(
			String geneName) {
		String querl = querl1 + geneName + querl2;

		String result = REST.getResultLoop(querl);

		// System.out.println(result);
		return passXML(result);
	}

	public static Quar<String, String, Integer, Integer> passXML(String xml) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;

		Quar<String, String, Integer, Integer> geneInformation = new Quar<String, String, Integer, Integer>();

		try {
			db = dbf.newDocumentBuilder();

			Document doc;
			doc = db.parse(new InputSource(new ByteArrayInputStream(xml
					.getBytes("utf-8"))));

			// System.out.println(x);
			// MDebug.output(xml);

			NodeList rootList;

			doc.getDocumentElement().normalize();
			rootList = doc.getElementsByTagName("data");
			for (int i = 0; i < rootList.getLength(); ++i) {
				Node tmpNode = rootList.item(i);
				Element e = (Element) tmpNode;
				String name = e.getAttribute("id");
				String chr = e.getAttribute("seq_region_name");
				int start = Integer.parseInt(e.getAttribute("start"));
				int end = Integer.parseInt(e.getAttribute("end"));

				geneInformation.setValue1(name);
				geneInformation.setValue2(chr);

				geneInformation.setValue3(start);
				geneInformation.setValue4(end);

			}

		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return geneInformation;
	}

	public static Quar<String, String, Integer, Integer> getGeneName(
			String chr, int beg, int end) {
		String querl = querlRegionGene + chr + ":" + beg + "-" + end
				+ "?feature=gene;content-type=text/xml";

		String xml = REST.getResultLoop(querl);

		Quar<String, String, Integer, Integer> geneName = passXMLGene(xml);

		return geneName;
	}

	private static Quar<String, String, Integer, Integer> passXMLGene(String xml) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;

		Quar<String, String, Integer, Integer> geneInformation = new Quar<String, String, Integer, Integer>();

		try {
			db = dbf.newDocumentBuilder();

			Document doc;
			doc = db.parse(new InputSource(new ByteArrayInputStream(xml
					.getBytes("utf-8"))));

			// System.out.println(x);
			// MDebug.output(xml);

			NodeList rootList;

			doc.getDocumentElement().normalize();
			rootList = doc.getElementsByTagName("data");
			for (int i = 0; i < 1; ++i) {
				Node tmpNode = rootList.item(i);
				Element e = (Element) tmpNode;
				String name = e.getAttribute("ID");
				String chr = e.getAttribute("seq_region_name");
				int start = Integer.parseInt(e.getAttribute("start"));
				int end = Integer.parseInt(e.getAttribute("end"));

				geneInformation.setValue1(name);
				geneInformation.setValue2(chr);

				geneInformation.setValue3(start);
				geneInformation.setValue4(end);

			}

		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return geneInformation;
	}

	public static String getGFF(String transcriptId) {

		String querlBeg = "http://beta.rest.ensembl.org/feature/id/";
		String querlEnd = "?feature=cds;content-type=text/x-gff3";
		String querl = querlBeg + transcriptId + querlEnd;

		String result = REST.getResultLoop(querl);
		return result;
	}

	public static String convertTransciptIDToGeneID(String transciptID) {
		querlGeneID = querlGeneID + transciptID
				+ "?feature=gene;content-type=text/xml";
		String xml = REST.getResultLoop(querlGeneID);

		return passXMLGene(xml).getValue1();

	}

	public static void main(String[] args) {

		convertTransciptIDToGeneID("ENST00000455263");

		System.out.println(getGFF("ENST00000455263"));
		// Quar<String,String,Integer,Integer>
		// s=getEnsembleGeneNameUsingGenericGeneName("CLIC2");
		/*
		 * System.out.println(s.getValue1()); System.out.println(s.getValue2());
		 * System.out.println(s.getValue3()); System.out.println(s.getValue4());
		 */
		/*
		 * Quar<String, String, Integer, Integer> s = getGeneName("17",
		 * 7565097,7590856); System.out.println(s.getValue1());
		 * System.out.println(s.getValue2()); System.out.println(s.getValue3());
		 * System.out.println(s.getValue4()); Quar<String, String, Integer,
		 * Integer> s=getGeneName("9",45675583,45675715);
		 * System.out.println(s.getValue1()); System.out.println(s.getValue2());
		 * System.out.println(s.getValue3()); System.out.println(s.getValue4());
		 */

	}

}
