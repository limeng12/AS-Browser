package Utility.ProteinFeatureWrapper;

import Utility.ASDebug;
import Utility.Algorithm.ASRandom;
import Utility.Algorithm.REST;
import Utility.Structuure.ConfigureASEvent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class PfamEngine {
	
	private static boolean ifConnect;
	
	private static Connection conn;

	public PfamEngine() {
		super();
	} /*
	 * pfam.
	 */
	
	public static void connect() {
		if (ifConnect)
			return;

		try {
			conn = DriverManager
					.getConnection("jdbc:mysql://191.101.1.231/pfam?"
							+ "user=limeng&password=6231498");

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		ifConnect = false;

	}
	
	
	
	static String serverString1 = "http://pfam.sanger.ac.uk/search/sequence";
	static String serverString2 = "http://pfam.janelia.org/search/sequence";
	static String pfamDescriptServerString = "http://pfam.sanger.ac.uk/family/";// ?output=xml"

	static boolean server = true;

	static String serverString = "";

	static String quer;

	
	private static String getDescriptionDatabase(String pfamId){
		connect();
		
		String des = "";

		String sqlStr = "select description from pfama where pfamA_acc=" + "'"
				+ pfamId + "'";
		Statement stmt;
		try {
			stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(sqlStr);
			while (rset.next()) {
				des = rset.getString("description");

			}
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return des;
		
	}

	public static String getDescription(String pfamId) {
		String querl = pfamDescriptServerString + pfamId + "?output=xml";

		String xml = REST.getResultLoop(querl);
		return passXMLDescription(xml);

	}

	private static String passXMLDescription(String x) {
		// TODO Auto-generated method stub
		String des = "";
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			// String result = "";
			try {
				db = dbf.newDocumentBuilder();

				Document doc;

				doc = db.parse(new InputSource(new ByteArrayInputStream(x
						.getBytes("utf-8"))));

				// System.out.println(x);
				ASDebug.output(x);

				NodeList rootList;

				doc.getDocumentElement().normalize();
				rootList = doc.getElementsByTagName("description");
				// result=rootList.item(0).getNodeValue();
				for (int i = 0; i < rootList.getLength(); ++i) {
					Node tmpNode = rootList.item(i);
					Element e = (Element) tmpNode;
					des = e.getTextContent();
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return des;
	}

	public static ArrayList<String> getPfamLocally(String seq) {
		ArrayList<String> pfamEntry = new ArrayList<String>();

		String pfamScan = ConfigureASEvent.configurietion.get("pfamScan");
		String pfamDir = ConfigureASEvent.configurietion.get("pfamA");

		String path = pfamScan + ASRandom.randomCharGenerator(50);
		try {

			FileWriter fw;

			fw = new FileWriter(path, true);

			PrintWriter pw = new PrintWriter(fw);
			pw.println(">hi");
			pw.println(seq);

			pw.close();
			fw.close();

			String command = "/usr/bin/perl -I " + pfamScan + " " + pfamScan
					+ "pfam_scan.pl -fasta " + path + " -dir " + pfamDir;// +" -outfile "+path+".txt";
			Runtime run = Runtime.getRuntime();
			ASDebug.output(command);
			// Process

			Process p = run.exec(command);
			// System.out.println(command);

			// Thread.sleep(1000 * 1);

			int times = 0; // becareful here
			// p.getErrorStream();

			while (p.getInputStream().available() <= 0) {
				Thread.sleep(1000 * 1);

				times++;
				if (times > 10000)
					break;
			}			
			
			String lineStr;


			// path + ".txt"
			//FileInputStream fis = new FileInputStream(p.getErrorStream(path +".txt"));
			BufferedInputStream error=new BufferedInputStream(p.getErrorStream());
			BufferedReader errorBr = new BufferedReader(new InputStreamReader(error));
			while((lineStr=errorBr.readLine())!=null){
				System.out.println(lineStr);
				
			}
			
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());

			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

			while ((lineStr = inBr.readLine()) != null) {
				// System.out.println(lineStr);
				if (lineStr.startsWith("#"))
					continue;

				if (lineStr.length() < 40) {
					// System.out.println(lineStr);
					continue;

				}

				// System.out.println(lineStr);
				// System.out.println(lineStr);

				String[] arrLine = lineStr.split(" ");
				ArrayList<String> line = new ArrayList<String>();

				// int index=0;
				for (int i = 0; i < arrLine.length; ++i) {
					if (arrLine[i].length() == 0) {

					} else {
						line.add(arrLine[i]);
						
						// index++;
						

					}

				}

				int begPos = Integer.parseInt(line.get(1));
				int endPos = Integer.parseInt(line.get(2));
//line.set(line.size()-1,arrLine[i]+"\t"+getDescriptionDatabase(arrLine[i-1].trim().split(".")[0]));
				String pfamacc=line.get(5).substring(0, 7).trim();
				String family = line.get(5);
				String name = line.get(6)+"\t"+getDescriptionDatabase(pfamacc);

				String oneEntry = begPos + "&" + endPos + "&" + family + "&"
						+ name;

				pfamEntry.add(oneEntry);

			}

			File file = new File(path);
			file.delete();
			file.deleteOnExit();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return pfamEntry;

	}

	public static ArrayList<String> getProteinXML(String proteinSequence) {
		// String qstr = serverString+"ce6/dna?segment=chrII:1,100000";

		ArrayList<String> proteinFam = new ArrayList<String>();
		if (proteinSequence.contains("*"))
			return proteinFam;

		quer = serverString1 + "?seq=" + proteinSequence + "&output=xml";

		String x = REST.getResultLoop(quer);
		// System.out.println(x);
		ASDebug.output(x);

		if (!x.startsWith("<?xml"))
			return proteinFam;

		// int beg=x.indexOf("<result_url>");
		// int end=x.indexOf("</result_url>");
		// quer=x.substring(beg+12, end);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();

			// System.out.println(x);
			Document doc = db.parse(new InputSource(new ByteArrayInputStream(x
					.getBytes("utf-8"))));

			NodeList rootList;

			doc.getDocumentElement().normalize();
			rootList = doc.getElementsByTagName("result_url");

			// quer=rootList.item(0).getNodeValue();
			// quer=rootList.item(0).getNodeName();
			quer = rootList.item(0).getTextContent();

		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		ASDebug.output(quer);

		String hmmValue = REST.getResultLoop(quer);

		// proteinFam=passHTML(x);
		proteinFam = passXML(hmmValue);

		return proteinFam;

	}

	static ArrayList<String> passXML(String x) {
		ArrayList<String> pfamEntry = new ArrayList<String>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		// String result = "";
		try {
			db = dbf.newDocumentBuilder();

			Document doc = db.parse(new InputSource(new ByteArrayInputStream(x
					.getBytes("utf-8"))));
			// System.out.println(x);
			ASDebug.output(x);

			NodeList rootList;

			doc.getDocumentElement().normalize();
			rootList = doc.getElementsByTagName("match");
			// result=rootList.item(0).getNodeValue();
			for (int i = 0; i < rootList.getLength(); ++i) {
				Node tmpNode = rootList.item(i);
				Element e = (Element) tmpNode;
				// String pfamDomain = e.getAttribute("accession") + "\t"
				// + e.getAttribute("id") + "\t" + e.getAttribute("type")
				// + "\t" +
				// e.getAttribute("class")+"&"+getDescription(e.getAttribute("id"));
				String pfamDomain = e.getAttribute("accession") + "&"
						+ getDescription(e.getAttribute("id"));

				NodeList mathChild = e.getChildNodes();

				for (int g = 0; g < mathChild.getLength(); ++g) {
					Node t = mathChild.item(g);
					String nodeName = t.getNodeName();
					if (nodeName.equals("location")) {
						Element location = (Element) t;

						String start = location.getAttribute("start");
						String end = location.getAttribute("end");

						pfamEntry.add(start + "&" + end + "&" + pfamDomain);

						// break;
					}
				}

				// result = e.getAttribute("id");
				// result = e.getAttribute("type");
			}

		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return pfamEntry;
	}

	public static void main(String args[]) throws IOException,
			InterruptedException {
		// DNASequence x=new
		// DNASequence("cgatgcatcgtatcgtggggggggggggggggatcagctagctacgtacgtacgtacgtac");
		// System.out.println(SearchProteinUsingRest.getProteinXML(x.getRNASequence().getProteinSequence()));
		System.out.println("meng");
		ConfigureASEvent
				.readConfigureFromFile("/home/limeng/alternative/Configure.txt");

		ArrayList<String> t = PfamEngine
				.getPfamLocally("MSQWYELQQLDSKFLEQVHQLYDDSFPMEIRQYLAQWLEKQDWEHAANDVSFATIRFHDLLSQLDDQYSRFSLENNFLLQHNIRKSKRNLQDNFQEDPIQMSMIIYSCLKEERKILENAQRFNQAQSGNIQSTVMLDKQKELDSKVRNVKDKVMCIEHEIKSLEDLQDEYDFKCKTLQNREHETNGVAKSDQKQEQLLLKKMYLMLDNKRKEVVHKIIELLNVTELTQNALINDELVEWKRRQQSACIGGPPNACLDQLQNWFTIVAESLQQVRQQLKKLEELEQKYTYEHDPITKNKQVLWDRTFSLFQQLIQSSFVVERQPCMPTHPQRPLVLKTGVQFTVKLRLLVKLQELNYNLKVKVLFDKDVNERNTVKGFRKFNILGTHTKVMNMEESTNGSLAAEFRHLQLKEQKNAGTRTNEGPLIVTEELHSLSFETQLCQPGLVIDLETTSLPVVVISNVSQLPSGWASILWYNMLVAEPRNLSFFLTPPCARWAQLSEVLSWQFSSVTKRGLNVDQLNMLGEKLLGPNASPDGLIPWTRFCKENINDKNFPFWLWIESILELIKKHLLPLWNDGCIMGFISKERERALLKDQQPGTFLLRFSESSREGAITFTWVERSQNGGEPDFHAVEPYTKKELSAVTFPDIIRNYKVMAAENIPENPLKYLYPNIDKDHAFGKYYSRPKEAPEPMELDGPKGTGYIKTELISVSEV");
		for (String ite : t) {
			System.out.println(ite);
		}

	}

}
