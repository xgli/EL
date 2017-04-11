/**
 * 
 */
package edu.li.candidate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.li.other.testProps;

/**
 *date:Apr 10, 2017 5:16:03 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Apr 10, 2017 5:16:03 PM
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> teStrings = new LinkedList<String>();
		teStrings.add("adfdf\n");
		teStrings.add("oooo");
		String str = "";
		for(String s: teStrings){
			str += s;
		}
		System.out.println(str);
	}

}
