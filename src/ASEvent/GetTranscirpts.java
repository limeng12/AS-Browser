package ASEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import Utility.SequenceFeatureWrapper.SequenceFeatureExtractor;
import ASEvent.GTFFeature.GTFFeature;
import GeneStructure.Exon;
import GeneStructure.Transcript;

public class GetTranscirpts {

	ArrayList<Transcript> transcripts = new ArrayList<Transcript>();

	Transcript transcript = new Transcript();

	void run() {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// System.out.println(x);
		Document doc = db.newDocument();

		// Document doc = db.parse(new File("meng.xml"));
		Element tableRoot = doc.createElement("root");
		doc.appendChild(tableRoot);

		// ArrayList<ASevent> table = new ArrayList<ASevent>();

		try {
			getTranscripts("ENST00000455263", "hg19");
			HashSet<Integer> asExons = new HashSet<Integer>();
			asExons.add(2);
			asExons.add(3);
			getTranscript("ENST00000455263", "hg19", asExons);

			Element eventRoot = doc.createElement("event");
			tableRoot.appendChild(eventRoot);

			processXMLTranscript(doc, eventRoot);
			processXMLTranscripts(doc, eventRoot);

			// ASevent p = new ASEventBrowserTranscriptID("ENST00000455263@2:3",
			// "hg19");
			// p.init();
			// p.processXML(doc, tableRoot);
			// table.add(p);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());

		}

		outputXML(doc, "/home/limeng/alternative/browserhome/transcript.xml");

	}

	private void processXMLTranscript(Document doc, Element tableRoot) {
		// Element eventRoot = doc.createElement("event");
		// tableRoot.appendChild(eventRoot);

		Element transcriptNode = doc.createElement("alternativeExons");

		Iterator<Exon> iteExon = transcript.getExonIterator();
		while (iteExon.hasNext()) {
			Exon exon = iteExon.next();
			Element exonNode = doc.createElement("exon");
			exonNode.setAttribute("start", "" + exon.getExonBegCoorPos());
			exonNode.setAttribute("end", "" + exon.getExonEndCoorPos());
			exonNode.setAttribute("modBy3", "" + exon.getModBy3());
			exonNode.setAttribute("IfAlternative", "" + exon.getIfAlternative());
			exonNode.setTextContent(exon.getSequenceAsString());
			transcriptNode.appendChild(exonNode);
		}

		tableRoot.appendChild(transcriptNode);

	}

	public void processXMLTranscripts(Document doc, Element tableRoot) {
		// Element eventRoot = doc.createElement("event");
		// tableRoot.appendChild(eventRoot);

		Element transcriptsNode = doc.createElement("transcripts");
		Iterator<Transcript> iteTranscript = transcripts.iterator();
		while (iteTranscript.hasNext()) {
			Transcript t = iteTranscript.next();
			Element exonsNode = doc.createElement("transcript");
			exonsNode.setAttribute("ID", t.getTranscriptName());
			
			Iterator<Exon> iteExon = t.getExonIterator();
			while (iteExon.hasNext()) {
				Exon exon = iteExon.next();
				Element exonNode = doc.createElement("exon");
				exonNode.setAttribute("start", "" + exon.getExonBegCoorPos());
				exonNode.setAttribute("end", "" + exon.getExonEndCoorPos());
				exonNode.setAttribute("modBy3", "" + exon.getModBy3());
				exonNode.setAttribute("IfAlternative",
						"" + exon.getIfAlternative());
				exonNode.setTextContent(exon.getSequenceAsString());
				exonsNode.appendChild(exonNode);
			}
			transcriptsNode.appendChild(exonsNode);

		}
		tableRoot.appendChild(transcriptsNode);

	}

	void outputXML(Document doc, String fileName) {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileName));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (TransformerException e) {
			System.out.println(e.getMessage());
		}
	}

	void getTranscript(String ensemblTranscriptID, String org,
			HashSet<Integer> asPosition) {
		ArrayList<GTFFeature> features = SequenceFeatureExtractor
				.getTranscriptsOfATranscript(ensemblTranscriptID);

		int exonIndex = 1;
		for (int i = 0; i < features.size(); ++i) {
			GTFFeature feature = features.get(i);
			// ASDebug.output(i);

			String attr = features.get(i).getAttribute();
			String[] attrLines = attr.split("\\;");
			String transcriptid = "";
			for (int j = 0; j < attrLines.length; ++j) {
				// if (attrLines[j].contains("Parent")) {
				// cds+=attLine[i];
				// transcriptid = attrLines[j].substring(7);
				if (attrLines[i].contains("transcript_id")) {
					// cds+=attLine[i];
					transcriptid = attrLines[i].substring(16, 31);

					// transcriptid =
					// transcriptid.substring(1,transcriptid.length() - 1);

					// transcriptIDs.add(t);

				}
				// cdss.add(cds);
			}
			if (transcriptid.equals(ensemblTranscriptID)) {

				Exon e = new Exon("hg19", feature.getChr(), "",
						feature.getStart(), feature.getEnd());

				if (asPosition.contains(exonIndex))
					e.setAlternative(true);

				transcript.addExon(e);

				exonIndex++;
			}

		}
	}

	public void getTranscripts(String ensemblTranscriptID, String org) {

		ArrayList<GTFFeature> features = SequenceFeatureExtractor
				.getTranscriptsOfATranscript(ensemblTranscriptID);

		HashMap<String, Integer> transcriptIDMap = new HashMap<String, Integer>();

		int transcriptIndex = 0;
		for (int i = 0; i < features.size(); ++i) {
			GTFFeature feature = features.get(i);
			// ASDebug.output(i);

			String attr = features.get(i).getAttribute();
			String[] attrLines = attr.split("\\;");
			String transcriptid = "";
			for (int j = 0; j < attrLines.length; ++j) {
				if (attrLines[j].contains("transcript_id")) {
					// cds+=attLine[i];
					transcriptid = attrLines[j].substring(16, 31);
					
				}
				
			}

			if (transcriptIDMap.containsKey(transcriptid)) {
				transcripts.get(transcriptIDMap.get(transcriptid)).addExon(
						new Exon("hg19", feature.getChr(), "", feature
								.getStart(), feature.getEnd()));

			} else {
				transcriptIDMap.put(transcriptid, transcriptIndex++);
				Transcript t = new Transcript();
				t.setTranscriptName(transcriptid);
				
				t.addExon(new Exon("hg19", feature.getChr(), "", feature
						.getStart(), feature.getEnd()));
				transcripts.add(t);

			}

		}

	}

	public static void main(String[] args) {
		GetTranscirpts a = new GetTranscirpts();
		a.run();

	}

}
