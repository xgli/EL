/**
 * 
 */
package edu.li.candidate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
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
	
	public static final String MENTIONFILEINPUTDIR = "data" + File.separator + "mentiones" + File.separator + "spa" + File.separator;
	public static final String CANDIDATEFILEOUTDIR = "data" + File.separator + "candidate" + File.separator + "spa" + File.separator;
		
	public static final String ENTITYTEXTOUTDIR = "data" + File.separator + "entityText" + File.separator + "spa" + File.separator;
	
	public static final String MENTIONLISTOUTFILE = "data" + File.separator + "result" + File.separator + "spa" + File.separator + "mentionlist.tab";
	public static final String	TEMPRESULTOUTFILE = "data" + File.separator + "result" + File.separator + "spa" + File.separator +"tempresult.tab";
	
	public static final String DICTFILE = "dict" + File.separator + "spanish_sort.dict";
	
	static Map<String,String> DoneMention;
	
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
		
		file = new File("spa_candidate.ser");
		if(file.exists()){
			FileInputStream fis;
			try {
				fis = new FileInputStream("spa_candidate.ser");
				ObjectInputStream ois = new ObjectInputStream(fis);
				DoneMention = (HashMap<String, String>) ois.readObject();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}
		else {
			DoneMention = new HashMap<String, String>();			
		}
		
	}
	
	public static Map<String, String> loadDict() throws IOException{
		Map<String, String> dict = new HashMap<String, String>();
		String text = IOUtils.slurpFile(DICTFILE);
		String[] lines = text.split("\n");
		for(String line : lines){
			String[] tokens = line.split("\t");
			String mention = tokens[0];
			String mid = tokens[1];
			String type = tokens[2];
			dict.put(mention, mid + "\t" + type);
//			System.out.println(mention + mid + type);			
		}
		return dict;
	}
	
	static Map<String,String> dict = new HashMap<String, String>();
	static {
		try {
			dict = loadDict();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void GenCandidate(String fileName) throws IOException{
		
		String text = IOUtils.slurpFile(MENTIONFILEINPUTDIR + fileName); 
		FileOutputStream fos = new FileOutputStream(CANDIDATEFILEOUTDIR + fileName);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
				
		String[] lines = text.split("\n");

		 
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
			 
			 if(mention_type.equals("FAC"))//字表查错的FAC
				 continue;
			 
			 
			 if(-1 == mention_type.indexOf("NIL")){// 实验识别出来的两个相同的mention有不同的类型
//				 System.out.println(mention + ":" + mention_type);
				 osw.write("@" + mention + "\t" + mention_loc + "\t" + mention_type + "\n");
				 mentionosw.write(mention_loc + "\n");
				 mentionosw.flush();
				 
				 if(!DoneMention.containsKey(mention + mention_type)){//如果没有查询过
					 SearchHits hits = Search.getHits(mention, mention_type, "spa");
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
//					 System.out.println(thresholdScore);
					 String candidates = "";					 
					 
					 
					 for (SearchHit hit : hits.getHits()){ //getHits 的使用	
							if(hit.getScore() >= thresholdScore){
								candidates = candidates + hit.getId().toString() + "\n";
//								osw.write(mention + "\t" + mention_loc + "\t"+  hit.getId().replace("f_", "")  + "\t" + mention_type + "\n");
//								System.out.println(mention + "\t" + mention_loc + "\t"+  hit.getId()  + "\t" + mention_type + "\n");
//								break;// 获取第一个结果
//								System.out.println(mention);
//								System.out.println(hit.getId().toString());			
								String 	entitytext = hit.getFields().get("f_common.topic.description_es").getValue().toString();
								FileOutputStream candidateFilefos = new FileOutputStream(ENTITYTEXTOUTDIR + hit.getId());
								OutputStreamWriter candidateFileosw = new OutputStreamWriter(candidateFilefos, "UTF-8");
								candidateFileosw.write(entitytext);
								candidateFileosw.close();
								candidateFilefos.close();
							}
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
	
	

	
	
	public static void  main(String[] args) throws IOException, DocumentException, ClassNotFoundException {
//		 String fileName = "SPA_NW_001075_20150615_F0010003L.nw.ltf.xml";
//		 GenCandidate(fileName, "news");	
//		String MENTIONLISTOUTFILE = "data" + File.separator + "result" + File.separator + "spa" + File.separator + "mentionlist.tab";
//		String	TEMPRESULTOUTFILE = "data" + File.separator + "result" + File.separator + "spa" + File.separator +"tempresult.tab";
		
		
//		spaGenCandidate.GenCandidate("SPA_DF_001253_20150903_G00A0HK11.xml");
		
		
	
		
		File file;
		file = new File(MENTIONLISTOUTFILE);
		
		if(file.exists())
			file.delete();
		file = new File(TEMPRESULTOUTFILE);
		if(file.exists())
			file.delete();	  			
				
		FileInputStream fis = new FileInputStream("data/spafilenamelist.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		List<String> files = (ArrayList<String>) ois.readObject();
			
		int all = files.size();
		int done = 0;
		long start = System.currentTimeMillis();
		
		FileOutputStream failedFilefos = new FileOutputStream("failedspa.tab");
		OutputStreamWriter failedFileosw = new OutputStreamWriter(failedFilefos, "UTF-8");
		

		for(Iterator<String> iterator = files.iterator();iterator.hasNext();){
			String fileName = iterator.next();
			done += 1;
			System.out.println("doing:" + done + "\t" + "all:" + all);
			System.out.println(fileName);
			try {
					spaGenCandidate.GenCandidate(fileName);
//				}
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
//					System.out.println(e.printStackTrace());
				failedFileosw.write(fileName + "\n");
				failedFileosw.write(e.toString() + "\n");
				continue;					
			}		

		}	
		
		failedFileosw.close();
		failedFilefos.close();
		long end = System.currentTimeMillis();
		System.out.println((end - start) + "s");		
		
		
		FileOutputStream fos = new FileOutputStream("spa_candidate.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(DoneMention);
		oos.close();
		fos.close(); 
		 
	
	}
	
	
	

}
