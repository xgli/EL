/**
 * 
 */
package edu.li.candidate;

import java.awt.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.freebase.samples.SearchExample;

import edu.li.es.Search;
import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jun 16, 2016 2:48:29 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 16, 2016 2:48:29 PM
 */
public class GenCandidata {
	
	
	public static final String FILEINPUTDIR = "data" + File.separator + "mention" + File.separator + "cmn" +File.separator;
	public static final String FILEOUTDIR = "data" + File.separator + "candidate" + File.separator;
	
	public static void  main(String[] args) throws IOException {
		 String fileName = "CMN_NW_000020_20150604_F00100013.nw.xml";
		 String text = IOUtils.slurpFile(FILEINPUTDIR + fileName);
		 String[] lines = text.split("\n");
		 FileOutputStream fos = new FileOutputStream(FILEOUTDIR + fileName);
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		 int count = 0;
		 int NILcount = 0; 
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
				 System.out.print("li" + "\t" + "EDL_" + count + "\t");
				 count++;
				 osw.write("li" + "\t" + "EDL_"+ count + "\t");
				 if (0 == hits.totalHits()){
					 System.out.println(mention + "\t" + mention_loc + "\t" + "NIL" + NILcount + "\t" + mention_type + "\t" + "NAM" + "\t" + "1.0");
					 osw.write(mention + "\t" + mention_loc + "NIL" + NILcount + "\t" + mention_type + "NAM" + "\t" + "1.0" + "\n");
//					 System.out.println("*******************************");		
					 continue;
				 }
				 for (SearchHit hit : hits.getHits()){ //getHits 的使用			
//					System.out.println(hit.getId());
//					System.out.println(hit.getFields().get("rs_label_zh").getValue());
//					System.out.println(hit.getFields().get("f_common.topic.description_zh").getValue());
//					String mid = hit.getId().split("/")[0] + "." + hit.getId().split("/")[1];
					System.out.println(mention + "\t" + mention_loc + "\t"+ hit.getId()  + "\t" + mention_type + "\t" + "NAM" + "\t" + "1.0");
					osw.write(mention + "\t" + mention_loc + "\t"+ hit.getId()  + "\t" + mention_type + "\t" + "NAM" + "\t" + "1.0" + "\n");
//					System.out.print(mention + "\t" + mention_loc + mid + "\t" + mention_type + "NAM" + "\t" + "1.0");
					break;
				 }
				 osw.flush();			 
		 
			 }			 
			 
		 }
		 osw.close();
	}

}
