/**
 * 
 */
package edu.li.edl;

import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;

import edu.li.candidate.spaGenCandidate;
import edu.li.mention.spaGenMention;
import edu.li.result.spaMergerResult;
import edu.li.xmlParse.spaXmlParse;

/**
 *date:Jun 18, 2016 10:02:48 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 18, 2016 10:02:48 PM
 */
public class spaProcess {
	
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
//					spaXmlParse.Parse(fileName, type);
//					System.out.println("GenMention:###########");
//					spaGenMention.GetMention(fileName, type);
					System.out.println("GenCandidate:#########");
					spaGenCandidate.GenCandidate(fileName, type);
				}
			}
			long end = System.currentTimeMillis();
			System.out.println((end - start) + "s");
		}
				
		
	}
	
	public static void main(String[] args) throws DocumentException, IOException {
	
		String newsFileDir = "data" + File.separator + "raw" + File.separator + "spa" + File.separator +  "news";
		String dfFileDir = "data" + File.separator + "raw" + File.separator + "spa" + File.separator +  "df";
		processAll(newsFileDir, "news");
		processAll(dfFileDir, "df");	
		spaMergerResult.mergerResult();
	}
	

}
