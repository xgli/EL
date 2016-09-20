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
import sun.util.locale.StringTokenIterator;


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
		String filename = "data/xmlParse/spa/df/SPA_DF_001253_20151213_G00A0HPDU.xml";
		String text = IOUtils.slurpFile(filename,"utf-8");
		String[] lines = text.split("\n");
		for (String line : lines){
			String[] tokens = line.split("\t");
			String start = tokens[0];
			String line_text = tokens[1];
			if(line_text.startsWith("\ufeff")){
				System.out.println(line.length());
				line = line.replace("\ufeff", "");
				System.out.println(line.length());				
			}
//			System.out.println(start);
//			System.out.println(line_text.replace("\u0096", " ").replace("\u0093", " ").replace("<feff>", " "));
			
		
		}
	}

}
