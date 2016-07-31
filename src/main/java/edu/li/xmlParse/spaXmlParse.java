/**
 * 
 */
package edu.li.xmlParse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *date:Jun 18, 2016 10:06:51 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 18, 2016 10:06:51 PM
 */
public class spaXmlParse {
	public  static final String DFFILEOUTDIR = "data" + File.separator + "xmlParse" + File.separator + "spa" + File.separator + "df" + File.separator;
	public static final String DFFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "spa" + File.separator + "df" + File.separator;
	
	public  static final String NEWSFILEOUTDIR = "data" + File.separator + "xmlParse" + File.separator + "spa" + File.separator + "news" + File.separator;
	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "spa" + File.separator + "news" + File.separator;
		
	public static final String AUTHOROUTDIR = "data" + File.separator + "result" + File.separator + "author" + File.separator+ "spa" + File.separator; 
		
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
	}
	
	public static void ParseNews(String fileName) throws DocumentException, IOException{
		SAXReader saxReader = new SAXReader();
		File file = new File(NEWSFILEINPUTDIR + fileName);
		Document document = saxReader.read(file);
		Element LCTL_TEXT = document.getRootElement(); //LCTL_TEXT
		Element DOC = (Element) LCTL_TEXT.elements().get(0);
//		String fileOutPath = NEWSFILEOUTDIR + DOC.attributeValue("id");//输出文件路径
		String fileOutPath = NEWSFILEOUTDIR + fileName;
		FileOutputStream fos = new FileOutputStream(fileOutPath);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		
		Element TEXT = (Element) DOC.elements().get(0);
		List<Element> SEGs = TEXT.elements();
		for(Element SEG : SEGs){
			Element ORIGINAL_TEXT = (Element) SEG.elements().get(0);
			String text = ORIGINAL_TEXT.getText();
//			System.out.println(text);
			if (-1 == text.indexOf("<")){
				String temp =  SEG.attributeValue("start_char") + "\t" +ORIGINAL_TEXT.getText() + "\n";
				osw.write(temp);
			}
		}
		osw.close();
		fos.close();
	}
	
	public static void ParseDf(String fileName) throws DocumentException, IOException {
		SAXReader saxReader = new SAXReader();
		File file = new File(DFFILEINPUTDIR + fileName);
		Document document = saxReader.read(file);
		Element LCTL_TEXT = document.getRootElement(); //LCTL_TEXT
//		System.out.println(.getName());
		Element DOC = (Element) LCTL_TEXT.elements().get(0);
		
		String fileID = DOC.attributeValue("id").split("\\.")[0];
//		String fileOutPath = DFFILEOUTDIR + DOC.attributeValue("id");//纯文本输出文件路径
		String fileOutPath = DFFILEOUTDIR + fileName;
		
		FileOutputStream textfos = new FileOutputStream(fileOutPath);
		OutputStreamWriter textosw = new OutputStreamWriter(textfos, "UTF-8");
		
//		String authorOutPath = AUTHOROUTDIR + DOC.attributeValue("id");//作者输出文件路径
		String authorOutPath = AUTHOROUTDIR + fileName;//作者输出文件路径
		
		FileOutputStream authorfos = new FileOutputStream(authorOutPath);
		OutputStreamWriter authorosw = new OutputStreamWriter(authorfos, "UTF-8");	
		
		Element TEXT = (Element) DOC.elements().get(0);
		int flag = 0;
		List<Element> SEGs = TEXT.elements();
		for(Element SEG : SEGs){
			Element ORIGINAL_TEXT = (Element) SEG.elements().get(0);
			String text = ORIGINAL_TEXT.getText();
//			System.out.println(text);
			
			if (-1 != text.indexOf("<quote orig_author=")  ||-1 != text.indexOf("&lt;quote orig_author=") ){
				flag += 1;
//				System.out.println(flag);
				continue;
			}
			
			if(-1 != text.indexOf("</quote>") || -1 != text.indexOf ("&lt;/quote&gt;")){
				flag -= 1;
//				System.out.println(flag);
				continue;
			}
			
			if (flag != 0){
//				System.out.println(flag);
				continue;
			}
			
			
			if(-1 != text.indexOf("&lt;post id=") || -1 != text.indexOf("<post id=")){//提取作者
				Pattern pattern = Pattern.compile("author=\"(.*?)\"");
				Matcher matcher = pattern.matcher(text);
				if(matcher.find()){
					int len = matcher.group(1).trim().length();
					int start = Integer.parseInt(SEG.attributeValue("start_char")) + matcher.start(1);
					int end = start + len - 1;					
//					System.out.println(matcher.group(1) + "\t" + fileID + ":" + start + "-" + end);
					authorosw.write(matcher.group(1) + "\t" + fileID + ":" + start + "-" + end + "\n");
					authorosw.flush();
				}				
				continue;				
			}
			
			if(text.equals("</post>") || text.equals("&lt;/post&gt;") ){
				continue;
			}
			
			textosw.write(SEG.attributeValue("start_char") + "\t" + text + "\n");
			textosw.flush();
//			if (-1 == text.indexOf("<") && -1 == text.indexOf("&lt")){ //提取纯文本
//				textosw.write(SEG.attributeValue("start_char") + "\t" + text + "\n");
//
//				textosw.flush();
//			}
//			else if( -1 != text.indexOf("author")){//提取发贴的作者
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
//			}
		}
		textosw.close();
		textfos.close();
		authorosw.close();
		authorfos.close();
	}
	
	public static void Parse(String fileName, String type) throws DocumentException, IOException{
		if(type.equals("df"))
			ParseDf(fileName);
		else 
			ParseNews(fileName);
	}
	
	
	public static void main(String[] args) throws IOException, DocumentException {
		// TODO Auto-generated method stub
		String df = "ENG_DF_000170_20150322_F00000082.df.ltf.xml";//路径需要拼接，避免不同的平台使用。
		ParseDf(df);
		String news = "ENG_NW_001001_20150404_F00000000.ltf.xml";
		ParseNews(news);
	}
}
