/**
 * 
 */
package edu.li.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.google.common.primitives.Bytes;

import edu.li.other.testProps;
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

	public static void main(String[] args) throws IOException {
//		Set<String> filterAbbre = new  HashSet<String>();
//
//		 FileOutputStream  fos = new FileOutputStream("test.tab");
//		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
//		for(String element : filterAbbre){
//			osw.write(element + "\n");
//			osw.flush();
//		}
//		osw.close();
//		fos.close();
		String text = "中国";
		char[] words = text.toCharArray();
		for(char word : words){
			System.out.println(word+"a");
		}
	}

}
