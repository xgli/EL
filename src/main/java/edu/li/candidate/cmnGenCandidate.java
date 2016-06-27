/**
 * 
 */
package edu.li.candidate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

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
	
	public static final String LANG = "cmn";
	
	public static final String DFFILEINPUTDIR = "data" + File.separator + "mention" + File.separator +  LANG  + File.separator + "df" + File.separator;
	public static final String DFFILEOUTDIR = "data" + File.separator + "candidate" + File.separator + LANG + File.separator + "df" + File.separator;
	
	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "mention" + File.separator + LANG + File.separator + "news" + File.separator;
	public static final String NEWSFILEOUTDIR = "data" + File.separator + "candidate" + File.separator + LANG + File.separator + "news" + File.separator;
	
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
		
		//加载词表
		Map<String,String> dict = new HashMap<String, String>();
		dict = loadDict();
		
		
		 String[] lines = text.split("\n");
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		 Map<String, String> DoneMention = new HashMap<String, String>();
//		 System.out.println(lines[0]);
		 for(String line : lines){
			 if(line.equals("")){
				 continue;
			 }
//			 System.out.println(line);
			 String[] tokens = line.trim().split("\t");
			 String mention = tokens[0];
			 String mention_type = tokens[2];
			 String mention_loc = tokens[1];
			 
			 
			 if(dict.containsKey(mention)){
//				 String temp =dict.get(mention);			 
				 osw.write(mention + "\t" + mention_loc + "\t" + dict.get(mention) + "\n");
				 continue;
//				 DoneMention.put(mention+mention_type, "NIL");
			 }
			 		 			 
			 if(-1 == mention_type.indexOf("NIL")){  //已经判定类型的
				 
				 if(!DoneMention.containsKey(mention+mention_type)){//如果没有查询过
					 SearchHits hits = Search.getHits(mention, mention_type, LANG);
					 if (0 == hits.totalHits()){
						 osw.write(mention + "\t" + mention_loc + "\t" + "NIL"  + "\t" + mention_type + "\n");
						 DoneMention.put(mention+mention_type, "NIL");
						 continue;//继续循环
					 }
					 for (SearchHit hit : hits.getHits()){ //getHits 的使用	
						osw.write(mention + "\t" + mention_loc + "\t"+  hit.getId().replace("f_", "")  + "\t" + mention_type + "\n");
//						System.out.println(mention + "\t" + mention_loc + "\t"+  hit.getId()  + "\t" + mention_type + "\n");
						DoneMention.put(mention+mention_type,  hit.getId().replace("f_", ""));						
						break;// 获取第一个结果
					 }

				 }
				 else {//已经查询过了
					 osw.write(mention + "\t" + mention_loc + "\t" + DoneMention.get(mention+mention_type) + "\t" + mention_type  + "\n");
				 }
				 osw.flush();
		 	 }
			 else { 
//				 osw.write(mention + "\t" + mention_loc + "\t" + "NIL" + "\t" + "GPE" + "\n");
			 }
		 } 
		 
		 osw.close();
		 fos.close();
		
	}	
	
	public static void  main(String[] args) throws IOException {
		 String fileName = "CMN_DF_000311_20150707_F0010009S.df.ltf.xml";
		 GenCandidate(fileName, "df");	
//		loadDict();
	}

}
