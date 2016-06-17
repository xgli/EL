/**
 * 
 */
package edu.li.candidate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import edu.li.es.Search;
import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jun 17, 2016 10:09:50 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 10:09:50 AM
 */
public class cmnGenCandidate {
	
	
	public static final String DFFILEINPUTDIR = "data" + File.separator + "mention" + File.separator + "cmn" + File.separator + "df" + File.separator;
	public static final String DFFILEOUTDIR = "data" + File.separator + "candidate" + File.separator + "cmn" + File.separator + "df" + File.separator;
	
	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "mention" + File.separator + "cmn" + File.separator + "news" + File.separator;
	public static final String NEWSFILEOUTDIR = "data" + File.separator + "candidate" + File.separator + "cmn" + File.separator + "news" + File.separator;
	
	
	public static void GenCandidate(String fileName, String fileType) throws IOException{
		
		String text = null; 
		FileOutputStream fos = null;
		if (fileType.equals("news")){
			 text = IOUtils.slurpFile(NEWSFILEINPUTDIR + fileName);
			 fos = new FileOutputStream(NEWSFILEOUTDIR + fileName);
		 }
		 else{
			 text = IOUtils.slurpFile(DFFILEINPUTDIR + fileName);
			 fos = new FileOutputStream(DFFILEOUTDIR + fileName);
		 }
		 String[] lines = text.split("\n");
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");

		 for(String line : lines){
//			 System.out.println(line);
			 String[] tokens = line.trim().split("\t");
			 String mention = tokens[0];
			 String mention_type = tokens[2];
			 String mention_loc = tokens[1];
//			 System.out.println("*******************************");

			 if(-1 == mention_type.indexOf("NIL")){
//				 System.out.println(mention+":"+mention_type);
				 SearchHits hits = Search.getHits(mention, mention_type, "cmn");
//				 System.out.print("li" + "\t" + "EDL"  + "\t");
//				 osw.write("li" + "\t" + "EDL" + "\t");
				 if (0 == hits.totalHits()){
//					 System.out.println(mention + "\t" + mention_loc + "\t" + "NIL" + "\t" + mention_type);
					 osw.write(mention + "\t" + mention_loc + "\t" + "NIL"  + "\t" + mention_type + "\n");
					 continue;
				 }
				 for (SearchHit hit : hits.getHits()){ //getHits 的使用	
//					System.out.println(mention + "\t" + mention_loc + "\t"+ hit.getId()  + "\t" + mention_type);
					osw.write(mention + "\t" + mention_loc + "\t"+ hit.getId()  + "\t" + mention_type + "\n");
					break;
				 }
				 osw.flush();			 
		 
			 }			 
			 
		 }
		 osw.close();
		
	}	
	
	public static void  main(String[] args) throws IOException {
		 String fileName = "CMN_DF_000020_20150108_F00100074.df.xml";
		 GenCandidate(fileName, "df");		 
//		 String text = IOUtils.slurpFile(DFFILEINPUTDIR + fileName);
//		 String[] lines = text.split("\n");
//		 FileOutputStream fos = new FileOutputStream(DFFILEOUTDIR + fileName);
//		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
//
// 
//		 for(String line : lines){
////			 System.out.println(line);
//			 String[] tokens = line.trim().split("\t");
//			 String mention = tokens[0];
//			 String mention_type = tokens[2];
//			 String mention_loc = tokens[1];
////			 System.out.println("*******************************");
//
//			 if(-1 == mention_type.indexOf("NIL")){
////				 System.out.println(mention+":"+mention_type);
//				 SearchHits hits = Search.getHits(mention, mention_type, "cmn");
////				 System.out.print("li" + "\t" + "EDL"  + "\t");
//				 osw.write("li" + "\t" + "EDL" + "\t");
//				 if (0 == hits.totalHits()){
//					 System.out.println(mention + "\t" + mention_loc + "\t" + "NIL" + "\t" + mention_type);
//					 osw.write(mention + "\t" + mention_loc + "NIL"  + "\t" + mention_type + "\n");
//					 continue;
//				 }
//				 for (SearchHit hit : hits.getHits()){ //getHits 的使用	
//					System.out.println(mention + "\t" + mention_loc + "\t"+ hit.getId()  + "\t" + mention_type);
//					osw.write(mention + "\t" + mention_loc + "\t"+ hit.getId()  + "\t" + mention_type + "\n");
//					break;
//				 }
//				 osw.flush();			 
//		 
//			 }			 
//			 
//		 }
//		 osw.close();		
	}
	

}
