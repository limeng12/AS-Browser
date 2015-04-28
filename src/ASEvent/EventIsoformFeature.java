package ASEvent;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import GeneStructure.Frame;
import GeneStructure.Strand;
import Utility.ProteinFeatureWrapper.ProteinFeatureExtractor;
import Utility.Structuure.DISTYPE;
import Utility.Structuure.Pair;
import Utility.Structuure.Quar;

public class EventIsoformFeature {
	
	private Pair<Integer, Integer> asPosition=new Pair<Integer,Integer>(-1,-1);

	public EventIsoformFeature(String tproteinSequence, Frame tframe,
			String tchr) {
		chr = tchr;

		proteinSequence = tproteinSequence;
		proteinSequenceSeq = proteinSequence;

		frame = tframe;
		init();

	}
	public EventIsoformFeature(String tproteinSequence,String tchr) {

		// transcriptBegPos=ttranscriptBegPos;
		// transcriptEndPos=ttranscriptEndPos;
		
		
		chr = tchr;

		proteinSequence = tproteinSequence;
		proteinSequenceSeq = proteinSequence;

		//asPosition.setValue1(tasPosition.getValue1());
		//asPosition.setValue2(tasPosition.getValue2());

		// frame = tframe;
		init();

	}
	public EventIsoformFeature(){
		
	}

	public EventIsoformFeature(String tproteinSequence, Frame tframe,String tchr,Pair<Integer,Integer> tasPosition) {

		// transcriptBegPos=ttranscriptBegPos;
		// transcriptEndPos=ttranscriptEndPos;
		
		
		chr = tchr;

		proteinSequence = tproteinSequence;
		proteinSequenceSeq = proteinSequence;

		asPosition.setValue1(tasPosition.getValue1());
		asPosition.setValue2(tasPosition.getValue2());

		// frame = tframe;
		init();

	}

	String chr;

	String proteinSequence;
	String proteinSequenceSeq;
	
	// alternative splicing information
	Frame frame = Frame.UNKNOWN;

	// orf information
	int orf = -1;

	private ArrayList<Quar<Integer, Integer, String, String>> pfamInfo = new ArrayList<Quar<Integer, Integer, String, String>>();

	// disprot information
	ArrayList<Double> disprotValue = new ArrayList<Double>();

	// disprot String
	String disprotStr = "";

	// alternative splicing disorder type
	DISTYPE distype = DISTYPE.UNKNOWN;
	
	DISTYPE distypeInASRegion=DISTYPE.UNKNOWN;

	// ptm information
	ArrayList<String> ptms = new ArrayList<String>();

	ArrayList<Pair<Integer, String>> uniPTMs = new ArrayList<Pair<Integer, String>>();

	private void init() {
		// TODO Auto-generated method stub
		pfamInfo = ProteinFeatureExtractor.getPfamQuar(proteinSequenceSeq,
				Strand.POSITIVE);

		disprotStr = ProteinFeatureExtractor.getDisProbability(
				proteinSequenceSeq, Strand.POSITIVE, disprotValue);
		distype = getDisorderType();
		distypeInASRegion=getDisorderTypeInASRegion();
		
		// pfamInfo=ProteinFeatureExtractor.
		// ptms = ProteinFeatureExtractor.getPTMs(proteinSequenceSeq);
		// ptms = modifyPtm(ptms);
		// uniPTMs=ProteinFeatureExtractor.getPTMUsingGeneName(chr,transcriptBegPos,transcriptEndPos,proteinSequenceSeq);

		// uniPTMs=modifyUniprotPtm(uniPTMs);
	}

	private ArrayList<Pair<Integer, String>> modifyUniprotPtm(
			ArrayList<Pair<Integer, String>> uniPTMs2) {

		ArrayList<Pair<Integer, String>> uniPTMInfo = new ArrayList<Pair<Integer, String>>();
		// TODO Auto-generated method stub
		for (Pair<Integer, String> a : uniPTMs) {
			int p = a.getValue1();

			uniPTMInfo.add(new Pair<Integer, String>(p, a.getValue2()));

		}

		return uniPTMInfo;
	}

	private ArrayList<String> modifyPtm(ArrayList<String> tptms) {

		// int asProteinBegPos = asPosition.getValue1();
		// int asProteinEndPos = asPosition.getValue2();

		for (int i = 0; i < tptms.size(); ++i) {
			String[] re = tptms.get(i).split("\\t");
			String posStr = re[0].substring(1);
			int pos = Integer.parseInt(posStr);
			// if (pos >= asProteinBegPos && pos <= asProteinEndPos)
			// tptms.set(i, tptms.get(i) + " In AS region ");

		}

		return tptms;
		// modPreds.add(ptms);
		// k++;
	}

