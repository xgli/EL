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

import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jun 18, 2016 10:08:41 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 18, 2016 10:08:41 PM
 *合并中文的结果
 */
public class cmnMergerResult {
	public static final String DFRESULTINPUTDIR = "data" + File.separator + "candidate" + File.separator + "cmn" + File.separator +"df" + File.separator;
	public static final String NEWSRESULTINPUTDIR = "data" + File.separator + "candidate" + File.separator + "cmn" + File.separator +"news" + File.separator;
	public static final String AUTHORRESULTINPUTDIR = "data" + File.separator + "result" + File.separator + "author" + File.separator + "cmn" + File.separator;
	public static final String RESULTFINAL = "data" + File.separator + "result" + File.separator + "cmn_li.tab";
	public static final String AUTHOROUTFILE = "data" + File.separator + "result" + File.separator +"cmn" + File.separator + "author.tab";
	
	
	public static void mergerResult() throws IOException{
		
		Map<String,String> NILMention = new HashMap<String, String>();		
		String text = null;
		
		int count = 0;
		int nil = 0;

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
				}
				count++;
			}
		}				
		
		File dfDir = new File(DFRESULTINPUTDIR);
		File[] dfFiles = dfDir.listFiles();
		for(File file:dfFiles){			
			text = IOUtils.slurpFile(file);
			String[] lines = text.split("\n");
			for(String line : lines){
				if(line.equals("")){ //空行可能出错
					continue;
				}
//				System.out.println(line);
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
				}
				count++;
			}
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
		}		
		osw.close();
		fos.close();
		Check.checkResult(RESULTFINAL);
	}
	
	public static void mergerAuthor() throws IOException{
		
		File authorDir = new File(AUTHORRESULTINPUTDIR);
		
		 FileOutputStream fos = new FileOutputStream(AUTHOROUTFILE);
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");		
		
		File[] authorFiles = authorDir.listFiles();
		for(File file:authorFiles){			
			String text = IOUtils.slurpFile(file);
			String[] lines = text.split("\n");
			for(String line : lines){
				if (line.equals(""))
					continue;
				osw.write(line + "\n");
			}
			osw.flush();
	
		}		
		osw.close();
		fos.close();		
		
		
		
	}
		
	public static void main(String[] args) throws IOException{
//		mergerResult();
		mergerAuthor();
	}	
}
