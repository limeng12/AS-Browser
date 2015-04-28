package Test;

import java.io.File;
import java.util.ArrayList;

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

import Utility.Structuure.ConfigureASEvent;
import ASEvent.ASEventBrowserExons;
import ASEvent.ASevent;

public class TestInputExons {

	void test() {

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

		ArrayList<ASevent> table = new ArrayList<ASevent>();

		try {

			ASevent p = new ASEventBrowserExons(
					"chr17:7572927:7573008:-@chr17:7573927:7574033:-:Splice@chr17:7576853:7576926:-@chr17:7577019:7577155:-:Splice@chr17:7577499:7577608:-@chr17:7578177:7578289:-@chr17:7578371:7578544:-@chr17:7579312:7579590:-@chr17:7579700:7579721:-@chr17:7579839:7579912:-",
					"hg19");
			p.init();
			p.processXML(doc, tableRoot);

			table.add(p);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());

		}

		outputXML(doc, "all.xml");

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

	public static void main(String[] args) {
		String configureFileName = "/home/limeng/alternative/Configure.txt";

		ConfigureASEvent.readConfigureFromFile(configureFileName);

		TestInputExons t = new TestInputExons();
		t.test();

	}

}
