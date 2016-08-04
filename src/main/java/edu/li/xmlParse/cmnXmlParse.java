/**
 * 
 */
package edu.li.xmlParse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.elasticsearch.search.facet.terms.doubles.TermsDoubleFacetExecutor.StaticAggregatorValueProc;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.hankcs.hanlp.dependency.nnparser.util.std;
import com.sun.xml.internal.bind.v2.model.core.ID;

import edu.li.other.testProps;
import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jun 18, 2016 10:04:28 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 18, 2016 10:04:28 PM
 */
public class cmnXmlParse {
	
	public  static final String DFFILEOUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator + "df" + File.separator;
	public static final String DFFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator + "df" + File.separator;
//	
	public  static final String NEWSFILEOUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator + "news" + File.separator;
	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator + "news" + File.separator;
//	
//	
	public static final String AUTHOROUTDIR = "data" + File.separator + "result" + File.separator + "author" + File.separator  + "cmn" + File.separator; 
	public static final String CHARACTER_COUNTS_FILE = "data" + File.separator + "raw" + File.separator + "character_counts.tsv";
	public static final String QUOTE_REGIONS_FILE = "data" + File.separator + "raw" + File.separator + "quote_regions.tsv";
	public static String TEMPTEXT;
	public static Map<String, Integer>CHARACTER_DICT;
	public static Map<String, List<Integer>> QUOTE_DICT;
	
