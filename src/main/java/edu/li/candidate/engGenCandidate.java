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
 *date:Jun 17, 2016 10:10:04 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 10:10:04 AM
 */
public class engGenCandidate {
	
	public static final String DFFILEINPUTDIR = "data" + File.separator + "mention" + File.separator + "eng" + File.separator + "df" + File.separator;
	public static final String DFFILEOUTDIR = "data" + File.separator + "candidate" + File.separator + "eng" + File.separator + "df" + File.separator;
	
	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "mention" + File.separator + "eng" + File.separator + "news" + File.separator;
	public static final String NEWSFILEOUTDIR = "data" + File.separator + "candidate" + File.separator + "eng" + File.separator + "news" + File.separator;
	
	public static final String DICTFILE = "data" + File.separator + "dict" + File.separator + "english.tab";
	
	
	public static final String LANG = "eng";
	
	static{//判断是否存在文件目录
		File file ;
		file = new File(DFFILEOUTDIR);
		if(!file.exists() && !file.isDirectory()){
			file.mkdirs();
		}
		file = new File(NEWSFILEOUTDIR);
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
			String mid = tokens[1];
			String type = tokens[2];
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
		
		Map<String,String> dict = new HashMap<String, String>();
		dict = loadDict();
		
		
		 String[] lines = text.split("\n");
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		 Map<String, String> DoneMention = new HashMap<String, String>();
		 for(String line : lines){
//			 System.out.println(line);
			 String[] tokens = line.trim().split("\t");
			 String mention = tokens[0];
			 String mention_type = tokens[2];
			 String mention_loc = tokens[1];
			 
			 //判断是否在词表中
			 if(dict.containsKey(mention)){		 
				 osw.write(mention + "\t" + mention_loc + "\t" + dict.get(mention) + "\n");
				 continue;
			 }	 
			  
			 			 
			 if(-1 == mention_type.indexOf("NIL")){
//				 System.out.println(mention + ":" + mention_type);
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
					 osw.write(mention + "\t" + mention_loc + "\t" + DoneMention.get(mention + mention_type) + "\t" + mention_type  + "\n");
				 }
				 osw.flush();
		 	 }			 
			 
		 }
		 osw.close();
		 fos.close();
		
	}	
	
	public static void  main(String[] args) throws IOException {
//			 String fileName = "ENG_DF_000170_20150322_F00000082.df.ltf.xml";
//			 GenCandidate(fileName, "df");		
		loadDict();
	}
	

}
