/**
 * 
 */
package edu.li.other;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.DocumentException;

import edu.li.candidate.engGenCandidate;
import edu.li.mention.cmnGenMention;
import edu.li.mention.engGenMention;
import edu.li.xmlParse.engXmlParse;
import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jun 23, 2016 8:28:43 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 23, 2016 8:28:43 PM
 */
public class other {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public static void getOthers(String fileDir) throws IOException{
		File dir = new File(fileDir);
		File[] files = dir.listFiles();
		int all =  files.length;
		int count = 0;
		for(File file : files){
			String filePath = file.getAbsolutePath();
			String text = IOUtils.slurpFile(filePath);
			String[] lines = text.split("\n");

			for(String line : lines){
//				System.out.println(line);
//				String ner = cmnGenMention.getAnsjNER(line.split("\t")[1]);
				
				Pattern pattern = Pattern.compile("([\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})+)");//提取所有带·的名字
				Matcher matcher = pattern.matcher(line);
				while(matcher.find()){
					count++;
					System.out.println(matcher.group(1) + ":" + count);
				}
				
			}			
		}
		
	}	
	
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String newsFileDir = "data" + File.separator + "segment" + File.separator + "cmn" + File.separator +  "news";
		String dfFileDir = "data" + File.separator + "segment" + File.separator + "cmn" + File.separator +  "df";
		getOthers(newsFileDir);
	}

}
