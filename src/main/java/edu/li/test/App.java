/**
 * 
 */
package edu.li.test;

import java.io.IOException;
import java.util.List;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;


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
		
		String str = "北京是中国的一个城市。";
		 List<Term> terms = NlpAnalysis.parse(str);
		 String seg = "";
		 for (Term term : terms){
			 seg += term.getName() + "\t";
		 }
		 seg = seg.trim();
		 System.out.println(seg);
//		String filename = "data/xmlParse/spa/df/SPA_DF_001253_20151213_G00A0HPDU.xml";
//		String text = IOUtils.slurpFile(filename,"utf-8");
//		String[] lines = text.split("\n");
//		for (String line : lines){
//			String[] tokens = line.split("\t");
//			String start = tokens[0];
//			String line_text = tokens[1];
//			if(line_text.startsWith("\ufeff")){
//				System.out.println(line.length());
//				line = line.replace("\ufeff", "");
//				System.out.println(line.length());				
//			}
//			System.out.println(start);
//			System.out.println(line_text.replace("\u0096", " ").replace("\u0093", " ").replace("<feff>", " "));
			
		
		
	}

}
