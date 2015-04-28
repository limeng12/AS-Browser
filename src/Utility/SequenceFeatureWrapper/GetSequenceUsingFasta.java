package Utility.SequenceFeatureWrapper;

import Utility.Structuure.ConfigureASEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import net.sf.picard.reference.FastaSequenceIndex;
import net.sf.picard.reference.IndexedFastaSequenceFile;

public class GetSequenceUsingFasta {
	public GetSequenceUsingFasta() {
		super();
		fastaPath = ConfigureASEvent.configurietion.get("FastaFilesPath");

	}

	static String fastaPath = "";

	private HashMap<String, IndexedFastaSequenceFile> faiGenome = new HashMap<String, IndexedFastaSequenceFile>();

	public String getSequence(String chr, int strart, int end) {

		byte[] seq = faiGenome.get(chr).getSubsequenceAt(chr, strart, end)
				.getBases();
		String str = new String(seq);
		return str;

	}

	public void generateFaiGenome() {
		String rootPath = fastaPath;
		File file = new File(rootPath);
		String[] pathList = file.list();
		try {
			for (int i = 0; i < pathList.length; ++i) {
				String fasta = rootPath + pathList[i];
				if (!fasta.endsWith(".fa"))
					continue;

				File fastaFile = new File(fasta);
				File indexFile = new File(fasta + ".fai");
				IndexedFastaSequenceFile testFai;
				//System.out.println(indexFile.getName());
				if (indexFile.exists())
					testFai = new IndexedFastaSequenceFile(fastaFile,
							new FastaSequenceIndex(indexFile));
				else {

					// testFai = new IndexedFastaSequenceFile(fastaFile);
					Process p = Runtime.getRuntime().exec(
							"/usr/bin/samtools faidx " + fasta);
					Thread.sleep(2 * 1000);
					System.out.println(fasta);
					p.waitFor();
					testFai = new IndexedFastaSequenceFile(fastaFile,
							new FastaSequenceIndex(indexFile));

				}

				// String chr = pathList[i].split("\\.", 3)[0];
				String chr = pathList[i].substring(0, pathList[i].length() - 3);
				faiGenome.put(chr, testFai);

			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testTbi() {

	}

	public void testFai() throws FileNotFoundException {
		// File fastaFile = new
		// File("F:\\AS-pipeline\\data\\mm9\\fasta\\chr1.fa");
		// IndexedFastaSequenceFile testFai = new
		// IndexedFastaSequenceFile(fastaFile);
		// ReferenceSequence x = testFai.getSubsequenceAt("chr1", 0, 100);
		// System.out.println(x.getBases()[3]);
		// String.format();

	}

	// IndexedGenome index= IndexedGenome.readIndex(indexFile);
	public static void main(String[] args) throws FileNotFoundException {
		GetSequenceUsingFasta name = new GetSequenceUsingFasta();
		// name.testFai();
		name.generateFaiGenome();
		System.out.println(name.getSequence("chr1", 1, 10000));

	}

}
