/**
 * 
 */
package edu.li.mention;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.DocumentException;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;

/**
 *date:Jun 17, 2016 9:24:05 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 9:24:05 AM
 */
public class engGenMention {

//	public static final String NERHOST =  "127.0.0.1";
//	public static final int NERPORT = 2314;
	
	public static final String FILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "eng" + File.separator;
//	public static final String NEWSFILEINPUTDIR = "data" + File.separator  +  "xmlParse" + File.separator + "eng" + File.separator + "news" + File.separator;
	public static final String MENTIONFILEOUTDIR = "data" + File.separator + "mention" + File.separator + "eng" + File.separator;
	public static final String MENTIONTEXTOUTDIR = "data" + File.separator + "mentionText" + File.separator + "eng" +File.separator;

	public static final String serializedClassifier = "classifiers/english.nowiki.3class.distsim.crf.ser.gz";
	public static AbstractSequenceClassifier<CoreLabel> ner;
	
	static {	
			ner = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	}
	
	
	static{//判断文件目录是否存在
		File file;
		file = new File(MENTIONFILEOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
		file = new File(MENTIONTEXTOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
	}	

	
	public static String getNer(String text) throws IOException{
		 String  str = ner.classifyWithInlineXML(text);
		 str = str.replaceAll("PERSON>", "PER>").replaceAll("LOCATION>", "GPE>");
		 return str.replaceAll("ORGANIZATION>", "ORG>").replaceAll("<MISC>", "").replaceAll("</MISC>", "");
				 
//	   	 StringReader sr = new StringReader(text); //输入切分好的text
//		 BufferedReader br = new BufferedReader(sr);
//		 StringWriter sw = new StringWriter();
//		 BufferedWriter bw = new BufferedWriter(sw);
//		 NERClient.communicateWithNERServer(NERHOST, NERPORT, "UTF-8",br,bw,false);
//		 bw.close();
//		 br.close();
////		 String ner = sw.toString().replaceAll("\t", "").replaceAll("LOC", "GPE").replaceAll("PERSON", "PER");
//		 String ner = sw.toString().replaceAll("PERSON>", "PER>").replaceAll("LOCATION>", "GPE>");
////		 return  ner.replaceAll("MISC", "NIL");//这一类有点特殊。
//		 return ner.replaceAll("ORGANIZATION>", "ORG>").replaceAll("<MISC>", "").replaceAll("</MISC>", "");
	}
	
	public static void GetMention(String fileName) throws IOException {
		 
		 String text = IOUtils.slurpFile(FILEINPUTDIR + fileName,"utf-8"); 

		 FileOutputStream segfos = new FileOutputStream(MENTIONTEXTOUTDIR + fileName);
		 FileOutputStream nerfos = new FileOutputStream(MENTIONFILEOUTDIR + fileName);;
//		 if(file_type.equals("news")){
//			 text = IOUtils.slurpFile(NEWSFILEINPUTDIR + fileName);
////			 nerfos = new FileOutputStream(NEWSFILEOUTDIR + fileName);
//
//		 }
//		 else {
//			 text = IOUtils.slurpFile(DFFILEINPUTDIR + fileName);	
////			 nerfos = new FileOutputStream(DFFILEOUTDIR + fileName);
////			 segfos = new FileOutputStream(DFSEGMENTOUTDIR + fileName);
//		 } 

		 String[] lines = text.split("\n");
		 int start = 0; //mention location the first char.
		 int end = 0;  //mention location the lastest char.	
		 
		 OutputStreamWriter segosw = new OutputStreamWriter(segfos, "UTF-8");
		 OutputStreamWriter nerosw = new OutputStreamWriter(nerfos, "UTF-8");
		 
		 String fileID = fileName.replace(".xml", "");
		 for(String line:lines){
//			 System.out.println(line);
			 int bias = Integer.parseInt(line.split("\t")[0].trim());
			 String textLine = line.split("\t")[1];
			 
			 segosw.write(textLine);
			 segosw.write("\n");
			 segfos.flush();
			 
			 String ner = getNer(textLine);
//			 System.out.print(bias);
//			 System.out.println(ner);
			 int len = 0;
			 Pattern pattern;
			 Matcher matcher;
			 
			 pattern = Pattern.compile("<(GPE|ORG|PER|LOC|FAC)>(.*?)(</GPE>|</ORG>|</PER>|</LOC>|</FAC>)");
			 matcher = pattern.matcher(ner);
			 while(matcher.find()){	 //考虑提取后的，标签对位置的影响
				 start = matcher.start() - len + bias;
				 end = start + matcher.group(2).length() - 1;
				 len = len + matcher.group(1).length() * 2 + 5;
				 String mention = matcher.group(2);
//				 System.out.println(mention);
				 String type = matcher.group(1);
				 String loc = start + "-" + end;
				 
//				 System.out.print(mention + "\t");
				 nerosw.write(mention + "\t");				 
//				 System.out.print(loc + "\t");
				 nerosw.write(fileID + ":" + loc + "\t");					 
//				 System.out.println(type);
				 nerosw.write(type + "\n");
				 nerosw.flush();
			 }			 
			 
		 }
		 nerosw.close();
		 nerfos.close(); 
		 segosw.close();
		 segfos.close();
	}
	
	
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
						System.out.println("GenMention:###########start");
//						engGenMention.GetMention(fileName, type);
						System.out.println("GenMention:###########end");
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
	


	public static void main(String[] args) throws IOException, DocumentException, ClassNotFoundException {
		
		// TODO Auto-generated method stub
//		 String fileName = "ENG_DF_001228_20150614_F001000DP.df.ltf.xml";
//		 GetMention(fileName,"df");
//		System.out.println(getNer("Don't you mean Chelsea Manning...?"));
//		String newsFileDir = "data" + File.separator +  "raw" + File.separator + "eng" + File.separator +  "nw";
//		String dfFileDir = "data" + File.separator + "raw" + File.separator + "eng" + File.separator +  "df";
//		processAll(newsFileDir, "news");
//		processAll(dfFileDir, "df");
		
		
		FileInputStream fis = new FileInputStream("data/engfilenamelist.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		List<String> files = (ArrayList<String>) ois.readObject();
		
		
	
		int all = files.size();
		int done = 0;
		long start = System.currentTimeMillis();
		
		FileOutputStream failedFilefos = new FileOutputStream("failedeng.tab");
		OutputStreamWriter failedFileosw = new OutputStreamWriter(failedFilefos, "UTF-8");		
		

		for(Iterator<String> iterator = files.iterator();iterator.hasNext();){
			String fileName = iterator.next();
			done += 1;
			System.out.println("doing:" + done + "\t" + "all:" + all);
			System.out.println(fileName);
			try {
//				if(fileName.endsWith("xml")){
//					System.out.println("GenMention:###########");
					engGenMention.GetMention(fileName);
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
		
		
	}
	
	
	

}
