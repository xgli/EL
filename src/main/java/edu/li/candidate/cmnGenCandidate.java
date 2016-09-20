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
import edu.li.mention.engGenMention;
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
	
	public static final String MENTIONFILEINPUTDIR = "data" + File.separator + "mentiones" + File.separator +  LANG  + File.separator;
	public static final String CANDIDATEFILEOUTDIR = "data" + File.separator + "candidate" + File.separator + LANG + File.separator;	

	
	//候选的文本
	public static final String ENTITYTEXTOUTDIR = "data" + File.separator + "entityText" + File.separator + LANG + File.separator;

	public static final String DICTFILE = "data" + File.separator + "dict" + File.separator + "cmn_candidate_dict.tab";
	
	public static final String MENTIONLISTOUTFILE = "data" + File.separator + "result" + File.separator + "cmn" + File.separator + "mentionlist.tab";
	public static final String	TEMPRESULTOUTFILE = "data" + File.separator + "result" + File.separator + "cmn" + File.separator +"tempresult.tab";
	
	static Map<String,String> DoneMention;
	
	static{
		File file ;
		file = new File(CANDIDATEFILEOUTDIR);
		if(!file.exists() && !file.isDirectory()){
			file.mkdirs();
		}
		file = new File(ENTITYTEXTOUTDIR);
		if(!file.exists() && !file.isDirectory()){
			file.mkdirs();
		}
		
		file = new File("cmn_candidate.ser");
		if(file.exists()){
			FileInputStream fis;
			try {
				fis = new FileInputStream("cmn_candidate.ser");
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
		
//		if (fileType.equals("news")){
//			 text = IOUtils.slurpFile(NEWSFILEINPUTDIR + fileName);
//			 fos = new FileOutputStream(NEWSFILEOUTDIR + fileName);
//		 }
//		 else{
//			 text = IOUtils.slurpFile(DFFILEINPUTDIR + fileName);
//			 fos = new FileOutputStream(DFFILEOUTDIR + fileName);
//		 }
		
		//加载词表
//		 Map<String,String> dict = new HashMap<String, String>();
//		 dict = loadDict();		 
		
		
		
		 String[] lines = text.split("\n");

//		 Map<String, String> DoneMention = new HashMap<String, String>();
//		 System.out.println(lines[0]);
		 FileOutputStream mentionfos = new FileOutputStream(MENTIONLISTOUTFILE,true);
		 OutputStreamWriter mentionosw = new OutputStreamWriter(mentionfos, "utf-8");
		 for(String line : lines){
			 if(line.equals("")){
				 continue;
			 }
//			 System.out.println(line);
			 String[] tokens = line.trim().split("\t");
			 String mention = tokens[0];
			 String mention_type = tokens[2];
			 String mention_loc = tokens[1];
//			 System.out.println(mention);
			 
			 if(dict.containsKey(mention)){
				 FileOutputStream resultfos = new FileOutputStream(TEMPRESULTOUTFILE,true);
				 OutputStreamWriter resultosw = new OutputStreamWriter(resultfos, "utf-8");
				 resultosw.write(mention + "\t" + mention_loc + "\t" + dict.get(mention) + "\n");
				 resultosw.close();
				 resultfos.close();				 
//				 String mid_type =dict.get(mention);
//				 String mid = mid_type.split("\t") 
//				 osw.write("@" + mention + "\t" + mention_loc + "\t" + dict.get(mention).split("\t")[1] + "\n" +  dict.get(mention).split("\t")[0] + "\n");
//				 System.out.println(mention_loc);
//				 System.out.println("f_"+dict.get(mention).split("\t")[0]);
//				 SearchHit hit = Search.getHitsById("f_" + dict.get(mention).split("\t")[0]).getHits()[0];
//				String entitytext = hit.getFields().get("f_common.topic.description_zh").getValue().toString();
//				
//				entitytext = AnsjSegment.getAnsjSegment(entitytext);
//				FileOutputStream candidateFilefos = new FileOutputStream(ENTITYTEXTOUTDIR + hit.getId());
//				OutputStreamWriter candidateFileosw = new OutputStreamWriter(candidateFilefos, "UTF-8");
//				candidateFileosw.write(entitytext);
//				candidateFileosw.close();
//				candidateFilefos.close();
				 
				 
//				 mentionosw.write(mention_loc + "\n");
//				 mentionosw.flush();
				 continue;
//				 DoneMention.put(mention+mention_type, "NIL");
			 }
			 
			 if(mention_type.equals("FAC"))//字表查错的FAC
				 continue;
			 		 			 
			 if(-1 == mention_type.indexOf("NIL")){  //已经判定类型的
				 osw.write("@" + mention + "\t" + mention_loc + "\t" + mention_type + "\n");
				 mentionosw.write(mention_loc + "\n");
				 mentionosw.flush();
				 if(!DoneMention.containsKey(mention+mention_type)){//如果没有查询过
					 SearchHits hits = Search.getHits(mention, mention_type, "cmn");
					 if (0 == hits.totalHits()){
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
//							osw.write(mention + "\t" + mention_loc + "\t"+  hit.getId().replace("f_", "")  + "\t" + mention_type + "\n");
//							System.out.println(mention + "\t" + mention_loc + "\t"+  hit.getId()  + "\t" + mention_type + "\n");
//							break;// 获取第一个结果
							String entitytext = hit.getFields().get("f_common.topic.description_zh").getValue().toString();
							entitytext = AnsjSegment.getAnsjSegment(entitytext);
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
				 }
				 osw.flush();
		 	 }
			 else {//NIL类型的处理 
//				 osw.write(mention + "\t" + mention_loc + "\t" + "NIL" + "\t" + "GPE" + "\n");
			 }
		 }
		 mentionosw.close();
		 mentionfos.close();
		 osw.close();
		 fos.close();		
	}	
	
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
						System.out.println("GenCandidate:#########");
//						cmnGenCandidate.GenCandidate(fileName, type);
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
	
	public static void  main(String[] args) throws IOException, DocumentException, ClassNotFoundException {
//		 String fileName = "CMN_NW_000020_20150604_F00100013.nw.ltf.xml";
//		 GenCandidate(fileName, "news");	
//		String MENTIONLISTOUTFILE = "data" + File.separator + "result" + File.separator + "cmn" + File.separator + "mentionlist.tab";
//		String	TEMPRESULTOUTFILE = "data" + File.separator + "result" + File.separator + "cmn" + File.separator +"tempresult.tab";
		File file;
		file = new File(MENTIONLISTOUTFILE);		
		if(file.exists())
			file.delete();
		file = new File(TEMPRESULTOUTFILE);
		if(file.exists())
			file.delete();		
		
		
//		String newsFileDir = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator +  "nw";
//		String dfFileDir = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator +  "df";
		
		
//		processAll(newsFileDir, "news");
//		processAll(dfFileDir, "df");
		
		FileInputStream fis = new FileInputStream("data/cmnfilenamelist.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		List<String> files = (ArrayList<String>) ois.readObject();
		
		
	
		int all = files.size();
		int done = 0;
		long start = System.currentTimeMillis();
		
		FileOutputStream failedFilefos = new FileOutputStream("failedcmn.tab");
		OutputStreamWriter failedFileosw = new OutputStreamWriter(failedFilefos, "UTF-8");		
		

		for(Iterator<String> iterator = files.iterator();iterator.hasNext();){
			String fileName = iterator.next();
			done += 1;
			System.out.println("doing:" + done + "\t" + "all:" + all);
			System.out.println(fileName);
			try {
//				if(fileName.endsWith("xml")){
//					System.out.println("GenMention:###########");
//					cmnGenCandidate(fileName);
					cmnGenCandidate.GenCandidate(fileName);

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
		
		FileOutputStream fos = new FileOutputStream("cmn_candidate.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(DoneMention);
		oos.close();
		fos.close();
		 
		 
	}

}
