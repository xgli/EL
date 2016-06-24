/**
 * 
 */
package edu.li.other;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jun 24, 2016 9:40:03 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 24, 2016 9:40:03 AM
 */
public class SearchWords {
	public static final String DICTFILE = "data" + File.separator + "dict" + File.separator + "chinese.tab";
	
	public static Map<String, String> loadDict() throws IOException{
		Map<String, String> dict = new HashMap<String, String>();
		String text = IOUtils.slurpFile(DICTFILE);
		String[] lines = text.split("\n");
		for(String line : lines){
//			System.out.println(line);
			String[] tokens = line.split("\t");
			String mention = tokens[0];
			String mid = tokens[2];
			String type = tokens[3];
			dict.put(mention, mid + "\t" + type);
//			System.out.println(mention + mid + type);			
		}
		return dict;
	}
	
	public static void GenSearchWords(String fileDir) throws IOException{
		File dir = new File(fileDir);
		File[] files = dir.listFiles();
		int all =  files.length;
		int count = 0;
		for(File file : files){
			String filePath = file.getAbsolutePath();
			String text = IOUtils.slurpFile(filePath);
			String[] lines = text.split("\n");
			for(String line : lines){
				
		
			}
		}
	}
	
	
	
	public static void main(String[] args) throws IOException {
		
		Map<String,String> dict = new HashMap<String, String>();
		dict = loadDict();
		// TODO Auto-generated method stub
		String newsFileDir = "data" + File.separator + "segment" + File.separator + "cmn" + File.separator +  "news";
		String dfFileDir = "data" + File.separator + "segment" + File.separator + "cmn" + File.separator +  "df";

	}


}
