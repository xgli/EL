/**
 * 
 */
package edu.li.expand;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jun 23, 2016 4:57:18 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 23, 2016 4:57:18 PM
 */
public class spaExpandMention {
	public static final String NEWSMENTIONINPUTDIR  = "data" + File.separator + "mention" + File.separator + "cmn" + File.separator + "news" + File.separator;
	public static final String DFMENTIONINPUTDIR  = "data" + File.separator + "mention" + File.separator + "cmn" + File.separator + "df" + File.separator;
//	public static final String MENTIONINPUTDIR = "data" + File.separator + "mention" + File.separator + "cmn" + File.separator;
	public static final String MENTIONEXPANDOUTDIR = "data" + File.separator + "mentionexpand" + File.separator + "cmn" + File.separator; 
	
	
	
	public static void expand(String fileName,String type) throws IOException{
		String text;
		if(type.equals("news")){
			text = IOUtils.slurpFile(NEWSMENTIONINPUTDIR + fileName);		
		}

		else{
			text = IOUtils.slurpFile(DFMENTIONINPUTDIR + fileName);
		}
			
		 String[] lines = text.split("\n");
		 Map<String, String> dict = new HashMap<String, String>();
		 FileOutputStream fos = new FileOutputStream(MENTIONEXPANDOUTDIR + fileName);
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		 
		 for(String line : lines){
			 int flag = 0;
			 String[] tokens = line.split("\t");
			 String mention = tokens[0];
			 String mention_type = tokens[2];
			 String newline = "";
			 if (dict.containsKey(mention)){
//				 newline = line + "\n";
				 newline = "";
			 }
			 else{
				 for (String key:dict.keySet()){
					 if(mention.length() < key.length() && key.contains(mention) && mention_type.equals(dict.get(key)) && mention_type.equals("PER")){
						 newline = mention + "\t" + line.replace(mention, key) + "\n";
						 System.out.println(newline);
						 flag = 1;
						 break;
					 }
				 }
				 if(flag == 0){
//					 newline = line + "\n";
					 newline = "";
					 dict.put(mention, mention_type);
				 }
			 }
			 osw.write(newline);

			 
		 }
	}
	
	public static void main(String[] args) throws IOException{
		
		String fileName = "CMN_NW_001127_20150725_F00100014.nw.ltf.xml";
		expand(fileName,"news");
	}
	
	
	
}
