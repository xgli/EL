/**
 * 
 */
package edu.li.test;

import java.awt.SystemTray;
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

import javax.sound.sampled.Port;

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
	public static final String FILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator;
	public static final String FILEOUTDIR = "data" + File.separator + "mention" + File.separator;
	public static final String SEGMENTOUTDIR = "data" + File.separator + "segment" + File.separator;
	
	public static String getAnsjSegment(String text, String host, int port) throws IOException{
	  	 StringReader sr = new StringReader(text);
		 BufferedReader br = new BufferedReader(sr);
		 
		 StringWriter sw = new StringWriter(); // create client writer not to write a file
		 BufferedWriter bw = new BufferedWriter(sw);

		 segServer.segClient.communicateWithsegServer(host, port, "utf-8",br,bw,false);
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
	
	public static String getNer(String text, String host, int port) throws IOException{
	   	 StringReader sr = new StringReader(text); //输入切分好的text
		 BufferedReader br = new BufferedReader(sr);
		 StringWriter sw = new StringWriter();
		 BufferedWriter bw = new BufferedWriter(sw);
		 NERClient.communicateWithNERServer(host, port, "UTF-8",br,bw,false);
		 bw.close();
		 br.close();
		 String ner = sw.toString().replaceAll("\t", "").replaceAll("LOC", "GPE").replaceAll("PERSON", "PER");
		 return  ner.replaceAll("MISC", "NIL");//这一类有点特殊。
	}
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		 String fileName = "CMN_NW_000020_20150604_F00100013.nw.xml";
		 String text = IOUtils.slurpFile(FILEINPUTDIR + fileName);	 
		 String segText = getAnsjSegment(text,"10.103.28.254",4465);
		 String[] lines = segText.split("\n");
		 int start = 0; //mention location the first char.
		 int end = 0;  //mention location the lastest char.	
		 
		 FileOutputStream segfos = new FileOutputStream(SEGMENTOUTDIR + fileName);
		 OutputStreamWriter segosw = new OutputStreamWriter(segfos, "UTF-8");
		 
		 FileOutputStream nerfos = new FileOutputStream(FILEOUTDIR + fileName);
		 OutputStreamWriter nerosw = new OutputStreamWriter(nerfos, "UTF-8");
		 for(String line:lines){
			 int bias = Integer.parseInt(line.split(" ")[0].trim());
			 String segLine = line.split(" ")[1];
			 segosw.write(segLine + "\n");
			 segosw.flush();
			 String ner = getNer(segLine,"10.103.28.254",2310);
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
				 
				 System.out.print(mention + "\t");
				 nerosw.write(mention + "\t");				 
				 System.out.print(loc + "\t");
				 nerosw.write(fileName + ":" + loc + "\t");					 
				 System.out.println(type);
				 nerosw.write(type + "\n");


				 nerosw.flush();
			 }
		 }
		 nerosw.close();
		 nerfos.close(); 
		 segosw.close();
		 segfos.close();
	}

}
