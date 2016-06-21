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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		
		String text = "/"; //		text = text.replaceAll(".*?\"(.*?)\".*?", )
		String[] tokens  = text.split("/");
		System.out.println(tokens.length);
		for(String token : tokens){
			System.out.println(token);
		}
		
	}

}
