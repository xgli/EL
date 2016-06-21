/**
 * 
 */
package edu.li.mention;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.elasticsearch.common.lang3.ObjectUtils.Null;

import edu.li.wordSegment.segServer;
import edu.stanford.nlp.ie.NERServer.NERClient;
import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jun 17, 2016 9:24:51 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 9:24:51 AM
 */
public class cmnGenMention {
	
	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator + "news" + File.separator;
	public static final String NEWSFILEOUTDIR = "data" + File.separator + "mention" + File.separator + "cmn" + File.separator + "news" + File.separator;
	public static final String NEWSSEGMENTOUTDIR = "data" + File.separator + "segment" + File.separator + "cmn" + File.separator + "news" + File.separator;
	
	public static final String DFFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator + "df" + File.separator;
	public static final String DFFILEOUTDIR = "data" + File.separator + "mention" + File.separator + "cmn" + File.separator + "df" + File.separator;
	public static final String DFSEGMENTOUTDIR = "data" + File.separator + "segment" + File.separator  + "cmn" + File.separator + "df" + File.separator;
	
	public static final String SEGHOST = "127.0.0.1";
	public static final int SEGPORT = 4465;
	public static final String NERHOST =  "127.0.0.1";
	public static final int NERPORT = 2310;
	
	public static String getAnsjNER(String text)throws IOException{
	  	
		StringReader sr = new StringReader(text);
		 BufferedReader br = new BufferedReader(sr);
		 
		 StringWriter sw = new StringWriter(); // create client writer not to write a file
		 BufferedWriter bw = new BufferedWriter(sw);

		 segServer.segClient.communicateWithsegServer(SEGHOST, SEGPORT, "utf-8",br,bw,false);
		 
		 br.close();
		 bw.close();
		 
		 String[] lines = sw.toString().split("\n");
		 StringBuilder sb = new StringBuilder();
		 for(String line : lines){	
//			 System.out.println(line);
			 line = line.substring(1,line.length()-1);			 
			 String[] tokens = line.split(", ");//只能先隔断词语了。
			 			
			 for(String token : tokens){
//				 System.out.println(token);
				
				 if(token.equals("/")){//过滤这个只有/
					 sb.append("/");
					 continue;
				 }
				 
				 if(-1 == token.indexOf("/")){ //没有词性
					 sb.append(token);
					 continue;
				 }
				 
				 String[] terms = token.split("/");					 

				 if(-1 != terms[1].indexOf("nr")){
					 sb.append("<PER>"+ terms[0] + "</PER>");
				 }
				 else if (-1 != terms[1].indexOf("ns") || -1 != terms[1].indexOf("j")){
					 sb.append("<GPE>" + terms[0] + "</GPE>");
				 }
				 else if(-1 != terms[1].indexOf("nt")){
					 sb.append("<ORG>" + terms[0] + "</ORG>");
				 }
				 else if(-1 != terms[1].indexOf("j")){
					 sb.append("<GPE>" + terms[0] + "</GPE>");
				 }
				 else {
					 sb.append(terms[0]); 						 
				 }					 
			 }
		 } 
		 return sb.toString();		
	}
	
	public static String getAnsjSegment(String text) throws IOException{
	  	 StringReader sr = new StringReader(text);
		 BufferedReader br = new BufferedReader(sr);
		 
		 StringWriter sw = new StringWriter(); // create client writer not to write a file
		 BufferedWriter bw = new BufferedWriter(sw);

		 segServer.segClient.communicateWithsegServer(SEGHOST, SEGPORT, "utf-8",br,bw,false);
		 
		 br.close();
		 bw.close();
		 String[] lines = sw.toString().split("\n");
		 StringBuilder sb = new StringBuilder();
		 for(String line : lines){
			 line = line.substring(1,line.length()-1);
			 String[] tokens = line.split(", ");
			 for(String token : tokens){
//				 System.out.println(token);
				 if(token.equals("/")){
					 sb.append("/\t");
					 continue;
				 }
				 
				 if(-1 == token.indexOf("/")){
					 sb.append(token + "\t");
					 continue;
				 }
				 
				 String[] terms = token.split("/");
				 sb.append(terms[0] + "\t"); 

			 }
			 sb.append("\n");
		 } 
		 return sb.toString();		
	}
	
