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
import edu.li.wordSegment.AnsjSegment;
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
	
	public static final String MENTIONTEXTOUTDIR = "data" + File.separator + "mentionText" + File.separator + LANG + File.separator;

	public static final String DICTFILE = "data" + File.separator + "dict" + File.separator + "chinese.tab";
	
	static{
		File file ;
		file = new File(DFFILEOUTDIR);
		if(!file.exists() && !file.isDirectory()){
			file.mkdirs();
		}
		file = new File(NEWSFILEOUTDIR);
		if(!file.exists() && !file.isDirectory()){
			file.mkdirs();
		}
		file = new File(MENTIONTEXTOUTDIR);
		if(!file.exists() && !file.isDirectory()){
			file.mkdirs();
		}
	}
	
	
	
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
//				 String mid_type =dict.get(mention);
//				 String mid = mid_type.split("\t") 
				 osw.write("@" + mention + "\t" + mention_loc + "\t" + dict.get(mention).split("\t")[1] + "\n" +  dict.get(mention).split("\t")[0] + "\n");
				 continue;
//				 DoneMention.put(mention+mention_type, "NIL");
			 }
			 		 			 
			 if(-1 == mention_type.indexOf("NIL")){  //已经判定类型的
				 osw.write("@" + mention + "\t" + mention_loc + "\t" + mention_type + "\n");
				 if(!DoneMention.containsKey(mention+mention_type)){//如果没有查询过
					 SearchHits hits = Search.getHits(mention, mention_type, "cmn");
					 if (0 == hits.totalHits()){
						 DoneMention.put(mention+mention_type, "");
						 osw.flush();
						 continue;//继续循环
					 }
					 
					 float thresholdScore = hits.getHits()[0].getScore() / 2;
					 for (SearchHit hit : hits.getHits()){ //getHits 的使用	
						 String candidates = "";					 
						 
						if(hit.getScore() >= thresholdScore){							
							candidates = candidates + hit.getId().toString() + "\n";
//							osw.write(mention + "\t" + mention_loc + "\t"+  hit.getId().replace("f_", "")  + "\t" + mention_type + "\n");
//							System.out.println(mention + "\t" + mention_loc + "\t"+  hit.getId()  + "\t" + mention_type + "\n");
//							break;// 获取第一个结果
							String mentiontext = hit.getFields().get("f_common.topic.description_zh").getValue().toString();
							mentiontext = AnsjSegment.getAnsjSegment(mentiontext);
							FileOutputStream candidateFilefos = new FileOutputStream(MENTIONTEXTOUTDIR + hit.getId());
							OutputStreamWriter candidateFileosw = new OutputStreamWriter(candidateFilefos, "UTF-8");
							candidateFileosw.write(mentiontext);
							candidateFileosw.close();
							candidateFilefos.close();
						}
						DoneMention.put(mention+mention_type,  candidates);	
						osw.write(candidates);
					 }	 
				 }
				 else {//已经查询过了
					 osw.write(DoneMention.get(mention+mention_type));
				 }
				 osw.flush();
		 	 }
			 else {//NIL类型的处理 
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
