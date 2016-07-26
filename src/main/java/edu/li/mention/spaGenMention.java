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

import edu.stanford.nlp.ie.NERServer.NERClient;
import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jun 17, 2016 9:24:27 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 9:24:27 AM
 */
public class spaGenMention {

	public static final String NERHOST =  "127.0.0.1";
	public static final int NERPORT = 2311;
	
	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "spa" + File.separator + "news" + File.separator;
	public static final String NEWSFILEOUTDIR = "data" + File.separator + "mention" + File.separator + "spa" + File.separator + "news" + File.separator;
	public static final String NEWSSEGMENTOUTDIR = "data" + File.separator + "segment" + File.separator + "spa" +File.separator+"news" + File.separator;
	
	public static final String DFFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "spa" + File.separator + "df" + File.separator;
	public static final String DFFILEOUTDIR = "data" + File.separator + "mention" + File.separator + "spa" + File.separator + "df" + File.separator;
	public static final String DFSEGMENTOUTDIR = "data" + File.separator + "segment" + File.separator  + "spa" + File.separator + "df" + File.separator;
	
	static{//判断文件目录是否存在
		File file;
		file = new File(DFFILEOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
		file = new File(DFSEGMENTOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
		file = new File(NEWSFILEOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
		file = new File(NEWSSEGMENTOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
	}	
	
	public static String getNer(String text) throws IOException{
	   	 StringReader sr = new StringReader(text); //输入切分好的text
		 BufferedReader br = new BufferedReader(sr);
		 StringWriter sw = new StringWriter();
		 BufferedWriter bw = new BufferedWriter(sw);
		 NERClient.communicateWithNERServer(NERHOST, NERPORT, "UTF-8",br,bw,false);
		 bw.close();
		 br.close();
		 String ner = sw.toString().replaceAll("LUG>", "GPE>").replaceAll("PERS>", "PER>");
		 //		 return  ner.replaceAll("MISC", "NIL");//这一类有点特殊。
		 return ner.replaceAll("OTROS>", "NIL>");
	}
	
	public static void GetMention(String fileName,String file_type) throws IOException {
		 
		 String text = "";
		 FileOutputStream segfos = null;		 
		 FileOutputStream nerfos = null;

		
		 if(file_type.equals("news")){
			 text = IOUtils.slurpFile(NEWSFILEINPUTDIR + fileName);
			 nerfos = new FileOutputStream(NEWSFILEOUTDIR + fileName);
			 segfos = new FileOutputStream(NEWSSEGMENTOUTDIR + fileName);
		 }
		 else {
			 text = IOUtils.slurpFile(DFFILEINPUTDIR + fileName);	
			 nerfos = new FileOutputStream(DFFILEOUTDIR + fileName);
			 segfos = new FileOutputStream(DFSEGMENTOUTDIR + fileName);
		 } 

		 String[] lines = text.split("\n");
		 int start = 0; //mention location the first char.
		 int end = 0;  //mention location the lastest char.	
		 
		 OutputStreamWriter segosw = new OutputStreamWriter(segfos, "UTF-8");
		 OutputStreamWriter nerosw = new OutputStreamWriter(nerfos, "UTF-8");
		 
		 String fileID = fileName.split("\\.")[0];
		 for(String line:lines){
//			 System.out.println(line);
			 int bias = Integer.parseInt(line.split("\t")[0].trim()) - 39;
			 String segLine = line.split("\t")[1];
			 
			 segosw.write(segLine);
			 segosw.write("\n");
			 segfos.flush();
			 
			 String ner = getNer(segLine);
//			 System.out.println(ner);
			 int len = 0;
			 Pattern pattern = Pattern.compile("<(.*?)>(.*?)</.*?>");
			 Matcher matcher = pattern.matcher(ner);
			 while(matcher.find()){	 //考虑提取后的，标签对位置的影响
				 start = matcher.start() - len + bias;
				 end = start + matcher.group(2).length() - 1;
				 len = len + matcher.group(1).length() * 2 + 5;
				 String mention = matcher.group(2);
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
	
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		 String fileName = "ENG_DF_000170_20150322_F00000082.df.ltf.xml";
		 GetMention(fileName,"df");
	}
	
	
	
	
}
