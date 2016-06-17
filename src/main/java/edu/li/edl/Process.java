/**
 * 
 */
package edu.li.edl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.http.impl.cookie.PublicSuffixListParser;
import org.dom4j.DocumentException;

import edu.li.candidate.cmnGenCandidate;
import edu.li.mention.cmnGenMention;
import edu.li.xmlParse.xmlParse;
import edu.stanford.nlp.ie.crf.FactorTable;
import edu.stanford.nlp.sentiment.SentimentTraining;

/**
 *date:Jun 17, 2016 10:51:21 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 10:51:21 AM
 */
public class Process {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws DocumentException 
	 * 
	 */
	public static void processAll(String fileDir, String type) throws DocumentException, IOException{
		File dir = new File(fileDir);
		File[] files = dir.listFiles();
		int all = files.length;
		int done = 0;
		long start = System.currentTimeMillis();
		if(files != null){
			for(File file : files){
				done += 1;
				System.out.println("doing:" + done + "\t" + "all:" + all);
				String fileName = file.getName();
				System.out.println(fileName);
				if(fileName.endsWith("xml")){
//					System.out.println("xmlParse:###########");
					if(type.equals("df"))
						xmlParse.ParseDf(fileName);
					else
						xmlParse.ParseNews(fileName);
//					System.out.println("GenMention:###########");
					cmnGenMention.GetMention(fileName, type);
//					System.out.println("GenCandidate:#########");
					cmnGenCandidate.GenCandidate(fileName, type);					
				}
			}
			long end = System.currentTimeMillis();
			System.out.println((end - start) + "s");
		}
				
		
	}
	
	public static void main(String[] args) throws DocumentException, IOException {
	
		String newsFileDir = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator +  "news";
		String dfFileDir = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator +  "df";
		processAll(newsFileDir, "news");
		processAll(dfFileDir, "df");		

	}
//				

	

}
