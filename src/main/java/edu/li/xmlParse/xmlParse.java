/**
 * 
 */
package edu.li.xmlParse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLNonTransientConnectionException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.stanford.nlp.fsm.TransducerGraph.SetToStringNodeProcessor;

/**
 *date:Jun 13, 2016 9:03:54 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 13, 2016 9:03:54 PM
 */
public class xmlParse {

	/**
	 * @param args
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public  static final String DFFILEOUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator + "df" + File.separator;
	public static final String DFFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator + "df" + File.separator;
	
	public  static final String NEWSFILEOUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator + "news" + File.separator;
	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator + "news" + File.separator;
	

	
	
	public static final String AUTHOROUTDIR = "data" + File.separator + "result" + File.separator + "author" + File.separator; 
	
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
			if (-1 == text.indexOf("<")){
				String temp =  SEG.attributeValue("start_char") + " " +ORIGINAL_TEXT.getText() + "\n";
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
		List<Element> SEGs = TEXT.elements();
		for(Element SEG : SEGs){
			Element ORIGINAL_TEXT = (Element) SEG.elements().get(0);
			String text = ORIGINAL_TEXT.getText();
			if (-1 == text.indexOf("<")){ //提取纯文本
				textosw.write(SEG.attributeValue("start_char") + " " + text + "\n");
				textosw.flush();
			}
			else if( -1 != text.indexOf("author")){//提取发贴的作者
				Pattern pattern = Pattern.compile("author=\"(.*?)\"");
				Matcher matcher = pattern.matcher(text);
				if(matcher.find()){
					int start = Integer.parseInt(SEG.attributeValue("start_char")) + matcher.start(1);
					int end = Integer.parseInt(SEG.attributeValue("start_char")) + matcher.end(1) - 1;					
//					System.out.println(matcher.group(1) + "\t" + fileID + ":" + start + "-" + end);
					authorosw.write(matcher.group(1) + "\t" + fileID + ":" + start + "-" + end + "\n");
					authorosw.flush();
				}
			}
		}
		textosw.close();
		textfos.close();
		authorosw.close();
		authorfos.close();
	}
	
	public static void main(String[] args) throws IOException, DocumentException {
		// TODO Auto-generated method stub
		String fileName = "CMN_DF_000020_20150108_F00100074.df.ltf.xml";//路径需要拼接，避免不同的平台使用。
		ParseDf(fileName);

	}

}
