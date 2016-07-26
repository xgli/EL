/**
 * 
 */
package edu.li.result;

import java.io.IOException;

import com.spatial4j.core.shape.SpatialRelation;

import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jul 19, 2016 5:01:54 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jul 19, 2016 5:01:54 PM
 *检查结果是否符合要求
 */
public class Check {
	public static void checkResult(String fileName) throws IOException {
		String text = IOUtils.slurpFile(fileName);
		String[] lines = text.split("\n");
		for(String line:lines){
			String[] tokens = line.split("\t"); 
			if(8 != tokens.length){
				System.out.println("line");
			}
		}
		
	}

}
