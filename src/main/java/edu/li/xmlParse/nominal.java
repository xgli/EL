/**
 * 
 */
package edu.li.xmlParse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.elasticsearch.common.netty.handler.ssl.SslBufferPool;
/**
 *date:Sep 12, 2016 10:59:49 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Sep 12, 2016 10:59:49 PM
 */
public class nominal {
	public static final String INPUTDIR = "ere" + File.separator + "data" + File.separator + "cmn" + File.separator + "ere" + File.separator;
	public static final String OUTPUTFILE = "ere" + File.separator + "new" + File.separator + "cmn_fac_loc_head.tab";
	
	static FileOutputStream fos;
	static OutputStreamWriter osw;
	static {
		try {
			fos = new FileOutputStream(OUTPUTFILE);
			osw = new OutputStreamWriter(fos, "utf-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void parse(String fileName) throws DocumentException, IOException{
		System.out.println(fileName);
		SAXReader saxReader = new SAXReader();
		File file = new File(INPUTDIR + fileName);
		Document document = saxReader.read(file);
		Element deft_ere  = document.getRootElement();
		Element entities = (Element) deft_ere.elements().get(0);
		List<Element> entitiylist =  entities.elements();
		
		for (Element entity : entitiylist){
			String  mention_type = entity.attributeValue("type");
//			System.out.println(mention_type);
			List<Element> entity_mentions = entity.elements();
			for (Element entity_mention : entity_mentions){
				String noun_type = entity_mention.attributeValue("noun_type");
//				System.out.println(noun_type);
//				if(mention_type.equals("LOC"))
//					System.out.println(mention_type);
				if (mention_type.equals("FAC") || mention_type.equals("LOC")){//获取nominal head
					String mention_text =  entity_mention.element("mention_text").getText();
					String nom_head;
					if (noun_type.equals("NOM")){
						nom_head = entity_mention.element("nom_head").getText();
					}else{
						nom_head = mention_text;
					}
						
						//					System.out.println(mention_text);
//					System.out.println(nom_head);
					String outString =noun_type + "\t" + mention_type + "\t" + mention_text + "\t" + nom_head + "\n";
					System.out.println(outString);
					osw.write(outString);
					osw.flush();
//				if (noun_type.equals("NOM")){//获取nominal head
//					String mention_text =  entity_mention.element("mention_text").getText();
//					String nom_head = entity_mention.element("nom_head").getText();
////					System.out.println(mention_text);
////					System.out.println(nom_head);
//					String outString = mention_type + "\t" + mention_text + "\t" + nom_head + "\n";
//					System.out.println(outString);
//					osw.write(outString);
//					osw.flush();
					
				}else{
					continue;
				}
			}
		}
//		System.out.println(entities.getName());
		
		
	}
	
	public static void main(String[] args) throws DocumentException, IOException{
//		String fileName = "dff2127764a09cf49312501bc9a6a542.rich_ere.xml";
//		parse(fileName);
		File dir = new File(INPUTDIR);
		File[] files = dir.listFiles();
		for (File file : files){
//			System.out.println(files.length);
			String fileName = file.getName();
			if(fileName.endsWith("dtd")){
				continue;
			}
			parse(fileName);
		}
		osw.close();
		fos.close();
	}

}