	public static String getNer(String text) throws IOException{
	   	 StringReader sr = new StringReader(text); //输入切分好的text
		 BufferedReader br = new BufferedReader(sr);
		 StringWriter sw = new StringWriter();
		 BufferedWriter bw = new BufferedWriter(sw);
		 NERClient.communicateWithNERServer(NERHOST, NERPORT, "UTF-8",br,bw,false);
		 bw.close();
		 br.close();
//		 String ner = sw.toString().replaceAll("\t", "").replaceAll("LOC", "GPE").replaceAll("PERSON", "PER");
		 String ner = sw.toString().replaceAll("\t", "").replaceAll("PERSON", "PER");
		 return  ner.replaceAll("MISC", "NIL");//这一类有点特殊。
	}
	
	public static void GetMention(String fileName,String file_type) throws IOException {
//		 String fileName = "CMN_NW_000020_20150604_F00100013.nw.xml";
		 String text = "";
		 FileOutputStream segfos = null;
//		 OutputStreamWriter segosw = null;
		 
		 FileOutputStream nerfos = null;
//		 OutputStreamWriter nerosw = new OutputStreamWriter(nerfos, "UTF-8");
		
		 if(file_type.equals("news")){
			 text = IOUtils.slurpFile(NEWSFILEINPUTDIR + fileName);
			 segfos = new FileOutputStream(NEWSSEGMENTOUTDIR + fileName);
			 nerfos = new FileOutputStream(NEWSFILEOUTDIR + fileName);
		 }
		 else {
			 text = IOUtils.slurpFile(DFFILEINPUTDIR + fileName);	
			 segfos = new FileOutputStream(DFSEGMENTOUTDIR + fileName);
			 nerfos = new FileOutputStream(DFFILEOUTDIR + fileName);
		 }
 
		 String[] lines = text.split("\n");//以行进行处理
		 int start = 0; //mention location the first char.
		 int end = 0;  //mention location the lastest char.			 

		 OutputStreamWriter segosw = new OutputStreamWriter(segfos, "UTF-8");
		 OutputStreamWriter nerosw = new OutputStreamWriter(nerfos, "UTF-8");
		 
		 String fileID = fileName.split("\\.")[0];
		 for(String line:lines){
			 int bias = Integer.parseInt(line.split("\t")[0].trim()) - 39 ;
//			 System.out.println(bias);
			 String rawLine = line.split("\t")[1];
			 String segLine = getAnsjSegment(rawLine);
//			 System.out.println("segline:" + segLine);
			 
			 segosw.write(segLine);
			 segosw.flush();
			 
			 String ner = getNer(segLine);
//			 System.out.println(ner);
			 int len = 0;
			 Pattern pattern = Pattern.compile("<(.*?)>(.*?)</.*?>");
			 Matcher matcher = pattern.matcher(ner);
			 while(matcher.find()){	 //考虑提取后的，标签对位置的影响   增加内嵌类型
				 start = matcher.start() - len + bias;
				 end = start + matcher.group(2).length() - 1;
				 len = len + matcher.group(1).length() * 2 + 5;
				 String mention = matcher.group(2);
				 String type = matcher.group(1);
				 String loc = start + "-" + end;	
				 
				 nerosw.write(mention + "\t");
				 nerosw.write(fileID + ":" + loc + "\t");					 
				 nerosw.write(type + "\n");
				 nerosw.flush();
				 
//				 System.out.print(mention + "\t");
//				 System.out.print(loc + "\t");
//				 System.out.println(type + "\n");	
				 
				 //增加嵌入类型，并且过滤
				 String ansjNER = getAnsjNER(mention);
				 Pattern p = Pattern.compile("<(.*?)>(.*?)</.*?>");
				 Matcher m = p.matcher(ansjNER);
				 int instart = 0;
				 int inend = 0;
				 int inlen = 0;
				 while(m.find()){
					String men = m.group(2);
					if(!men.equals(mention)){
						instart = m.start() - inlen + start;
						inend = instart + m.group(2).length() - 1;
						inlen = inlen + m.group(1).length() * 2 + 5;
						String inmention = m.group(2);
						String intype = m.group(1);
						String inloc = instart + "-" + inend;	
						nerosw.write(inmention + "\t");
						nerosw.write(fileID + ":" + inloc + "\t");					 
						nerosw.write(intype + "\n");
						nerosw.flush();
//						System.out.println("in:######################################");
//						System.out.print(inmention + "\t");
//						System.out.print(inloc + "\t");
//						System.out.println(intype + "\n");	
					}
				 }				 
				 			 
		 			 
			 }
		 }
		 nerosw.close();
		 nerfos.close(); 
		 segosw.close();
		 segfos.close();		
	}
	
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		 String fileName = "CMN_NW_001147_20150116_F0000005F.ltf.xml";
		 GetMention(fileName,"news");
	}

}
