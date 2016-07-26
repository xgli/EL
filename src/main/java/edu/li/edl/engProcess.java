/**
 * 
 */
package edu.li.edl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dom4j.DocumentException;

import edu.li.candidate.engGenCandidate;
import edu.li.mention.engGenMention;
import edu.li.result.engMergerResult;
import edu.li.xmlParse.engXmlParse;

/**
 *date:Jun 18, 2016 10:02:37 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 18, 2016 10:02:37 PM
 */
public class engProcess {	
	
	public static void processAll(String fileDir, String type) throws DocumentException, IOException{
		File dir = new File(fileDir);
		File[] files = dir.listFiles();
		int all = files.length;
		int done = 0;
		long start = System.currentTimeMillis();
		if(files != null){
			FileOutputStream failedFilefos = new FileOutputStream("failedeng.tab");
			OutputStreamWriter failedFileosw = new OutputStreamWriter(failedFilefos, "UTF-8");
			for(File file : files){
				try {
					done += 1;
					System.out.println("doing:" + done + "\t" + "all:" + all);
					String fileName = file.getName();
					System.out.println(fileName);
					if(fileName.endsWith("xml")){
						System.out.println("xmlParse:###########start");
						engXmlParse.Parse(fileName, type);
						System.out.println("xmlParse:###########end");
						System.out.println("GenMention:###########start");
						engGenMention.GetMention(fileName, type);
						System.out.println("GenMention:###########end");
						System.out.println("GenCandidate:#########start");
						engGenCandidate.GenCandidate(fileName, type);
						System.out.println("GenCandidate:#########end");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.toString());
					failedFileosw.write(file.getName()+"\n");
					failedFileosw.write(e.toString()+"\n");
					continue;	
				}

			}
			failedFileosw.close();
			failedFilefos.close();
			long end = System.currentTimeMillis();
			System.out.println((end - start) + "s");
		}		
	}
	
	public static void main(String[] args) throws DocumentException, IOException {
		String newsFileDir = "data" + File.separator +  "raw" + File.separator + "eng" + File.separator +  "news";
		String dfFileDir = "data" + File.separator + "raw" + File.separator + "eng" + File.separator +  "df";
		processAll(newsFileDir, "news");
		processAll(dfFileDir, "df");	
		engMergerResult.mergerResult();
	}	
}
