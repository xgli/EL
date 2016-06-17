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
	public static final String NEWSSEGMENTOUTDIR = "data" + File.separator + "segment" + File.separator + "news" + File.separator;
	
	public static final String DFFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator + "df" + File.separator;
	public static final String DFFILEOUTDIR = "data" + File.separator + "mention" + File.separator + "cmn" + File.separator + "df" + File.separator;
	public static final String DFSEGMENTOUTDIR = "data" + File.separator + "segment" + File.separator + "df" + File.separator;
	
	public static final String SEGHOST = "10.103.28.254";
	public static final int SEGPORT = 4465;
	public static final String NERHOST =  "10.103.28.254";
	public static final int NERPORT = 2310;
	
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
				 sb.append(token.split("/")[0] + "\t");
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
 
		 String segText = getAnsjSegment(text);
		 String[] lines = segText.split("\n");
		 int start = 0; //mention location the first char.
		 int end = 0;  //mention location the lastest char.			 

		 OutputStreamWriter segosw = new OutputStreamWriter(segfos, "UTF-8");
		 OutputStreamWriter nerosw = new OutputStreamWriter(nerfos, "UTF-8");
		 

		 for(String line:lines){
			 int bias = Integer.parseInt(line.split(" ")[0].trim());
			 String segLine = line.split(" ")[1];
			 segosw.write(segLine + "\n");
			 segosw.flush();
			 String ner = getNer(segLine);
			 int len = 0;
			 Pattern pattern = Pattern.compile("<(.*?)>(.*?)</.*?>");
			 Matcher matcher = pattern.matcher(ner);
			 while(matcher.find()){	 
				 start = matcher.start() - len + bias;
				 end = start + matcher.group(2).length() - 1;
				 len = len + matcher.group(1).length() * 2 + 1;
				 String mention = matcher.group(2);
				 String type = matcher.group(1);
				 String loc = start + "-" + end;
				 
//				 System.out.print(mention + "\t");
				 nerosw.write(mention + "\t");				 
//				 System.out.print(loc + "\t");
				 nerosw.write(fileName + ":" + loc + "\t");					 
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
	
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		 String fileName = "CMN_DF_000020_20150108_F00100074.df.xml";
		 GetMention(fileName,"df");
	}

}
