/**
 * 
 */
package edu.li.edl;

import java.io.IOException;

import org.dom4j.DocumentException;

import edu.li.candidate.cmnGenCandidate;
import edu.li.mention.cmnGenMention;
import edu.li.xmlParse.xmlParse;

/**
 *date:Jun 17, 2016 10:51:21 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 17, 2016 10:51:21 AM
 */
public class Process {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		String fileName = "CMN_DF_000020_20150108_F00100074.df.ltf.xml";
		xmlParse.ParseDf(fileName);
		cmnGenMention.GetMention(fileName, "df");
		cmnGenCandidate.GenCandidate(fileName, "df");

	}

}
