package ASEvent;

import java.util.ArrayList;
import java.util.Iterator;

import GeneStructure.Exon;
import GeneStructure.Strand;
import Utility.ASDebug;
import Utility.SequenceFeatureWrapper.SequenceFeatureExtractor;
import Utility.Structuure.ASTYPE;
import Utility.Structuure.Tris;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class ASevent {

	public ASevent(String toneEvent, String torganism) {
		oneEvent = toneEvent;
		organism = torganism;

		// init(toneEvent);

	}

	/*
	 * regex pattern for determining the alternative splicing type
	 * cassete,alternative 3' site or alternative 5' site or retained intro.
	 */

	public String id = "";

	protected ASTYPE asType = ASTYPE.UNKNOWN;

	protected Strand strand;

	protected String oneEvent;

	protected Fragment frag = new Fragment();

	protected String organism;

	protected String chr;

	protected ArrayList<EventIsoformFeature> eventIsoforms = new ArrayList<EventIsoformFeature>();
	
	protected ArrayList<Tris<String,Integer,Integer>> asRegions=new ArrayList<Tris<String,Integer,Integer>>();

	protected ArrayList<String> pfamInfo = new ArrayList<String>();

	protected ArrayList<String> cdss = new ArrayList<String>();
	
	protected ArrayList<Float> conservationScore=new ArrayList<Float>();

	public void init() {

		asType = tellASType(oneEvent);

		ASDebug.output(asType.toString());
		buildGeneFrag();
		ASDebug.output(frag.getSequence());
		asRegions=SequenceFeatureExtractor.getASRegions(chr, frag.getSeqBegPos(), frag.getSeqEndPos());
		conservationScore=SequenceFeatureExtractor.getConservationScore(chr, frag.getSeqBegPos(), frag.getSeqEndPos());
		
		
		// System.out.println(frag.getSeqBegPos() + "\t" + frag.getSeqEndPos());

		translate();

		// buildPfamInfo();//never used

		// buildCDSInfo();
	}

	protected abstract void buildGeneFrag();

	protected abstract ASTYPE tellASType(String toneEvent);

	protected abstract void translate();

	public Element processXML(Document doc, Element tableRoot) {
		Element eventNode = processXMLSequence(doc, tableRoot);

		for (int i = 0; i < eventIsoforms.size(); ++i) {
			eventIsoforms.get(i)
					.processXMLProtein(doc, tableRoot, eventNode, i);

		}
		return eventNode;
	}

	private Element processXMLSequence(Document doc, Element tableRoot) {
		Element eventRoot = doc.createElement("event");

		tableRoot.appendChild(eventRoot);
		// doc.appendChild(tableRoot);

		Element posNode = doc.createElement("position");
		posNode.setTextContent(oneEvent);
		eventRoot.appendChild(posNode);
		
		Element strandNode = doc.createElement("strand");
		strandNode.setTextContent(frag.strand.toString());
		eventRoot.appendChild(strandNode);
		
		
		// alternative splicing type
		Element exonsNode = doc.createElement("alternativeExons");
		Iterator<Exon> iteExon = frag.getExonsIterator();
		while (iteExon.hasNext()) {
			Exon exon = iteExon.next();
			Element exonNode = doc.createElement("exon");
			exonNode.setAttribute("start", "" + exon.getExonBegCoorPos());
			exonNode.setAttribute("end", "" + exon.getExonEndCoorPos());
			exonNode.setAttribute("modBy3", "" + exon.getModBy3());
			exonNode.setAttribute("IfAlternative", "" + exon.getIfAlternative());
			exonNode.setTextContent(exon.getSequenceAsString());
			exonsNode.appendChild(exonNode);
		}
		eventRoot.appendChild(exonsNode);
		
		Element alteventNode = doc.createElement("ucscaltevent");
		Iterator<Tris<String,Integer,Integer>> iteRegion = asRegions.iterator();
		while (iteRegion.hasNext()) {
			Tris<String,Integer,Integer> oneRegion = iteRegion.next();
			Element regionNode = doc.createElement("altRegion");
			
			regionNode.setAttribute("chr", "" + oneRegion.getValue1());
			regionNode.setAttribute("start", "" + oneRegion.getValue2());
			regionNode.setAttribute("end", "" + oneRegion.getValue3());
			
			//exonNode.setAttribute("IfAlternative", "" + exon.getIfAlternative());
			//exonNode.setTextContent(exon.getSequenceAsString());
			alteventNode.appendChild(regionNode);
		}
		eventRoot.appendChild(alteventNode);

		Element geneSeqNode = doc.createElement("geneSequence");
		geneSeqNode.setAttribute("start", "" + frag.getSeqBegPos());
		geneSeqNode.setAttribute("end", "" + frag.getSeqEndPos());
		geneSeqNode.setTextContent(frag.getSequenceOfTheGene());
		eventRoot.appendChild(geneSeqNode);

		Element asTypeNode = doc.createElement("asType");
		asTypeNode.setTextContent(asType.toString());
		eventRoot.appendChild(asTypeNode);
		// sequence

		Element seqNode = doc.createElement("sequenceOfExons");
		seqNode.setTextContent(frag.getSequence());
		eventRoot.appendChild(seqNode);
		
		
		Element conservationNode = doc.createElement("conservation");
		String result = "";
		for (int j = 0; j < conservationScore.size(); ++j) {

			result += conservationScore.get(j);

			if (conservationScore.size() - 1 != j)
				result += " ";
		}

		conservationNode.setTextContent(result);
		eventRoot.appendChild(conservationNode);

		// gene name
		// Element geneName = doc.createElement("ucsc_gene_name");
		// geneName.setTextContent(getGeneName());
		// eventRoot.appendChild(geneName);

		// if mod 3
		// Element mod3Node = doc.createElement("mod_by_3");
		// mod3Node.setTextContent("" + getMod3());
		// eventRoot.appendChild(mod3Node);

		// Element cds = doc.createElement("cds");
		// for (int i = 0; i < cdss.size(); ++i) {
		// Element p = doc.createElement("cds");
		// p.setTextContent(cdss.get(i));
		// cds.appendChild(p);
		// }
		// eventRoot.appendChild(cds);

		// pfam
		/*
		 * Element pfam = doc.createElement("pfam_information"); for (int i = 0;
		 * i < pfamInfo.size(); ++i) { Element p =
		 * doc.createElement("pfam_information_" + i);
		 * p.setTextContent(pfamInfo.get(i)); pfam.appendChild(p);
		 * 
		 * } eventRoot.appendChild(pfam);
		 */

		return eventRoot;
	}

}
