/**
 * 
 */
package edu.li.other;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


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
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String sent = "中国,  nfgfgf";
//		List<Pinyin> pinyins = HanLP.convertToPinyinList(sent);
//		String pinyinStr = "";
//		for (int i = 0; i < sent.length(); i++){
//			pinyinStr += sent.substring(i,i+1) + ":" + i + ":" + pinyins.get(i) + " ";
//		}
//		System.out.println(pinyinStr);
		Pattern pattern;
		Matcher matcher;
		  pattern = Pattern.compile("<(GPE)>(.*?)</GPE>");
		  matcher = pattern.matcher("【中华论坛】<GPE>埃及</GPE>出事：<PER>中国</PER>终于表态了！");
		 while(matcher.find()){	 //考虑提取后的，标签对位置的影响   增加内嵌类型
			 System.out.println(matcher.group(0));
			 System.out.println(matcher.group(1));
			 System.out.println(matcher.group(2));
		 }
		 pattern = Pattern.compile("<(PER)>(.*?)</PER>");
		 matcher = pattern.matcher("【中华论坛】<GPE>埃及</GPE>出事：<PER>中国</PER>终于表态了！");
		 while(matcher.find()){	 //考虑提取后的，标签对位置的影响   增加内嵌类型
			 System.out.println(matcher.group(0));
			 System.out.println(matcher.group(1));
			 System.out.println(matcher.group(2));
		 }
		 
	}

}
