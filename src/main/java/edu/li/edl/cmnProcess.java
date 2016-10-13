/**
 * 
 */
package edu.li.edl;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dom4j.DocumentException;



/**
 *date:Jun 18, 2016 10:02:26 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 18, 2016 10:02:26 PM
 */
public class cmnProcess {
	
	public static void processAll(String fileDir, String type) throws DocumentException, IOException{
		File dir = new File(fileDir);
		File[] files = dir.listFiles();
		int all = files.length;
		int done = 0;
		long start = System.currentTimeMillis();
		
		FileOutputStream failedFilefos = new FileOutputStream("failedcmn.tab");
		OutputStreamWriter failedFileosw = new OutputStreamWriter(failedFilefos, "UTF-8");	
		
		
		if(files != null){
			for(File file : files){
				try {
					done += 1;
					System.out.println("doing:" + done + "\t" + "all:" + all);
					String fileName = file.getName();
					System.out.println(fileName);
					if(fileName.endsWith("xml")){
//						System.out.println("xmlParse:###########");
//						cmnXmlParse.Parse(fileName, type);
//						System.out.println("GenMention:###########");
//						cmnGenMention.GetMention(fileName, type);
						
						System.out.println("GenCandidate:#########");
//						cmnGenCandidate.GenCandidate(fileName);
//						expandMention.expand(fileName, type);
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.toString());
//					System.out.println(e.printStackTrace());
					failedFileosw.write(file.getName() + "\n");
					failedFileosw.write(e.toString() + "\n");
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
		String MENTIONLISTOUTFILE = "data" + File.separator + "result" + File.separator + "cmn" + File.separator + "mentionlist.tab";
		String	TEMPRESULTOUTFILE = "data" + File.separator + "result" + File.separator + "cmn" + File.separator +"tempresult.tab";
		File file;
		file = new File(MENTIONLISTOUTFILE);		
		if(file.exists())
			file.delete();
		file = new File(TEMPRESULTOUTFILE);
		if(file.exists())
			file.delete();			
		
		String newsFileDir = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator +  "nw";
		String dfFileDir = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator +  "df";
		
		
		processAll(newsFileDir, "news");
		processAll(dfFileDir, "df");
//		cmnMergerResult.mergerResult();
//		cmnMergerResult.mergerAuthor();

	}	
	
}
