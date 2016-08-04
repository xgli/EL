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
 *date:Jun 17, 2016 9:24:05 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 9:24:05 AM
 */
public class engGenMention {

	public static final String NERHOST =  "127.0.0.1";
	public static final int NERPORT = 2314;
	
	public static final String DFFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "eng" + File.separator + "df" + File.separator;
	public static final String NEWSFILEINPUTDIR = "data" + File.separator  +  "xmlParse" + File.separator + "eng" + File.separator + "news" + File.separator;
	public static final String MENTIONFILEOUTDIR = "data" + File.separator + "mention" + File.separator + "eng" + File.separator;
	public static final String MENTIONTEXTOUTDIR = "data" + File.separator + "mentionText" + File.separator + "eng" +File.separator;

	
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
	   	 StringReader sr = new StringReader(text); //输入切分好的text
		 BufferedReader br = new BufferedReader(sr);
		 StringWriter sw = new StringWriter();
		 BufferedWriter bw = new BufferedWriter(sw);
		 NERClient.communicateWithNERServer(NERHOST, NERPORT, "UTF-8",br,bw,false);
		 bw.close();
		 br.close();
//		 String ner = sw.toString().replaceAll("\t", "").replaceAll("LOC", "GPE").replaceAll("PERSON", "PER");
		 String ner = sw.toString().replaceAll("PERSON>", "PER>").replaceAll("LOCATION>", "GPE>");
//		 return  ner.replaceAll("MISC", "NIL");//这一类有点特殊。
		 return ner.replaceAll("ORGANIZATION>", "ORG>").replaceAll("<MISC>", "").replaceAll("</MISC>", "");
	}
	
	public static void GetMention(String fileName,String file_type) throws IOException {
		 
		 String text = "";
		 


		 FileOutputStream segfos = new FileOutputStream(MENTIONTEXTOUTDIR + fileName);
		 FileOutputStream nerfos = new FileOutputStream(MENTIONFILEOUTDIR + fileName);;
		 if(file_type.equals("news")){
			 text = IOUtils.slurpFile(NEWSFILEINPUTDIR + fileName);
//			 nerfos = new FileOutputStream(NEWSFILEOUTDIR + fileName);

		 }
		 else {
			 text = IOUtils.slurpFile(DFFILEINPUTDIR + fileName);	
//			 nerfos = new FileOutputStream(DFFILEOUTDIR + fileName);
//			 segfos = new FileOutputStream(DFSEGMENTOUTDIR + fileName);
		 } 

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
			 
			 pattern = Pattern.compile("<(PER)>(.*?)</PER>");
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
			 
			 pattern = Pattern.compile("<(GPE)>(.*?)</GPE>");
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
			 pattern = Pattern.compile("<(ORG)>(.*?)</ORG>");
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
			 
			 pattern = Pattern.compile("<(LOC)>(.*?)</LOC>");
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
			 pattern = Pattern.compile("<(FAC)>(.*?)</FAC>");
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
	
	/**
	 * @param string
	 * @return
	 */
	private static FileOutputStream FileOutputStream(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		 String fileName = "ENG_DF_001228_20150614_F001000DP.df.ltf.xml";
		 GetMention(fileName,"df");
//		System.out.println(getNer("Don't you mean Chelsea Manning...?"));
	}
	
	
	

}
