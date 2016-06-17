/**
 * 
 */
package edu.li.result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.ejml.alg.dense.linsol.WrapLinearSolverBlock64;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;

/**
 *date:Jun 17, 2016 4:31:04 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 4:31:04 PM
 */
public class mergerResult {
	
	public static final String DFRESULTINPUTDIR = "data" + File.separator + "candidate" + File.separator + "cmn" + File.separator +"df" + File.separator;
	public static final String NEWSRESULTINPUTDIR = "data" + File.separator + "candidate" + File.separator + "cmn" + File.separator +"news" + File.separator;
	public static final String AUTHORRESULTINPUTDIR = "data" + File.separator + "result" + File.separator + "author" + File.separator;
	public static final String RESULTFINAL = "data" + File.separator + "result" + File.separator + "result.tab";
	
	public static void main(String[] args) throws IOException{
		
		Map<String,String> NILMention = new HashMap<String, String>();
		String text = null;
		int count = 0;
		int nil = 0;
//		File file = null;
		 FileOutputStream fos = new FileOutputStream(RESULTFINAL);
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		
		File newsDir = new File(NEWSRESULTINPUTDIR);
		File[] newsFiles = newsDir.listFiles();
		for(File file:newsFiles){			
			text = IOUtils.slurpFile(file);
			String[] lines = text.split("\n");
			for(String line : lines){				
				String[] tokens = line.split("\t");
				if(tokens[2].equals("NIL")){
					String mention = tokens[0];
					if(NILMention.containsKey(mention)){
						osw.write("li" + "\t" + "TEDL15_" + count + "\t" +line.trim().replace("NIL", NILMention.get(mention)) + "\tNAM\t1.0\n");
						
					}
					else{
						osw.write("li" + "\t" + "TEDL15_" + count + "\t" +line.trim().replace("NIL", "NIL"+nil) + "\tNAM\t1.0\n" );
						NILMention.put(mention, "NIL"+nil);
						nil++;
					}
				}
				else{
					osw.write("li" + "\t" + "TEDL15_" + count + "\t" +line.trim() +  "\tNAM\t1.0\n");
//					osw.write("li" + "\t" + "TEDL15_" + count + line.trim() + "\t" + "NAM" + "\t" +"1.0" + "\n");
				}
				count++;
			}
//			System.out.println(text);
		}
				
		
		File dfDir = new File(DFRESULTINPUTDIR);
		File[] dfFiles = dfDir.listFiles();
		for(File file:dfFiles){			
			text = IOUtils.slurpFile(file);
			String[] lines = text.split("\n");
			for(String line : lines){				
				String[] tokens = line.split("\t");
				if(tokens[2].equals("NIL")){
					String mention = tokens[0];
					if(NILMention.containsKey(mention)){
						osw.write("li" + "\t" + "TEDL15_" + count + "\t" +line.trim().replace("NIL", NILMention.get(mention)) + "\tNAM\t1.0\n");
						
					}
					else{
						osw.write("li" + "\t" + "TEDL15_" + count + "\t" +line.trim().replace("NIL", "NIL"+nil) + "\tNAM\t1.0\n" );
						NILMention.put(mention, "NIL"+nil);
						nil++;
					}

				}
				else{
					osw.write("li" + "\t" + "TEDL15_" + count + "\t" +line.trim() +  "\tNAM\t1.0\n");
//					osw.write("li" + "\t" + "TEDL15_" + count + line.trim() + "\t" + "NAM" + "\t" +"1.0" + "\n");
				}
				count++;
			}
//			System.out.println(text);
		}
				
		
		File authorDir = new File(AUTHORRESULTINPUTDIR);
		File[] authorFiles = authorDir.listFiles();
		for(File file:authorFiles){			
			text = IOUtils.slurpFile(file);
			String[] lines = text.split("\n");
			for(String line : lines){				
				String[] tokens = line.split("\t");			
				String mention = tokens[0];
				if(NILMention.containsKey(mention)){
					osw.write("li" + "\t" + "TEDL15_" + count + "\t" + line.trim() + "\t" + NILMention.get(mention) + "\tPER\tNAM\t1.0\n");
					
				}
				else{
					osw.write("li" + "\t" + "TEDL15_" + count + "\t" +line.trim() + "\t" + "NIL" + nil+ "\tPER\tNAM\t1.0\n" );
					NILMention.put(mention, "NIL"+nil);
					nil++;
				}
				count++;
			}

			
//			System.out.println(text);
		}
				
		
		osw.close();
		fos.close();
	}

	
	
	
	

}
