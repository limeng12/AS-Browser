package Test;

import java.io.File;

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

import ASEvent.ASEventMiso;
import ASEvent.ASevent;
import Utility.ASDebug;
import Utility.Structuure.ConfigureASEvent;

public class TestInputMiso {
	void test(String eventId) {

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

		ASevent p = new ASEventMiso(eventId, "hg19");
		p.init();
		//String ensemblID = p.id;
		//Element eventNode = 
		p.processXML(doc, tableRoot);

		// GetTranscirpts t=new GetTranscirpts();
		// t.getTranscripts(ensemblID, "hg19");
		// t.processXMLTranscripts(doc,eventNode);

		// table.add(p);
		outputXML(doc, "/home/limeng/alternative/browserhome/" + "testMiso" + ".xml");
		ASDebug.close();

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

	public static void run(String[] args) {
		// args[0]="ENST00000413465@2:3";
		// args[1]="/home/limeng/software/alternativeEngine/Configure.txt";

		// String configureFileName = args[1];

		ConfigureASEvent.readConfigureFromFile(args[1]);

		TestInputMiso t = new TestInputMiso();

		t.test(args[0]);

	}

	public static void main(String[] args) {
		// run(args);

		String configureFileName = "/home/limeng/alternative/Configure.txt";

		ConfigureASEvent.readConfigureFromFile(configureFileName);

		TestInputMiso t = new TestInputMiso();

		t.test("chr19:54780155:54780101|54780118:-@chr19:54779805:54779857:-");

	}

}
