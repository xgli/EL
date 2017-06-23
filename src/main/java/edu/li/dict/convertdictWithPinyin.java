/**
 * 
 */
package edu.li.dict;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import edu.stanford.nlp.io.IOUtils;

/**
 *date:Aug 8, 2016 9:06:46 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Aug 8, 2016 9:06:46 PM
 */
public class convertdictWithPinyin {
	
	public static void getPinyin() throws IOException{
		String fileinputpath = "dict/zuhe.dict";
		String fileoutputpath = "dict/zuhewithpinyin.dict";
		FileOutputStream fos = new FileOutputStream(fileoutputpath);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
		String text = IOUtils.slurpFile(fileinputpath, "utf-8");
		String[] lines = text.split("\n");
		for(String line : lines){
			line = line.trim();
			if(line.equals(""))
				continue;
			String[] tokens = line.split("\t");
			String mention_text = tokens[0];
			String mid = tokens[1];
			String mtype = tokens[2];
			String  mclass = "NAM";
			
			if (mention_text.length() < 2 ){
				continue;
			}
			String pinyinstr = mention_text + "\t";			
			List<Pinyin> pinyins = HanLP.convertToPinyinList(mention_text);
			for(int i = 0; i <mention_text.length(); i++){
				pinyinstr += pinyins.get(i) + " ";
			}
			pinyinstr = pinyinstr.trim();
			String outline = pinyinstr + "\t" + mid +  "\t" +  mtype +"\n";
			osw.write(outline);
			System.out.println(outline);
		}
		osw.close();
		fos.close();
	}
	
	public static void getDictPinyin(){
		String fileinputpath = "dict/zuhe.dict";
		String fileoutputpath = "dict/zuhewithpinyin.dict";
		try {
			FileOutputStream fos = new FileOutputStream(fileoutputpath);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
			String  text = IOUtils.slurpFile(fileinputpath, "utf-8");
			String[] lines = text.split("\n");
			for(String line : lines){
				line = line.trim();
				if(line.equals(""))
					continue;
				String[] tokens = line.split("\t");
				String mention = tokens[0];
				String pinyinstr = mention + "\t";
				List<Pinyin> pinyins = HanLP.convertToPinyinList(mention);
				for(int i = 0; i <mention.length(); i++){
					pinyinstr += pinyins.get(i) + " ";
				}
				pinyinstr = pinyinstr.trim();
				osw.write(pinyinstr + "\tmid\tGPE\n");
				osw.flush();
			}
			osw.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws IOException{
//		getDictPinyin();
//		getPinyin();
		getDictPinyin();
	}

}