	static{
		File file = null;
		file = new File(AUTHOROUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
		file = new File(DFFILEOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
		file = new File(NEWSFILEOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
		
		try {//进行文件的load
			TEMPTEXT = IOUtils.slurpFile(CHARACTER_COUNTS_FILE, "utf-8");
			String[] lines = TEMPTEXT.split("\n");
			CHARACTER_DICT = new HashMap<String, Integer>();
			for (String line : lines){ 
				String[] tokens = line.split("\t");
				CHARACTER_DICT.put(tokens[0], Integer.parseInt(tokens[1]));
			}
			TEMPTEXT = IOUtils.slurpFile(QUOTE_REGIONS_FILE, "utf-8");
			lines = TEMPTEXT.split("\n");
			QUOTE_DICT = new HashMap<String, List<Integer>>();
			for (String line : lines){
				String[] tokens = line.split("\t");
				List<Integer> temp_list = new  ArrayList<Integer>();
				temp_list.add(Integer.parseInt(tokens[1]));
				temp_list.add(Integer.parseInt(tokens[2]));
				QUOTE_DICT.put(tokens[0], temp_list);
			}			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void ParseNews(String fileName) throws DocumentException, IOException{
		SAXReader saxReader = new SAXReader();
		File file = new File(NEWSFILEINPUTDIR + fileName);			
		Document document = saxReader.read(file);
		int start = 0;
		Element doc = document.getRootElement(); //DOC
		System.out.println(start);
		String id = doc.attributeValue("id");
		start = start + "<DOC\tid=" .length() + id.length() + ">\n".length() + 2; //2是id的两个引号长度
		System.out.println(start);
		String date = doc.element("DATE_TIME").getText();
		start = start + "<DATE_TIME></DATE_TIME>\n".length() + date.length();
		System.out.println(start);
		String  headline = doc.element("HEADLINE").getText();
		start = start + "<HEADLINE></HEADLINE>\n".length() + headline.length();
		System.out.println(start);
		String author = doc.element("AUTHOR").getText();
		start = start + "<AUTHOR></AUTHOR>\n".length() + author.length();
		System.out.println(start);
		String text = doc.element("TEXT").getText();
//		System.out.println(text);
//		String fileOutPath = NEWSFILEOUTDIR + DOC.attributeValue("id");//输出文件路径
		String fileOutPath = NEWSFILEOUTDIR + fileName;
		FileOutputStream fos = new FileOutputStream(fileOutPath);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");		

		osw.close();
		fos.close();
	}
	
	public static void ParseDf(String fileName) throws DocumentException, IOException {
		SAXReader saxReader = new SAXReader();
		File file = new File(DFFILEINPUTDIR + fileName);
		Document document = saxReader.read(file);
		int start = 0;
		Element doc = document.getRootElement(); //LCTL_TEXT
		String id = doc.attributeValue("id");
		start = start + "<doc id=>".length() + id.length() +  2; //2为引号长度
		String headline = doc.elementText("headline");
		System.out.println(headline);

		for (Iterator posts = doc.elementIterator("post"); posts.hasNext();){
			Element post = (Element) posts.next();
			
			System.out.println(post.getText());
		}
		
//		String fileID = DOC.attributeValue("id").split("\\.")[0];
////		String fileOutPath = DFFILEOUTDIR + DOC.attributeValue("id");//纯文本输出文件路径
//		String fileOutPath = DFFILEOUTDIR + fileName;
//		
//		FileOutputStream textfos = new FileOutputStream(fileOutPath);
//		OutputStreamWriter textosw = new OutputStreamWriter(textfos, "UTF-8");
//		
////		String authorOutPath = AUTHOROUTDIR + DOC.attributeValue("id");//作者输出文件路径
//		String authorOutPath = AUTHOROUTDIR + fileName;//作者输出文件路径
//		
//		FileOutputStream authorfos = new FileOutputStream(authorOutPath);
//		OutputStreamWriter authorosw = new OutputStreamWriter(authorfos, "UTF-8");	
//		
//		Element TEXT = (Element) DOC.elements().get(0);
//		List<Element> SEGs = TEXT.elements();
//		
//		int flag = 0;
//		
//		for(Element SEG : SEGs){
//			Element ORIGINAL_TEXT = (Element) SEG.elements().get(0);
//			String text = ORIGINAL_TEXT.getText();
////			System.out.println(text);
//			if (-1 != text.indexOf("<quote") || -1 != text.indexOf ("&lt;/quote")) {
//				flag += 1;
//				System.out.println(flag);
////				System.out.println(flag + ":" + text);
//				continue;
//			}
//			
//			if(-1 != text.indexOf("</quote>") || -1 != text.indexOf ("&lt;/quote&gt;")){
//				flag -= 1;
//				System.out.println(flag);
////				System.out.println(flag + ":" + text);
//				continue;
//			}
//			
//			if (flag != 0){
////				System.out.println(flag);
////				System.out.println(text);
//				continue;
//			}
//
//			
//			if(-1 != text.indexOf("&lt;post id=") || -1 != text.indexOf("<post id=")){//提取作者
//				Pattern pattern = Pattern.compile("author=\"(.*?)\"");
//				Matcher matcher = pattern.matcher(text);
//				if(matcher.find()){
//					int len = matcher.group(1).trim().length();
//					int start = Integer.parseInt(SEG.attributeValue("start_char")) + matcher.start(1);
//					int end = start + len - 1;					
////					System.out.println(matcher.group(1) + "\t" + fileID + ":" + start + "-" + end);
//					authorosw.write(matcher.group(1) + "\t" + fileID + ":" + start + "-" + end + "\n");
//					authorosw.flush();
//				}				
//				continue;				
//			}
//			if(text.equals("</post>") || text.equals("&lt;/post&gt;")){
//				continue;
//			}
//			textosw.write(SEG.attributeValue("start_char") + "\t" + text + "\n");
//			textosw.flush();
//			
//
//		}
//		textosw.close();
//		textfos.close();
//		authorosw.close();
//		authorfos.close();
	}
	
	public static void Parse(String fileName, String type) throws DocumentException, IOException{
		if(type.equals("df"))
			ParseDf(fileName);
		else 
			ParseNews(fileName);
	}
	
	public static void main(String[] args) throws IOException, DocumentException {
		// TODO Auto-generated method stub
		String fileName = "CMN_NW_001278_20130101_F00010KRE.xml";//路径需要拼接，避免不同的平台使用。
//		ParseDf(fileName); 
//		Parse(fileName, "news");
		fileName = "CMN_DF_000020_20110201_G00A0E8P9.xml";
		Parse(fileName, "df");
		
	}	
	
}
