package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TestReadFile {

	public static void main(String[] args) {
		String text="";
		int line = 1;

		try {
			PrintWriter pw = new PrintWriter(new FileWriter(
					"merge_novel.vcf"));

			File file = new File(
					"/media/Elements/zhujingde/exon-capture/vcf/merge.vcf");
			BufferedReader input;

			input = new BufferedReader(new FileReader(file));


			while ((text = input.readLine()) != null) {
				//if (line > 2)
				//	break;
				// System.out.println(text);
				if(text.startsWith("#"))
					continue;
				
				
				String[] arr = text.split("\t");
				//System.out.println(arr[3]);
				//System.out.println(arr[13]);
				if(arr[2].startsWith("rs"))
						continue;
			
				
				line++;
				
			}
			input.close();
			pw.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println(text);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println(text);
		}catch(Exception e){
			System.out.println(e);
			System.out.println(text);
			System.out.println(line);
			
		}

	}

}
