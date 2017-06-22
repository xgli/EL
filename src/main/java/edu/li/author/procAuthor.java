/**
 * 
 */
package edu.li.author;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;

//import com.hankcs.hanlp.seg.common.Term;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;

/**
 *date:Aug 9, 2016 2:09:09 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Aug 9, 2016 2:09:09 PM
 */
public class procAuthor {

	/**
	 * @param args
	 */
	//public static final String SEGHOST = "127.0.0.1";
	//public static final int SEGPORT = 4465;
	public static final String authorinputpath = "data" + File.separator + "result" + File.separator + "result" + File.separator + "newsauthor.tab"; 
	public static final String authoroutputpath = "data" + File.separator + "result" + File.separator + "result" + File.separator + "newsauthor_new.tab"; 

	public static void getAuthor() throws IOException{
		String text = IOUtils.slurpFile(authorinputpath,"utf-8");
		FileOutputStream fos = new FileOutputStream(authoroutputpath);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
		String[] lines = text.split("\n");
		for(String line : lines){
			if(line.equals(""))
				continue;
			System.out.println(line);
//			osw.write(line + "\n");
			String[] tokens = line.split("\t");
			String rawmention = tokens[0];
			String loc = tokens[1].split(":")[1];
			String id = tokens[1].split(":")[0];
			int start = Integer.parseInt(loc.split("-")[0]);
			int end =   Integer.parseInt(loc.split("-")[1]);
			List<Term> terms = NlpAnalysis.parse(rawmention);
			int flag = 0;
			for (Term term : terms){
				
//				int flag = 0;
				if(term.getNatureStr().equals("nr")){
					flag = 1;
					String name = term.getName();
					int namestart = start + term.getOffe();
					int nameend = namestart + name.length() - 1;
					String outline = name + "\t" + id + ":" +  namestart + "-" + nameend + "\n";
//					System.out.println(name + ":" +namestart + "-" + nameend );
					osw.write(outline);
				}
//				if(0 == flag){
//					osw.write(line + "\n");
					
//				}
			}
			if ( 0 == flag){
				osw.write(line + "\n");
			}
		}
		osw.close();
		fos.close();
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
//		String text = getAnsj("我的同学叫李阳");
//		System.out.println(text);
		getAuthor();
		 
	}

}