	private DISTYPE getDisorderTypeInASRegion(){
		int asProteinBegPos = asPosition.getValue1();
		int asProteinEndPos = asPosition.getValue2();
		if((asProteinBegPos-1<0)||(asProteinBegPos-1>=asProteinEndPos-1))
		
			return DISTYPE.UNKNOWN;
					
		String asDomain = disprotStr.substring(asProteinBegPos - 1,
		asProteinEndPos - 1); // -1 convert to 0-based

		if (!asDomain.contains("o") && !asDomain.contains("."))
			return DISTYPE.FULLDISORDER; // full disorder
		else if (!asDomain.contains("D"))
			return DISTYPE.NODISORDER; // no disorder
		else
			return DISTYPE.PARTDISORDER; // partial disorder
		
	}
	
	private DISTYPE getDisorderType() {

		//return DISTYPE.UNKNOWN;

		 if (!disprotStr.contains("o") && !disprotStr.contains("."))
			 return DISTYPE.FULLDISORDER; // full disorder 
		 else if(!disprotStr.contains("D"))
			 return DISTYPE.NODISORDER; // no disorder
		 else 
			 return DISTYPE.PARTDISORDER; //partial disorder
		
	}

	public void processXMLProtein(Document doc, Element tableRoot,
			Element eventNode, int i) {
		Element proteinNode = doc.createElement("isoform");
		eventNode.appendChild(proteinNode);

		Element proteinSeqNode = doc.createElement("proteinSequence");

		proteinSeqNode.setTextContent(proteinSequenceSeq);
		proteinNode.appendChild(proteinSeqNode);

		Element proteinFrameNode = doc.createElement("frame");
		proteinFrameNode.setTextContent(frame.ordinal() + " ");
		proteinNode.appendChild(proteinFrameNode);

		// Element proteinOrfNode = doc.createElement("orf");
		// proteinOrfNode.setTextContent(frame.ordinal()+" ");
		// proteinNode.appendChild(proteinOrfNode);

		// Element proteinSequenceNode = doc.createElement("proteinSequence");
		// proteinSequenceNode.setTextContent(proteinSequenceSeq);
		// proteinNode.appendChild(proteinSequenceNode);
		Element asRegionNode=doc.createElement("asRegion");
		asRegionNode.setAttribute("start",""+ asPosition.getValue1());
		asRegionNode.setAttribute("end", ""+asPosition.getValue2());
		//asRegionNode.setTextContent("start="+asPosition.getValue1()+","+"end="+asPosition.getValue2());
		proteinNode.appendChild(asRegionNode);

		// pfam
		Element pfamsNode = doc.createElement("pfam_domains");
		for (Quar<Integer, Integer, String, String> itePfam : pfamInfo) {

			Element pfamNode = doc.createElement("pfam_domain");
			// p.setTextContent(pfamInfo.get(j));
			pfamNode.setAttribute("start", "" + itePfam.getValue1());
			pfamNode.setAttribute("end", "" + itePfam.getValue2());
			pfamNode.setAttribute("family", "" + itePfam.getValue3());
			pfamNode.setAttribute("description", itePfam.getValue4());
			pfamsNode.appendChild(pfamNode);

		}
		proteinNode.appendChild(pfamsNode);

		Element disValueNode = doc.createElement("disProt_value");
		String result = "";
		for (int j = 0; j < disprotValue.size(); ++j) {

			result += disprotValue.get(j);

			if (disprotValue.size() - 1 != j)
				result += " ";
		}

		disValueNode.setTextContent(result);
		disValueNode.setAttribute("disOrder_string", disprotStr);
		disValueNode.setAttribute("disOrder_type", distype.toString());
		disValueNode.setAttribute("disOrder_type_In_AS", distypeInASRegion.toString());

		proteinNode.appendChild(disValueNode);

		// Element disStrNode=doc.createElement("disProt_string");
		// disStrNode.setTextContent(disprotStr);
		// proteinNode.appendChild(disStrNode);

		// Element disTypeNode=doc.createElement("disProt_type");
		// disTypeNode.setTextContent(distype.toString());
		// proteinNode.appendChild(disTypeNode);

		// Element ptmsNode = doc.createElement("ptm");
		// String ptmStr="";
		// for (int j = 0; j < ptms.size(); ++j) {
		// Element p=doc.createElement("field"+j);
		// ptmStr += ptms.get(j);
		// p.setTextContent(ptms.get(j));

		// ptmsNode.appendChild(p);
		// }

		// ptmsNode.setTextContent(ptmStr);
		// proteinNode.appendChild(ptmsNode);

		Element uniprotptmsNode = doc.createElement("uniprotptm");
		// String ptmStr="";
		for (int j = 0; j < uniPTMs.size(); ++j) {
			Element p = doc.createElement("field" + j);
			// ptmStr += ptms.get(j);
			p.setTextContent(uniPTMs.get(j).toString());

			uniprotptmsNode.appendChild(p);
		}

		// ptmsNode.setTextContent(ptmStr);
		proteinNode.appendChild(uniprotptmsNode);

	}

}
