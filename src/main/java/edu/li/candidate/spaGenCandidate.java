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
 *date:Jun 17, 2016 10:10:16 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 10:10:16 AM
 */
public class spaGenCandidate {
	
	public static final String MENTIONFILEINPUTDIR = "data" + File.separator + "mention" + File.separator + "spa" + File.separator;
	public static final String CANDIDATEFILEOUTDIR = "data" + File.separator + "candidate" + File.separator + "spa" + File.separator;
	
//	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "mention" + File.separator + "spa" + File.separator + "news" + File.separator;
//	public static final String NEWSFILEOUTDIR = "data" + File.separator + "candidate" + File.separator + "spa" + File.separator;
	public static final String LANG = "spa";
	
	
	public static final String ENTITYTEXTOUTDIR = "data" + File.separator + "entityText" + File.separator + "spa" + File.separator;
	
	public static final String MENTIONLISTOUTFILE = "data" + File.separator + "result" + File.separator + "spa" + File.separator + "mentionlist.tab";
	public static final String	TEMPRESULTOUTFILE = "data" + File.separator + "result" + File.separator + "spa" + File.separator +"tempresult.tab";
	
	public static final String DICTFILE = "data" + File.separator + "dict" + File.separator + "spanish.tab";
	
	static{
		File file ;
		file = new File(CANDIDATEFILEOUTDIR);
		if(!file.exists() && !file.isDirectory()){
			file.mkdirs();
		}
		file = new File(ENTITYTEXTOUTDIR);
		if(!file.exists() && file.isDirectory()){
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
		
		String text = IOUtils.slurpFile(MENTIONFILEINPUTDIR + fileName); 
		FileOutputStream fos = new FileOutputStream(CANDIDATEFILEOUTDIR + fileName);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		
		
//		if (fileType.equals("news")){
//			 text = IOUtils.slurpFile(NEWSFILEINPUTDIR + fileName);
//			 fos = new FileOutputStream(NEWSFILEOUTDIR + fileName);
//		 }
//		 else{
//			 text = IOUtils.slurpFile(DFFILEINPUTDIR + fileName);
//			 fos = new FileOutputStream(DFFILEOUTDIR + fileName);
//		 }
		
		Map<String,String> dict = new HashMap<String, String>();
		dict = loadDict();
		
		
		 String[] lines = text.split("\n");

		 Map<String, String> DoneMention = new HashMap<String, String>();
		 
		 FileOutputStream mentionfos = new FileOutputStream(MENTIONLISTOUTFILE,true);
		 OutputStreamWriter mentionosw = new OutputStreamWriter(mentionfos, "utf-8");
		 
		 for(String line : lines){
			 if (line.equals(""))
				 continue;
//			 System.out.println(line);
			 String[] tokens = line.trim().split("\t");
			 String mention = tokens[0];
			 String mention_type = tokens[2];
			 String mention_loc = tokens[1];
			 
			 //判断是否在词表中
			 if(dict.containsKey(mention)){		 
//				 osw.write(mention + "\t" + mention_loc + "\t" + dict.get(mention) + "\n");
				 FileOutputStream resultfos = new FileOutputStream(TEMPRESULTOUTFILE,true);
				 OutputStreamWriter resultosw = new OutputStreamWriter(resultfos, "utf-8");
				 resultosw.write(mention + "\t" + mention_loc + "\t" + dict.get(mention) + "\n");
				 resultosw.close();
				 resultfos.close();
				 continue;
			 }		 
			 
			 
			 if(-1 == mention_type.indexOf("NIL")){// 实验识别出来的两个相同的mention有不同的类型
//				 System.out.println(mention + ":" + mention_type);
				 osw.write("@" + mention + "\t" + mention_loc + "\t" + mention_type + "\n");
				 mentionosw.write(mention_loc + "\n");
				 mentionosw.flush();
				 
				 if(!DoneMention.containsKey(mention + mention_type)){//如果没有查询过
					 SearchHits hits = Search.getHits(mention, mention_type, LANG);
					 if (0 == hits.totalHits()){
//						 osw.write(mention + "\t" + mention_loc + "\t" + "NIL"  + "\t" + mention_type + "\n");
//						 DoneMention.put(mention+mention_type, "NIL");
//						 continue;//继续循环
						 DoneMention.put(mention+mention_type, "");
						 osw.flush();
						 continue;//继续循环
					 }
					 
					 float thresholdScore = hits.getHits()[0].getScore() / 2;
					 if (thresholdScore < (float) 0.5)
						 thresholdScore =  (float) 0.5;	
					 System.out.println(thresholdScore);
					 String candidates = "";					 
					 
					 
					 for (SearchHit hit : hits.getHits()){ //getHits 的使用	
							if(hit.getScore() >= thresholdScore){
								candidates = candidates + hit.getId().toString() + "\n";
//								osw.write(mention + "\t" + mention_loc + "\t"+  hit.getId().replace("f_", "")  + "\t" + mention_type + "\n");
//								System.out.println(mention + "\t" + mention_loc + "\t"+  hit.getId()  + "\t" + mention_type + "\n");
//								break;// 获取第一个结果
								String entitytext = hit.getFields().get("f_common.topic.description_es").getValue().toString();
//								entitytext = AnsjSegment.getAnsjSegment(entitytext);
								FileOutputStream candidateFilefos = new FileOutputStream(ENTITYTEXTOUTDIR + hit.getId());
								OutputStreamWriter candidateFileosw = new OutputStreamWriter(candidateFilefos, "UTF-8");
								candidateFileosw.write(entitytext);
								candidateFileosw.close();
								candidateFilefos.close();
							}

//						osw.write(mention + "\t" + mention_loc + "\t"+  hit.getId().replace("f_", "")  + "\t" + mention_type + "\n");
////					System.out.println(mention + "\t" + mention_loc + "\t"+  hit.getId()  + "\t" + mention_type + "\n");
//						DoneMention.put(mention+mention_type,  hit.getId().replace("f_", ""));						
//						break;// 获取第一个结果
					 }
					DoneMention.put(mention+mention_type,  candidates);	
					osw.write(candidates);
				 }
				 else {//已经查询过了
					 osw.write(DoneMention.get(mention+mention_type));
//					 osw.write(mention + "\t" + mention_loc + "\t" + DoneMention.get(mention+mention_type) + "\t" + mention_type  + "\n");
				 }
				 osw.flush();
		 	 }			 
			 
		 }
		 mentionosw.close();
		 mentionfos.close();
		 osw.close();
		 fos.close();
		
	}	
	
	public static void  main(String[] args) throws IOException {
		 String fileName = "SPA_NW_001075_20150615_F0010003L.nw.ltf.xml";
		 GenCandidate(fileName, "news");		
	}
	
	
	

}
