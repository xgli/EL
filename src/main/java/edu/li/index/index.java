/**
 * 
 */
package edu.li.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.util.AttributeFactory.StaticImplementationAttributeFactory;

import edu.stanford.nlp.io.IOUtils;

/**
 *date:Aug 29, 2016 8:22:07 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Aug 29, 2016 8:22:07 PM
 */
public class index {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 */

	
		
	public static void main(String[] args) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		List<String> cmnfilenamelist = new ArrayList<String>();
		List<String> engfilenamelist = new ArrayList<String>();
		List<String> spafilenamelist = new ArrayList<String>();
		
		try {
			String text = IOUtils.slurpFile("data/filesnamelist.tab", "utf-8");
			String[] lines = text.split("\n");
			for(String line : lines){
				String fileName = line.split("\t")[0];
				if(-1 != fileName.indexOf("CMN_")){
					cmnfilenamelist.add(fileName+".xml");
				}
				else if(-1 != fileName.indexOf("ENG_")){
					engfilenamelist.add(fileName+".xml");
				}
				else if(-1 != fileName.indexOf("SPA_")){
					spafilenamelist.add(fileName+".xml");					
				}				
//				System.out.println(tokens[0]);
			}
//			int count = 0;
//			for( Iterator<String> iterator = engfilenamelist.iterator();iterator.hasNext();){
//				String name = iterator.next();
//				System.out.println(name);
//				count += 1;
//			}
//			System.out.println(count);
			
			FileOutputStream fos;
			ObjectOutputStream oos;
			
			fos = new FileOutputStream("data/engfilenamelist.ser");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(engfilenamelist);
			oos.close();
			fos.close();
			
			fos = new FileOutputStream("data/cmnfilenamelist.ser");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(cmnfilenamelist);
			oos.close();
			fos.close();
			
			fos = new FileOutputStream("data/spafilenamelist.ser");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(spafilenamelist);
			oos.close();
			fos.close();		
			
//			FileInputStream fis = new FileInputStream("data/engfilenamelist.ser");
//			ObjectInputStream ois = new ObjectInputStream(fis);
//			List<String> list = (ArrayList<String>) ois.readObject();
//			int count = 0;
//			for( Iterator<String> iterator = list.iterator();iterator.hasNext();){
//				String name = iterator.next();
//				System.out.println(name);
//				count += 1;
//			}
//			System.out.println(count);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
