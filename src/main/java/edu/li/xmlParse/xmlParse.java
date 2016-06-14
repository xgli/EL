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
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
	public static void Parse(String fileName) throws DocumentException, IOException{
		
		FileOutputStream fos = new FileOutputStream("raw_num.txt");
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		SAXReader saxReader = new SAXReader();
		File file = new File(fileName);
		Document document = saxReader.read(file);
		Element LCTL_TEXT = document.getRootElement(); //LCTL_TEXT
//		System.out.println(.getName());
		Element DOC = (Element) LCTL_TEXT.elements().get(0);		
		Element TEXT = (Element) DOC.elements().get(0);
		List<Element> SEGs = TEXT.elements();
		for(Element SEG : SEGs){
//			System.out.println("start_char:" + SEG.attributeValue("start_char"));
			Element ORIGINAL_TEXT = (Element) SEG.elements().get(0);
			String text = ORIGINAL_TEXT.getText();
			if (-1 == text.indexOf("<")){
				String temp =  SEG.attributeValue("start_char") + " " +ORIGINAL_TEXT.getText() + "\n";
				osw.write(temp);
//				System.out.print("start_char:" + SEG.attributeValue("start_char") + " ");
//				System.out.println(ORIGINAL_TEXT.getText());
			}
		}
		osw.close();
		fos.close();

	}
	public static void main(String[] args) throws IOException, DocumentException {
		// TODO Auto-generated method stub
		String fileName = "raw.xml";
		Parse(fileName);

	}

}
