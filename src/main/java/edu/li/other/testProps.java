/**
 * 
 */
package edu.li.other;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *date:Jul 18, 2016 11:05:37 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jul 18, 2016 11:05:37 AM
 */
public class testProps {
	
	public static void main(String[] args){
		Properties props = new Properties();
		InputStream inputStream = testProps.class.getResourceAsStream("../../../cmn.config");
		try {
			props.load(inputStream);
			String  text = props.getProperty("test");
			System.out.println(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
