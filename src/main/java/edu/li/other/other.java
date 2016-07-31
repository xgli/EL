/**
 * 
 */
package edu.li.other;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.DocumentException;

import com.spatial4j.core.shape.SpatialRelation;

import edu.li.candidate.engGenCandidate;
import edu.li.mention.cmnGenMention;
import edu.li.mention.engGenMention;
import edu.li.xmlParse.engXmlParse;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.sequences.TrueCasingForNISTDocumentReaderAndWriter.LineToTrueCasesParser;

/**
 *date:Jun 23, 2016 8:28:43 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 23, 2016 8:28:43 PM
 */
public class other {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static final String ENGNEWSNAMEFILE = "data" + File.separator + "engnewsname.tab";
	public static final String ENGDFNAMEFILE = "data" + File.separator + "engdfname.tab";
	public static void getEnglishPerson(String fileDir) throws IOException{
		File dir = new File(fileDir);
		File[] files = dir.listFiles();
		int all =  files.length;
		int count = 0;
		for(File file : files){
			String filePath = file.getAbsolutePath();
			String text = IOUtils.slurpFile(filePath);
			String[] lines = text.split("\n");
			String fileID = file.getName().split("\\.")[0];
//			System.out.println(file.getName().split("\\.")[0]);
			for(String line : lines){
				int bias = Integer.parseInt(line.split("\t")[0]);
				line = line.split("\t")[1];

//				String ner = cmnGenMention.getAnsjNER(line.split("\t")[1]);
				
				Pattern pattern = Pattern.compile("([\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})+)");//提取所有带·的名字
				Matcher matcher = pattern.matcher(line);
				while(matcher.find()){
					String old = matcher.group(0);
////					System.out.println(old+":");
					if(old.length() <= 1){
						continue;
					}
					count++;		
					System.out.println(line);
					int start = matcher.start() + bias - 39 ;
					int end = start + old.length() - 1;
					String loc = start +"-"+ end;
					System.out.println(count + "\t" +  old + "\t" + fileID +":"+ loc);
				}
				
			}			
		}		
		
	}
	
	
	public static void getEnglish(String fileDir,String  type) throws IOException{

		 FileOutputStream  fos;
		if(type.equals("news")){
			 fos = new FileOutputStream(ENGNEWSNAMEFILE);
		}
		else{
			fos = new FileOutputStream(ENGDFNAMEFILE);			
		}
		
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		
		File dir = new File(fileDir);
		File[] files = dir.listFiles();
		int all =  files.length;
		int count = 0;
		for(File file : files){
			String filePath = file.getAbsolutePath();
			String text = IOUtils.slurpFile(filePath);
			String[] lines = text.split("\n");
			String fileID = file.getName().split("\\.")[0];
//			System.out.println(file.getName().split("\\.")[0]);
			for(String line : lines){
				int bias = Integer.parseInt(line.split("\t")[0]);
				line = line.split("\t")[1];

//				String ner = cmnGenMention.getAnsjNER(line.split("\t")[1]);
				
//				Pattern pattern = Pattern.compile("([\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})+)");//提取所有带·的名字
//				Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");//提取（）内容，主要是英文人名
//				Pattern pattern = Pattern.compile("[a-zA-Z|]+\\s?\\.?[a-zA-Z|]+\\s?\\.?[a-zA-Z]*");
				Pattern pattern = Pattern.compile("[[a-zA-Z]+\\s?\\.?-?]+");//提取英文  排除全是空白或者..
				Matcher matcher = pattern.matcher(line);
				while(matcher.find()){
					String old = matcher.group(0);
////					System.out.println(old+":");
					if(old.length() <= 1){
						continue;
					}
					count++;		
					System.out.println(line);
					osw.write(line+"\n");
					int start = matcher.start() + bias - 39 ;
					int end = start + old.length() - 1;
					String loc = start +"-"+ end;
					System.out.println(count + "\t" +  old + "\t" + fileID +":"+ loc);
					osw.write(old + "\t" + fileID + ":" + loc + "\n");
					osw.flush();
				}
				
			}

		}
		osw.close();
		fos.close();		
	}			
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	}

}
