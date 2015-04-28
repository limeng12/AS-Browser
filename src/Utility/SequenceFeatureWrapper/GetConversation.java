package Utility.SequenceFeatureWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import Utility.Structuure.ConfigureASEvent;

public class GetConversation {
	// ArrayList<Float>
	public static ArrayList<Float> getConservationScore(String chr, int begPos,
			int endPos) {
		
		String conservationPath=ConfigureASEvent.configurietion.get("conservationPath");
		ArrayList<Float> result = new ArrayList<Float>();
		
		try {
		//chr = "chr1";
		String indexFileName=conservationPath+chr+".phastCons100way.wigFix.index";
		File fileIndex = new File(indexFileName);
		BufferedReader input;
		String text="";
		input = new BufferedReader(new FileReader(fileIndex));

		int startPos=-1;
		long bytes=-1;
		boolean find=false;
		
		while ((text = input.readLine()) != null) {
			String[] arrLine=text.split(" ");
			startPos=Integer.parseInt(arrLine[2].substring(6));
			bytes=Integer.parseInt(arrLine[4].substring(14));
			int length=Integer.parseInt(arrLine[5].substring(7));
				
			if(begPos>startPos&&begPos<startPos+length){
				if(begPos>startPos+length)
					return result;
				else{
					find=true;
					break;
					
				}
			}

		}
		
		
		input.close();
		if(!find)
			return result;
		String fileName=conservationPath+chr+".phastCons100way.wigFix";
		File file = new File(fileName);
		
		RandomAccessFile raf;

			raf = new RandomAccessFile(file, "rw");
/*
			String a = raf.readLine();
			String[] arrHead=a.split(" ");
			int start=-1;
			
			for(int i=0;i<arrHead.length;++i){
				if(arrHead[i].startsWith("start")){
					start=Integer.parseInt(arrHead[i].substring(6));
					System.out.println(start);
				}
			}
			
			if(begPos<start)
				return result;

			
			begPos=begPos-start;
			
			int headLength = a.length() + 1;

			System.out.println("Read full line: " + a);
			// raf.seek(headLength+1);
			 */ 
			
			//begPos=begPos-startPos;

			raf.seek(bytes + (begPos-startPos - 1) * 6);
			// raf.read((endPos-begPos+1)*6);
			byte[] b = new byte[(endPos - begPos + 1) * 6];
			raf.readFully(b, 0, (endPos - begPos + 1) * 6);
			String s = new String(b);
			String[] arrLine = s.split("\n");
			
			for (int i = 0; i < arrLine.length; ++i) {
				// System.out.println(Float.parseFloat(arrLine[i]));
				result.add(Float.parseFloat(arrLine[i]));

			}

			raf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}
//conservationPath
	public static void main(String[] args) {
		ConfigureASEvent.readConfigureFromFile("/home/limeng/alternative/Configure.txt");
		ArrayList<Float> result=getConservationScore("chr1", 1000, 1015);
		for(Float ite:result){
			System.out.println(ite);
			
		}
		
		
	}

}
