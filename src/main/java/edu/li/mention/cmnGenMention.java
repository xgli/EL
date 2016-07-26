/**
 * 
 */
package edu.li.mention;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;

import org.elasticsearch.common.lang3.ObjectUtils.Null;
import org.elasticsearch.common.text.StringAndBytesText;
import org.omg.CORBA.SystemException;

import com.hankcs.hanlp.HanLP;

import TraToSim.TraToSim;
import edu.li.other.testProps;
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
	
	//ansj的服务地址
	public static final String SEGHOST = "127.0.0.1";
	public static final int SEGPORT = 4465;
	//stanford的服务地址
	public static final String NERHOST =  "127.0.0.1";
	public static final int NERPORT = 2310;
	
	public static final String ABBREFILEPATH = "data" + File.separator + "dict" + File.separator + "abbre.tab";
	public static final String ABBREFILOUTEPATH = "data" + File.separator + "dict" + File.separator + "abbreOut.tab";

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
	
	public static Set<String> filterAbbre = new  HashSet<String>();
	static {
		try {
			FileOutputStream  fos = new FileOutputStream("test.tab");
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			String text = IOUtils.slurpFile(ABBREFILEPATH);
			String[] words = text.split("\n");
			for (String word : words){
				filterAbbre.add(word);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String getAnsjNER(String text)throws IOException{
	  	
		 StringReader sr = new StringReader(text);
		 BufferedReader br = new BufferedReader(sr);
		 
		 StringWriter sw = new StringWriter(); // create client writer not to write a file
		 BufferedWriter bw = new BufferedWriter(sw);

		 segServer.segClient.communicateWithsegServer(SEGHOST, SEGPORT, "utf-8",br,bw,false);
		 
		 br.close();
		 bw.close();
//		 System.out.println(sw.toString());
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
				 else if (-1 != terms[1].indexOf("ns")){
					 sb.append("<GPE>" + terms[0] + "</GPE>");
				 }
				 else if(-1 != terms[1].indexOf("nt")){
					 sb.append("<ORG>" + terms[0] + "</ORG>");
				 }
				 else if (-1 != terms[1].indexOf("nz")){
					 if(terms.equals("博科圣地")){
						 sb.append("<ORG>"+terms[0]+"</ORG>");
						 continue;
					 }
					 if(terms.equals("查理周刊")){
						 sb.append("<ORG>"+terms[0]+"</ORG>");
						 continue;
					 }
					 sb.append("<NIL>" + terms[0] + "</NIL>"); 
					 System.out.println(line);
					 System.out.println("nz" + terms[0]);
				 }
				 else if(terms[1].equals("j")){
					 if(filterAbbre.contains(terms[0])){
						 sb.append(terms[0]);						 
					 }
					 else{
						 sb.append("<GPE>" + terms[0] + "</GPE>");	
						 System.out.println("j" + ":" +  terms[0]);
					 }

//					 System.out.println(line);

				 }
				 else if(terms[1].equals("nw") && (-1 != terms[0].indexOf("·") || -1 != terms[0].indexOf("•"))){
					 sb.append("<PER>"+ terms[0] + "</PER>");
//					 System.out.println("nw" + ":" + terms[0]);
				 }
				 else if(terms[1].equals("nw")){
					 if(terms[0].equals("杰布")){
						 sb.append("<PER>"+ terms[0] + "</PER>");
						 continue;
					 }

					 System.out.println(line);
					 sb.append("<NIL>" + terms[0] + "</NIL>");
					 System.out.println("nw"+":"+terms[0]);
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

		 NERClient.communicateWithNERServer(SEGHOST, SEGPORT, "utf-8",br,bw,true);//使用stanfordner的client程序进行交互
		 
		 br.close();
		 bw.close();
		 String[] lines = sw.toString().split("\n");
		 StringBuilder sb = new StringBuilder();
		 for(String line : lines){
//			 System.out.println(line);
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
	
	public static void GetMention(String fileName,String file_type) throws IOException {//还得进行繁转简

		 String text = "";
		 FileOutputStream segfos = null;//分词输出		 
		 FileOutputStream nerfos = null;//ner输出

		
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
	 
		 
		 String fileID = fileName.split("\\.")[0];//获取文件name
		 
		 for(String line:lines){
			 
//			 boolean convertSimple = true;//进行每行判定是否为繁体
			 if(line.equals("")){
				 continue;
			 }
			 
			 int bias = Integer.parseInt(line.split("\t")[0].trim()) - 39 ;
			 String tempLine = line.split("\t")[1];
			
			 tempLine = tempLine.replaceAll("•", "·").replace("－", "·");
			 String rawLine = TraToSim.TraToSim(tempLine);//全部进行转换,然后在templine中找位置,在
//			 if(rawLine.equals(templine)){
//				 convertSimple = false;
//			 }
//			 else {
//				 System.out.println(templine);
//				 System.out.println("old:" + rawLine);
//				 System.out.println("new:" + rawLine);
//			 }
	 
			 
//			 if(rawLine.contains("《")){//打算把报刊一类的提取出来
//				 int s =rawLine.indexOf("《") + 1;
//				 int e = rawLine.indexOf("》") - 1;
//				 String m = rawLine.substring(s, e);
//				 nerosw.write(m + "\t");
//				 nerosw.write(fileID + ":" + (start + s) + '-' + (start + e) + "\t");					 
//				 nerosw.write("ORG" + "\n");
//				 nerosw.flush();//				 
//			 }			 
			 

			 String segLine = getAnsjSegment(rawLine);//ansj分词
//			 System.out.println("segline:" + segLine);
			 
			 segosw.write(segLine);
			 segosw.flush();
			 
//			 String ner = getNer(segLine);//斯坦福实体识别
			 String ner = getAnsjNER(rawLine);//ansj实体识别					 
//			 System.out.println(ner);
			 
			 int len = 0;
			 Pattern pattern = Pattern.compile("<(.*?)>(.*?)</.*?>");
			 Matcher matcher = pattern.matcher(ner);
			 while(matcher.find()){	 //考虑提取后的，标签对位置的影响   增加内嵌类型
				 start = matcher.start() - len + bias;
				 end = start + matcher.group(2).length() - 1;

				 
//				System.out.println(tempLine);
//				System.out.println(ner);
//				System.out.println(matcher.start() - len);
//				System.out.println(matcher.start() - len + matcher.group(2).length() -1);
				 
				String mention = matcher.group(2);				 
				String mentionRaw = tempLine.substring(matcher.start() - len, matcher.start() - len + matcher.group(2).length());				 

//				System.out.println(men1);
//				System.out.println(mention);
				
				 len = len + matcher.group(1).length() * 2 + 5;//过滤标签的影响

				 String type = matcher.group(1);
				 String loc = start + "-" + end;
				 
				 if(mention.contains("《") || mention.contains("》")){//过滤ner中的不规则实体
					 ;
				 }
				 else {
					 nerosw.write(mention + "\t");
					 nerosw.write(fileID + ":" + loc + "\t");					 
					 nerosw.write(type + "\n");
					 nerosw.flush();
				 }				 
//				 System.out.print(mention + "\t");
//				 System.out.print(loc + "\t");
//				 System.out.println(type + "\n");	
				 
				 //增加嵌入类型，并且过滤
//				 String ansjNER = getAnsjNER(mention);//对提取出来的mention,进行二次提取
//
//				 Pattern p = Pattern.compile("<(.*?)>(.*?)</.*?>");
//				 Matcher m = p.matcher(ansjNER);
//				 int instart = 0;
//				 int inend = 0;
//				 int inlen = 0;
//				 while(m.find()){
//					String inMention = m.group(2);
//					if(!inMention.equals(mention)){
//						instart = m.start() - inlen + start;
//						inend = instart + m.group(2).length() - 1;
//
//						String inMentionRaw = mentionRaw.substring(m.start() - inlen, m.start() - inlen + m.group(2).length());
//
//						inlen = inlen + m.group(1).length() * 2 + 5;
//						if(inMention.equals("查理")){//过滤掉查理周刊
//							continue;
//						}
//						String intype = m.group(1);
//						String inloc = instart + "-" + inend;	
////						nerosw.write(inMention + "\t");
//						nerosw.write(inMentionRaw + "\t");
//						nerosw.write(fileID + ":" + inloc + "\t");					 
//						nerosw.write(intype + "\n");
//						nerosw.flush();
//						
//						System.out.println(mention + ":" + type);
//						System.out.println("in:" + inMention + intype );
//						System.out.println("inraw:" + inMentionRaw);
//						
//						
////						System.out.println("in:######################################");
////						System.out.print(inmention + "\t");
////						System.out.print(inloc + "\t");
////						System.out.println(intype + "\n");	
//					}
//					else{ //test 国家分不开的类型 特殊类型
//						int otherstart = 0;
//						int otherend = 0;
//						String otherloc;
//						
//						if(-1 != mention.indexOf("美国") && !mention.equals("美国")){
//							otherstart = start + mention.indexOf("美国");
//							otherend = otherstart + 1;
//							otherloc = otherstart + "-" + otherend;
//							nerosw.write("美国" + "\t");
//							nerosw.write(fileID + ":" + otherloc + "\t");					 
//							nerosw.write("GPE" + "\n");
//							nerosw.flush();
//						}
//						if(-1 != mention.indexOf("中国") && !mention.equals("中国")){
//							otherstart = start + mention.indexOf("中国");
//							otherend = otherstart + 1;
//							otherloc = otherstart + "-" + otherend;
//							nerosw.write("中国" + "\t");
//							nerosw.write(fileID + ":" + otherloc + "\t");			 
//							nerosw.write("GPE" + "\n");
//							nerosw.flush();
//						}
//						if(-1 != mention.indexOf("巴西") && !mention.equals("巴西")){
//							otherstart = start + mention.indexOf("巴西");
//							otherend = otherstart + 1;
//							otherloc = otherstart + "-" + otherend;
//							nerosw.write("巴西" + "\t");
//							nerosw.write(fileID + ":" + otherloc + "\t");			 
//							nerosw.write("GPE" + "\n");
//							nerosw.flush();
//						}						
//					}
//				 }	
				 
				 //不使用内嵌类型
				int otherstart;
				int otherend;
				String otherloc;
				if(-1 != mention.indexOf("美国") && !mention.equals("美国")){
					otherstart = start + mention.indexOf("美国");
					otherend = otherstart + 1;
					otherloc = otherstart + "-" + otherend;
					nerosw.write("美国" + "\t");
					nerosw.write(fileID + ":" + otherloc + "\t");					 
					nerosw.write("GPE" + "\n");
					nerosw.flush();
				}
				if(-1 != mention.indexOf("中国") && !mention.equals("中国")){
					otherstart = start + mention.indexOf("中国");
					otherend = otherstart + 1;
					otherloc = otherstart + "-" + otherend;
					nerosw.write("中国" + "\t");
					nerosw.write(fileID + ":" + otherloc + "\t");			 
					nerosw.write("GPE" + "\n");
					nerosw.flush();
				}
				if(-1 != mention.indexOf("巴西") && !mention.equals("巴西")){
					otherstart = start + mention.indexOf("巴西");
					otherend = otherstart + 1;
					otherloc = otherstart + "-" + otherend;
					nerosw.write("巴西" + "\t");
					nerosw.write(fileID + ":" + otherloc + "\t");			 
					nerosw.write("GPE" + "\n");
					nerosw.flush();
				}
				if(-1 != mention.indexOf("英国") && !mention.equals("英国")){
					otherstart = start + mention.indexOf("英国");
					otherend = otherstart + 1;
					otherloc = otherstart + "-" + otherend;
					nerosw.write("英国" + "\t");
					nerosw.write(fileID + ":" + otherloc + "\t");			 
					nerosw.write("GPE" + "\n");
					nerosw.flush();
				}
				if(-1 != mention.indexOf("德国") && !mention.equals("德国")){
					otherstart = start + mention.indexOf("德国");
					otherend = otherstart + 1;
					otherloc = otherstart + "-" + otherend;
					nerosw.write("德国" + "\t");
					nerosw.write(fileID + ":" + otherloc + "\t");			 
					nerosw.write("GPE" + "\n");
					nerosw.flush();
				}
				//不使用内嵌类型			 		 		 
				 
		 	 }
		 }
		 nerosw.close();
		 nerfos.close(); 
		 segosw.close();
		 segfos.close();		
	}
	
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		 String fileName = "CMN_NW_001346_20150616_F0010001R.nw.ltf.xml";
		 GetMention(fileName,"news");
////		String text = "法国巴黎《查理周刊》杂志社7日遭一伙武装人员持冲锋枪,.?+=-{}][;'|asdfdasdf火箭炮袭击，导致包括周刊主编在内的至少12人死亡，其中两人是警察，多人受伤，袭击者随后在拦截一辆车辆后逃脱，目前警方还在抓捕中。";
////		String result =  getAnsjNER(text);
//		String text = ",";
//		String result = getAnsjSegment(text);
//		System.out.println(result);
	}

}
