package Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import Utility.Structuure.ConfigureASEvent;

public class IndexWigFix {
	
	public static void index(String fileName){
		String text="";
		int line = 1;
		long numberOfBytes=0;
		
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(fileName+".index"));

			File file = new File(fileName);
			BufferedReader input;

			input = new BufferedReader(new FileReader(file));
			
			ArrayList<String> indexes=new ArrayList<String>();
			int length=0;
			while ((text = input.readLine()) != null) {
				numberOfBytes+=text.length()+1;
				//if(line>2)
				//	break;
				if(text.startsWith("fixedStep")){
					
					indexes.add(text+" numberOfBytes="+numberOfBytes);
					if(line!=1){
						indexes.set(indexes.size()-2, indexes.get(indexes.size()-2)+" length="+length);
						
					}
					
					length=0;
					
				}
				length++;
	
				
				line++;
				
			}
			
			indexes.set(indexes.size()-1, indexes.get(indexes.size()-1)+" length="+length);

			for(String ite:indexes){
				pw.println(ite);
				
			}
			
			
			input.close();
			pw.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
	}
	
	public static void indexAll(String fileName){
		ConfigureASEvent.readConfigureFromFile(fileName);
		String path=ConfigureASEvent.getConfigure().get("conservationPath");
		File gtf = new File(path);
		String[] names = gtf.list();
		for(String onePath:names){
			
			String oneFileName = path + onePath;
			System.out.println(oneFileName);
			if(oneFileName.endsWith("wigFix"))
				index(oneFileName);
			
		}
		
	}
	
	public static void main(String[] args){
		//indexAll("/home/limeng/alternative/Configure.txt");
		//indexAll(args[0]);
		
		index("/home/limeng/alternative/conservation/chr14.phastCons100way.wigFix");
		
	}
	

}
