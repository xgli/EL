/**
 * 
 */
package edu.li.other;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ejml.alg.dense.linsol.WrapLinearSolverBlock64;

import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jun 23, 2016 10:41:19 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 23, 2016 10:41:19 PM
 */
public class count {
	
	
	public static void getOthers(String fileDir) throws IOException{
		File dir = new File(fileDir);
		File[] files = dir.listFiles();
		int all =  files.length;
		int count = 0;
		Map<String, Integer> wordsCount = new HashMap<String, Integer>();
		
		for(File file : files){
			String filePath = file.getAbsolutePath();
			String text = IOUtils.slurpFile(filePath);
			String[] lines = text.split("\n");
			for(String line : lines){
				String[] tokens = line.split("\t");
				for(String token : tokens){
					if(wordsCount.containsKey(token)){
						wordsCount.put(token, wordsCount.get(token) + 1);
					}
					else {
						wordsCount.put(token, 1);
					}
					
				}
				
			}

		}
		
		
//        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(wordsCount.entrySet());
//        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
//            //升序排序
//            public int compare(Entry<String, String> o1,
//                    Entry<String, String> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//            
//        });
//        
//        for(Map.Entry<String,String> mapping:list){ 
//               System.out.println(mapping.getKey()+":"+mapping.getValue()); 
//          } 
		
		
		for(String key : wordsCount.keySet()){
			int num = wordsCount.get(key);
			if(num > 40){
				System.out.println(key + ":" + num);			
			}

		}
		
	}	
	
	public static void searchWords(String filePath){
		
	}
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String newsFileDir = "data" + File.separator + "segment" + File.separator + "cmn" + File.separator +  "news";
		String dfFileDir = "data" + File.separator + "segment" + File.separator + "cmn" + File.separator +  "df";
		getOthers(newsFileDir);
	}

	

}
