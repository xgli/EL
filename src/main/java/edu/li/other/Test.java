/**
 * 
 */
package edu.li.other;

import java.io.File;

import sun.util.locale.StringTokenIterator;

/**
 *date:Jul 20, 2016 10:58:03 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jul 20, 2016 10:58:03 AM
 */
public class Test {

	/**
	 * @param args
	 */
	
	public static void testMkdirs(){
		String filepath = "data/li/test/";
		File file = new File(filepath);
		if(!file.exists() && !file.isDirectory()){
			file.mkdirs();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testMkdirs();
	}

}
