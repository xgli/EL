/**
 * 
 */
package edu.li.wordSegment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import edu.stanford.nlp.ie.NERServer.NERClient;

/**
 *date:Jul 24, 2016 3:58:44 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jul 24, 2016 3:58:44 PM
 */
public class AnsjSegment {
	
	public static final String SEGHOST = "127.0.0.1";
	public static final int SEGPORT = 4465;
	
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
	
	
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub

		String text = "法国巴黎《查理周刊》杂志社7日遭一伙武装人员持冲锋枪,.?+=-{}][;'|asdfdasdf火箭炮袭击，导致包括周刊主编在内的至少12人死亡，其中两人是警察，多人受伤，袭击者随后在拦截一辆车辆后逃脱，目前警方还在抓捕中。";
		String result = getAnsjSegment(text);
		System.out.println(result);
	}
	
	
	
	
	

}
