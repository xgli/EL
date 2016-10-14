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

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.dom4j.DocumentException;

import TraToSim.TraToSim;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
//import TraToSim.TraTosim;
/**
 *date:Jun 17, 2016 9:24:51 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 9:24:51 AM
 */
public class cmnGenMention {
	
	public static final String FILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator;
//	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator + "news" + File.separator;
	public static final String MENTIONFILEOUTDIR = "data" + File.separator + "mention" + File.separator + "cmn"+ File.separator;
	public static final String MENTIONTEXTOUTDIR = "data" + File.separator + "mentionText" + File.separator + "cmn" + File.separator;
	
	public static final String serializedClassifier = "classifiers/chinese.misc.distsim.crf.ser.gz";
	public static AbstractSequenceClassifier<CoreLabel> ner;
	
	static {	
			ner = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	}     
	
//	//ansj的服务地址
//	public static final String SEGHOST = "127.0.0.1";
//	public static final int SEGPORT = 4465;
//	//stanford的服务地址
//	public static final String NERHOST =  "127.0.0.1";
//	public static final int NERPORT = 2310;
	

	static{//判断文件目录是否存在
		File file;
		file = new File(MENTIONFILEOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
		file = new File(MENTIONTEXTOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
	}	
	

	
	public static String getAnsjSegment(String text) throws IOException{
		List<Term> terms = NlpAnalysis.parse(text);
		 String seg = "";
		 for (Term term : terms){
			 seg += term.getName() + "\t";
		 }
		 seg = seg.trim();
		 System.out.println(seg);
		 return seg;
//	  	 StringReader sr = new StringReader(text);
//		 BufferedReader br = new BufferedReader(sr);
//		 
//		 StringWriter sw = new StringWriter(); // create client writer not to write a file
//		 BufferedWriter bw = new BufferedWriter(sw);
//
//		 NERClient.communicateWithNERServer(SEGHOST, SEGPORT, "utf-8",br,bw,true);//使用stanfordner的client程序进行交互
//		 
//		 br.close();
//		 bw.close();
	}
	
	public static String getNer(String text) throws IOException{
		String  str = ner.classifyWithInlineXML(text);
		 str = str.replaceAll("\t", "").replace("PERSON>", "PER>");
		 str = str.replaceAll("</PER>·<PER>", "·");
		 return  str.replace("<MISC>", "").replace("</MISC>", "");//这一类有点特殊。
		
//	   	 StringReader sr = new StringReader(text); //输入切分好的text
//		 BufferedReader br = new BufferedReader(sr);
//		 StringWriter sw = new StringWriter();
//		 BufferedWriter bw = new BufferedWriter(sw);
//		 NERClient.communicateWithNERServer(NERHOST, NERPORT, "UTF-8",br,bw,false);
//		 bw.close();
//		 br.close();
////		 String ner = sw.toString().replaceAll("\t", "").replaceAll("LOC", "GPE").replaceAll("PERSON", "PER");
//		 String ner = sw.toString().replaceAll("\t", "").replace("PERSON>", "PER>");
//		 ner = ner.replaceAll("</PER>·<PER>", "·");
//		 return  ner.replace("<MISC>", "").replace("</MISC>", "");//这一类有点特殊。
	}
	
	public static void GetMention(String fileName) throws IOException {//还得进行繁转简

		 String text = "";

		 FileOutputStream nerfos = new FileOutputStream(MENTIONFILEOUTDIR + fileName);

		 FileOutputStream segfos = new FileOutputStream(MENTIONTEXTOUTDIR + fileName);
		 text = IOUtils.slurpFile(FILEINPUTDIR + fileName);
//		 if(file_type.equals("news")){
//			 text = IOUtils.slurpFile(NEWSFILEINPUTDIR + fileName);
//		 }
//		 else {
//			 text = IOUtils.slurpFile(DFFILEINPUTDIR + fileName);
//		 }
 
		 String[] lines = text.split("\n");//以行进行处理
		 int start = 0; //mention location the first char.
		 int end = 0;  //mention location the lastest char.			 

		 OutputStreamWriter segosw = new OutputStreamWriter(segfos, "UTF-8");
		 OutputStreamWriter nerosw = new OutputStreamWriter(nerfos, "UTF-8");
	 
		 
		 String fileID = fileName.replace(".xml", "");//获取文件name
		 
		 for(String line:lines){			 

			 if(line.equals("")){
				 continue;
			 }
			 
			 int bias = Integer.parseInt(line.split("\t")[0].trim());
			 String tempLine = line.split("\t")[1];
			
			 tempLine = tempLine.replaceAll("•", "·").replace("－", "·");
			 String rawLine = TraToSim.TraToSim(tempLine);//全部进行转换,然后在templine中找位置,在
//		 	 String rawLine = tempLine;


			 String segLine = getAnsjSegment(rawLine);//ansj分词
//			 System.out.println("segline:" + segLine);
			 
			 segosw.write(segLine);
			 segosw.flush();
			 
			 String ner = getNer(segLine);//斯坦福实体识别
//			 String ner = getAnsjNER(rawLine);//ansj实体识别					 
//			 System.out.println(ner);
//			 System.out.println(ner);
			 int len = 0;
			 Pattern pattern;
			 Matcher matcher;
			 pattern = Pattern.compile("<(GPE|ORG|PER|LOC|FAC)>(.*?)(</GPE>|</ORG>|</PER>|</LOC>|</FAC>)");
			 matcher = pattern.matcher(ner);
			 while(matcher.find()){	 //考虑提取后的，标签对位置的影响   增加内嵌类型
//				 System.out.println(matcher.group(2));
//				 System.out.println(matcher.start(2));
//				 System.out.println(matcher.end(2));
				 start = matcher.start() - len + bias;
				 end = start + matcher.group(2).length() - 1;			 

				 String mention = matcher.group(2);					 
				
				 len = len + matcher.group(1).length() * 2 + 5;//过滤标签的影响

				 String type = matcher.group(1);
				 String loc = start + "-" + end;		 
		
				 nerosw.write(mention + "\t");
				 nerosw.write(fileID + ":" + loc + "\t");					 
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
						System.out.println("GenMention:###########");
						cmnGenMention.GetMention(fileName);
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
	
	
	public static void main(String[] args) throws IOException, DocumentException, ClassNotFoundException {
		
		// TODO Auto-generated method stub
		
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
				if(fileName.endsWith("xml")){
//					System.out.println("GenMention:###########");
					cmnGenMention.GetMention(fileName);
				}
				
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
