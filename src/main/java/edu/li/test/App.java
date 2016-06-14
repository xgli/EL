/**
 * 
 */
package edu.li.test;

import java.awt.SystemTray;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ejml.alg.dense.linsol.qr.LinearSolverQrHouseTran;

import edu.li.wordSegment.segServer;
import edu.stanford.nlp.ie.NERServer.NERClient;
import edu.stanford.nlp.io.IOUtils;


/**
 *date:Jun 12, 2016 8:57:10 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 12, 2016 8:57:10 AM
 */
public class App {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static String getAnsjSegment(String text) throws IOException{
	  	 StringReader sr = new StringReader(text);
		 BufferedReader br = new BufferedReader(sr);
		 
		 StringWriter sw = new StringWriter(); // create client writer not to write a file
		 BufferedWriter bw = new BufferedWriter(sw);

		segServer.segClient.communicateWithsegServer("10.103.28.254", 4465 ,"utf-8",br,bw,false); 
		 br.close();
		 bw.close();
		 String[] lines = sw.toString().split("\n");
		 StringBuilder sb = new StringBuilder();
		 for(String line : lines){
//			 System.out.println(line);
//			 System.out.println("line:" + line.substring(1, line.length()-1));
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
		 NERClient.communicateWithNERServer("10.103.28.254", 2310, "UTF-8", br, bw, false); 
		 bw.close();
		 br.close();
		 String ner = sw.toString().replaceAll("\t", "").replaceAll("LOC", "GPE").replaceAll("PERSON", "PER");
		 return  ner.replaceAll("MISC", "NIL");//这一类有点特殊。
	}
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		 String text = IOUtils.slurpFile("raw_num.txt");
//		 String str = "美国总统发表演讲。杜甫是的诗人,石家庄位于黑龙江省。华盛顿位于美国。林光是一名伟大的球员,李白是诗人。清华大学的学生。北京大学生在一起";
		 FileOutputStream fos = new FileOutputStream("mention.txt");
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		 int start = 0; //mention location the first char.
		 int end = 0;  //mention location the lastest char.		 
		 String segText = getAnsjSegment(text);
		 String[] lines = segText.split("\n");
		 for(String line:lines){
			 int bias = Integer.parseInt(line.split(" ")[0].trim());
			 String ner = getNer(line.split(" ")[1]);
			 int len = 0;
			 Pattern pattern = Pattern.compile("(<.*?>)(.*?)</.*?>");
			 Matcher matcher = pattern.matcher(ner);
			 while(matcher.find()){	 
				 start = matcher.start() - len + bias;
				 end = start + matcher.group(2).length() - 1;
				 len = len + matcher.group(1).length() * 2 + 1;
				 String mention = matcher.group(2);
				 String type = matcher.group(1);
				 String loc = start + "-" + end;
				 System.out.print(mention + "\t");
				 osw.write(mention + "\t");
				 System.out.print(type + "\t");
				 osw.write(type + "\t");
				 System.out.println(loc);
				 osw.write(loc + "\n");
				 osw.flush();
			 }
		 }
		 osw.close();
		 fos.close();	 	 	 

	}

}
