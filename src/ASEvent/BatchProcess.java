package ASEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

import Utility.ASDebug;
import Utility.Structuure.ConfigureASEvent;

public class BatchProcess {
	
	BatchProcess() {

	}

	private ArrayList<String> pos = new ArrayList<String>();

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

	void batchProcessed(String organism, String outputFileName)
			throws ParserConfigurationException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;

		PrintWriter pw = new PrintWriter(new FileWriter("debug.txt"));

		db = dbf.newDocumentBuilder();

		// System.out.println(x);
		Document doc = db.newDocument();

		// Document doc = db.parse(new File("meng.xml"));
		Element tableRoot = doc.createElement("root");
		doc.appendChild(tableRoot);

		ArrayList<ASevent> table = new ArrayList<ASevent>();

		for (int i = 0; i < pos.size(); ++i) {
			try {
				ASDebug.output("current position=" + pos.get(i));

				ASevent p = new ASEventMiso(pos.get(i), organism,true);
				p.init();
				p.processXML(doc, tableRoot);

				table.add(p);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println(e.getStackTrace());

				pw.println(pos.get(i));
			}

		}
		if (outputFileName.length() == 0)
			outputXML(doc, "all.xml");
		else
			outputXML(doc, outputFileName);

		pw.flush();
		pw.close();

	}

	@SuppressWarnings("resource")
	void readPositionsFile(String fileName) throws IOException {
		File file = new File(fileName);
		BufferedReader input;
			input = new BufferedReader(new FileReader(file));

			String text;

			while ((text = input.readLine()) != null)
				pos.add(text);

	}

	public static void main(String[] args) {
		String configureFileName = "/home/limeng/alternative/Configure.txt";
		String org = "hg19";
		String fileName = "/home/limeng/alternative/zhouao-6-6/sample.txt";

		// String configureFileName=args[0];
		// String org=args[1];
		// String fileName=args[2];

		ConfigureASEvent.readConfigureFromFile(configureFileName);
		BatchProcess table = new BatchProcess();

		try {
			table.readPositionsFile(fileName);

			table.batchProcessed(org,
					"/home/limeng/alternative/zhouao-6-6/AS.hg19.short-event.xml");

		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
}
