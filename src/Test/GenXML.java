package Test;

import java.io.File;
import java.util.regex.Pattern;

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

import Utility.ASDebug;
import Utility.Structuure.ConfigureASEvent;
import ASEvent.ASEventBrowserExons;
import ASEvent.ASEventBrowserTranscriptID;
import ASEvent.ASEventMiso;
import ASEvent.ASevent;
import ASEvent.GetTranscirpts;

public class GenXML {

	static boolean isMiso(String input) {
		if (Pattern.matches(ASEventMiso.regexCasseteExon,
				input.subSequence(0, input.length())))
			return true;
		else if (Pattern.matches(ASEventMiso.regexA5SS,
				input.subSequence(0, input.length())))
			return true;
		else if (Pattern.matches(ASEventMiso.regexA3SS,
				input.subSequence(0, input.length())))
			return true;
		else if (Pattern.matches(ASEventMiso.regexRetainedIntro,
				input.subSequence(0, input.length())))
			return true;

		return false;

	}

	static boolean isID(String input) {
		if (input.startsWith("ENST"))
			return true;
		if (input.startsWith("uc"))
			return true;
		if (input.startsWith("NM"))
			return true;

		return false;
	}

	static void test(String eventId,String fileName) {		
		//eventId.replace(" ", "+");
		
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
		// String eventId="ENST00000392322@8";

		if (isID(eventId)) {
			ASevent p = new ASEventBrowserTranscriptID(eventId, "hg19");
			p.init();
			String ensemblID = p.id;
			// ASDebug.output("1");
			Element eventNode = p.processXML(doc, tableRoot);

			GetTranscirpts t = new GetTranscirpts();
			t.getTranscripts(ensemblID, "hg19");
			t.processXMLTranscripts(doc, eventNode);
		} else if (isMiso(eventId)) {
			ASevent p = new ASEventMiso(eventId, "hg19");
			p.init();
			p.processXML(doc, tableRoot);

		} else {
			ASevent p = new ASEventBrowserExons(eventId,"hg19");
			p.init();
			p.processXML(doc, tableRoot);

		}
		
		outputXML(doc, "/var/www/browserhome/userXML/" + fileName + ".xml");
		ASDebug.close();

	}

	static void outputXML(Document doc, String fileName) {
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

	public static void run(String[] args) {

		ConfigureASEvent.readConfigureFromFile(args[1]);

		test(args[0],args[2]);

	}

	public static void main(String[] args) {
		run(args);

		// String configureFileName = "/home/limeng/alternative/Configure.txt";

		// ConfigureASEvent.readConfigureFromFile(configureFileName);

		// test("ENST00000510385@6","ENST00000510385@6");

	}

}
