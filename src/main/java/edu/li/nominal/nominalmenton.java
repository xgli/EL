/**
 * 
 */
package edu.li.nominal;

/**
 *date:Aug 15, 2016 7:56:37 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Aug 15, 2016 7:56:37 AM
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import edu.li.es.Search;
import edu.li.other.count;
import edu.stanford.nlp.fsm.TransducerGraph.SetToStringNodeProcessor;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.optimization.InefficientSGDMinimizer;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import sun.text.resources.cldr.sw.FormatData_sw;






public class nominalmenton {

	/**
	 * @param args
	 */
	
	
	/** 
	 */
		
	static FileOutputStream fos;
	static OutputStreamWriter osw;
	static{

		try {
			fos = new FileOutputStream("loc_nom.tab");
			osw = new OutputStreamWriter(fos, "utf-8");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
	}
	
	
	static Map<String, String> m = new HashMap<String, String>();
	static Properties prop = new Properties();
	static Map<String, String> c_p = new HashMap<String, String>();
	static int nil = 970000;
	static{
		try {
			String text = IOUtils.slurpFile("regexner_map/locaddnom.txt", "utf-8");
			String[] lines = text.split("\n");
			for(String line : lines){
				if (line.equals(""))
					continue;
				String[] tokens = line.split("\t");
				c_p.put(tokens[0], tokens[1]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static{
		prop.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,regexner");
		prop.setProperty("regexner.mapping", "regexner_map/locaddnom.txt");
	}
	static StanfordCoreNLP pipline = new StanfordCoreNLP(prop);
	
	public static void getlocnam(String fileName, String type) throws IOException{
//		String fileInputPath = fileName;
		Map<String, String> nildict = new HashMap<String, String>();		
		String fileInputPath = NEWSFILEINPUTDIR + fileName;
		String doc_id = fileName.replace(".xml", "");
		String text = IOUtils.slurpFile(fileInputPath, "utf-8");
		String[] lines = text.split("\n");
		for(String line : lines){
			if (line.equals(""))
				continue;
			int bias = Integer.parseInt(line.split("\t")[0]);
			String mtext = line.split("\t")[1];
			Annotation doc = new Annotation(mtext);
			pipline.annotate(doc);

			for(CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)){
				List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
//				TokenSequencePattern pattern2 = TokenSequencePattern.compile("(?$COUNTRY_S [{ner:/COUNTRY|LOCATION/}]+)? []{0,5} [/[aA]|[aA]n|[tT]he|[oO]ne|[aA]nother|[tT]his|[tT]hat|[mM]y|[yY]our|[Hh]is|[Hh]er|[Oo]ur|[Tt]heir|,/] (?$COUNTRY_I [{ner:/COUNTRY|COUNTRIAN/}]+)? [{tag:/JJ|NN|NNS|NNP|IN|JJS|POS/}]{0,3}? (?$NOM [{ner:NOM}]+) /of/? (?$COUNTRY_E [{ner:/COUNTRY|LOCATION/}]+)?");
				TokenSequencePattern pattern2 = TokenSequencePattern.compile("[/[aA]|[aA]n|[tT]he|[oO]ne|[aA]nother|[tT]his|[tT]hat|[mM]y|[yY]our|[Hh]is|[Hh]er|[Oo]ur|[Tt]heir|,/] [{tag:/JJ|NN|NNS|NNP|IN|JJS|POS/}]{0,3}? (?$NOM [{ner:NOM}]+)");
				
				TokenSequenceMatcher matcher = pattern2.matcher(tokens);
				
				while (matcher.find()) {
//					String S = matcher.group("$COUNTRY_S");
//					String I = matcher.group("$COUNTRY_I");
//					String E = matcher.group("$COUNTRY_E");
					String nom = matcher.group("$NOM");
					
//					String country = null, city = null;
//					if(c_p.containsKey(E)){
//						country = E;
//					}else if(c_p.containsKey(I)){
//						country = I;
//					}else if(c_p.containsKey(S)){
//						country = S;
//					}
					
//					if(!c_p.containsKey(E) && E != null){
//						city = E;
//					}else if(!c_p.containsKey(I) && I != null){
//						city = I;
//					}else if(!c_p.containsKey(S) && S != null){
//						city = S;
//					}
					
//					System.out.println("---------------------");
//					System.out.print(nom + ": ");
//					if(nom.toLowerCase().equals("country")){
//						System.out.println(country);
//					}else if(nom.toLowerCase().equals("capital")){
//						System.out.println(c_p.get(country));
//					}else{
//						System.out.println(city);
//					}
					String mid = "";
//					if(city != null){
//						 SearchHits hits = Search.getHits(city, "GPE", "eng");
//						 if (0 != hits.totalHits()){
//							 for (SearchHit hit : hits.getHits()){ //getHits 的使用
//								 mid =  hit.getId().replace("f_", "");
//								 break;
//							 }
//						 }
//					}else{
//						System.out.println("nil");
						if(nildict.containsKey(nom)){
							mid = nildict.get(nom);
						}
						else{ 
							mid = "NIL_" + nil;
							nil += 1;
							nildict.put(nom, mid);
						}				
//						
//					}
					
					List<CoreMap> matchedTokens = matcher.groupNodes(1);
					CoreLabel cl = (CoreLabel) matchedTokens.get(0);
					System.out.println(cl.originalText());
					int start  = bias + cl.beginPosition();
					int end = bias + cl.endPosition() - 1;
//					System.out.println(start);
//					System.out.println(end);
//					String mid = "NIL_" + nil;
//					nil += 1;
					String wline = matcher.group("$NOM") + "\t" + doc_id + ":" + start + "-" + end + "\t" + mid +"\n";
//					osw.write("***********\n");
					osw.write(wline);
					System.out.println(wline);
					
					
					
					
//					System.out.println(matcher.group("$COUNTRY_S"));
//					osw.write(matcher.group("$COUNTRY_S").toString());
//					System.out.println(matcher.group("$COUNTRY_I"));
//					osw.write(matcher.group("$COUNTRY_I").toString());
//					System.out.println(matcher.group("$COUNTRY_E"));
//					if( )
//					osw.write(matcher.group("$COUNTRY_E"));
//					System.out.println("---------------------");
					osw.flush();
					fos.flush();
				
			
				}		
				
				
//				for (CoreLabel token : sentence.get(TokensAnnotation.class)){
//					String word = token.originalText();
//					String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//					String w = token.ner();
//					System.out.println(word + "\t" + w + "\t" + pos);
//				}
			}	

		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static void getIndexFile(String fileName, String type) throws IOException{
//		String fileInputPath = fileName;
		Map<String, String> nildict = new HashMap<String, String>();		
		String fileInputPath = NEWSFILEINPUTDIR + fileName;
		String doc_id = fileName.replace(".xml", "");
		String text = IOUtils.slurpFile(fileInputPath, "utf-8");
		String[] lines = text.split("\n");
		for(String line : lines){
			if (line.equals(""))
				continue;
			int bias = Integer.parseInt(line.split("\t")[0]);
			String mtext = line.split("\t")[1];
			Annotation doc = new Annotation(mtext);
			pipline.annotate(doc);
			for(CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)){
				List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
				TokenSequencePattern pattern2 = TokenSequencePattern.compile("(?$COUNTRY_S [{ner:/COUNTRY|LOCATION/}]+)? []{0,5} [/[aA]|[aA]n|[tT]he|[oO]ne|[aA]nother|[tT]his|[tT]hat|[mM]y|[yY]our|[Hh]is|[Hh]er|[Oo]ur|[Tt]heir|,/] (?$COUNTRY_I [{ner:/COUNTRY|COUNTRIAN/}]+)? [{tag:/JJ|NN|NNS|NNP|IN|JJS|POS/}]{0,3}? (?$NOM [{ner:NOM}]+) /of/? (?$COUNTRY_E [{ner:/COUNTRY|LOCATION/}]+)?");
				//TokenSequencePattern pattern_LOC = TokenSequencePattern.compile("[/[aA]|[aA]n|[tT]he|[oO]ne|[aA]nother|[tT]his|[tT]hat|[mM]y|[yY]our|[Hh]is|[Hh]er|[Oo]ur|[Tt]heir|,/] [{tag:/JJ|NN|NNS|NNP|IN|JJS|POS/}]{0,3}? (?$NOM [{ner:NOM}]+)";
				TokenSequenceMatcher matcher = pattern2.matcher(tokens);
				
				while (matcher.find()) {
					String S = matcher.group("$COUNTRY_S");
					String I = matcher.group("$COUNTRY_I");
					String E = matcher.group("$COUNTRY_E");
					String nom = matcher.group("$NOM");
					
					String country = null, city = null;
					if(c_p.containsKey(E)){
						country = E;
					}else if(c_p.containsKey(I)){
						country = I;
					}else if(c_p.containsKey(S)){
						country = S;
					}
					
					if(!c_p.containsKey(E) && E != null){
						city = E;
					}else if(!c_p.containsKey(I) && I != null){
						city = I;
					}else if(!c_p.containsKey(S) && S != null){
						city = S;
					}
					
					System.out.println("---------------------");
					System.out.print(nom + ": ");
					if(nom.toLowerCase().equals("country")){
						System.out.println(country);
					}else if(nom.toLowerCase().equals("capital")){
						System.out.println(c_p.get(country));
					}else{
						System.out.println(city);
					}
					String mid = "";
					if(city != null){
						 SearchHits hits = Search.getHits(city, "GPE", "eng");
						 if (0 != hits.totalHits()){
							 for (SearchHit hit : hits.getHits()){ //getHits 的使用
								 mid =  hit.getId().replace("f_", "");
								 break;
							 }
						 }
					}else{
						System.out.println("nil");
						if(nildict.containsKey(country)){
							mid = nildict.get(country);
						}
						else{
							mid = "NIL_" + nil;
							nil += 1;
							nildict.put(country, mid);
						}				
						
					}
					
					List<CoreMap> matchedTokens = matcher.groupNodes(3);
					CoreLabel cl = (CoreLabel) matchedTokens.get(0);
					System.out.println(cl.originalText());
					int start  = bias + cl.beginPosition();
					int end = bias + cl.endPosition() - 1;
//					System.out.println(start);
//					System.out.println(end);
					String wline = matcher.group("$NOM") + "\t" + doc_id + ":" + start + "-" + end + "\t" + mid +"\n";
//					osw.write("***********\n");
					osw.write(wline);
					System.out.println(wline);
					
					
					
					
//					System.out.println(matcher.group("$COUNTRY_S"));
//					osw.write(matcher.group("$COUNTRY_S").toString());
//					System.out.println(matcher.group("$COUNTRY_I"));
//					osw.write(matcher.group("$COUNTRY_I").toString());
//					System.out.println(matcher.group("$COUNTRY_E"));
//					if( )
//					osw.write(matcher.group("$COUNTRY_E"));
//					System.out.println("---------------------");
					osw.flush();
					fos.flush();
				
			
				}		
				
				
//				for (CoreLabel token : sentence.get(TokensAnnotation.class)){
//					String word = token.originalText();
//					String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//					String w = token.ner();
//					System.out.println(word + "\t" + w + "\t" + pos);
//				}
			}	

		}

	}
	
	
	
	
	
	
	
	public static final String NEWSFILEINPUTDIR = "news" + File.separator;
//
//	static Map<String, String> m = new HashMap<String, String>();
//	static Properties prop = new Properties();
//	static{
//		prop.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,regexner");
//		prop.setProperty("regexner.mapping", "regexner_map/gpe.txt");
//		//		prop.put("regexner.mapping", value);
//	}
//	static StanfordCoreNLP pipline = new StanfordCoreNLP(prop);
	
//	static CoreMapExpressionExtractor extractor = CoreMapExpressionExtractor
//			.createExtractorFromFiles(TokenSequencePattern.getNewEnv(),"defs.rules", "org_alternate_names.rules");

//");
	
	static int count = 800000;
//	
//	
//	
//	public static void getIndexFile(String fileName, String type) throws IOException{
//		//		System.out.println(fileName);
//		String fileInputPath = NEWSFILEINPUTDIR + fileName;
//		String doc_id = fileName.replace(".xml", "");
//		String text = IOUtils.slurpFile(fileInputPath, "utf-8");
//		String[] lines = text.split("\n");
//		Map<String,String> dict = new HashMap<String, String>();
//		for(String line : lines){
//			if (line.equals(""))
//				continue;
//			//			System.out.println(line);
//			int bias = Integer.parseInt(line.split("\t")[0]);
//			Annotation doc = new Annotation(line.split("\t")[1]);
//			pipline.annotate(doc);
//
//			List<CoreMap> sentences = doc.get(SentencesAnnotation.class);
//
//
///*
//			for (CoreMap sentence : sentences) {
//				System.out.println(sentence);
//				List<MatchedExpression> matchedExpressions = extractor.extractExpressions(sentence);
//				for (MatchedExpression matched : matchedExpressions) {
//		            // Print out matched text and value
//		            System.out.println("matched: " + matched.getText() + " with value " + matched.getValue());
//		            // Print out token information
//		            CoreMap cm = matched.getAnnotation();
//		            for (CoreLabel token : cm.get(CoreAnnotations.TokensAnnotation.class)) {
//		                String word = token.get(CoreAnnotations.TextAnnotation.class);
//		                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
//		                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//		                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
//		                System.out.println("matched token: " + "word=" + word + ", lemma=" + lemma + ", pos=" + pos + ", ne=" + ne);
//		            }
//		        }
//			}
//*/		
//			for(CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)){
//				List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
//				//				System.out.println(tokens);
////								TokenSequencePattern pattern = TokenSequencePattern.compile("([{ner:PERSON}]+) /was|is/ /the/ ([{tag:NN}]+)");
////				TokenSequencePattern pattern = TokenSequencePattern.compile("([{ner:TITLE}]+) /-LRB-/ ([{ner:PERSON}]+) /-RRB-/");
////				TokenSequencePattern pattern = TokenSequencePattern.compile("/[aA]|[aA]n|[tT]he|[oO]ne|[aA]nother|[tT]his|[tT]hat|[mM]y|[yY]our|[Hh]is|[Hh]er|[Oo]ur|[Tt]heir/ [!{ner:TITLE} | {tag:/JJ|NN|NNS|NNP|IN/}]{0,3}? (?$NOM ([{ner:TITLE} & !{tag:/JJ/}]+) (/and|or|,/ [{ner:TITLE}]+)*) [!{ner:/PERSON/} & !{tag:/NN|NNS|NNP/}]{1,2} (?$PERSON [{ner:PERSON}]+)?");
////				TokenSequencePattern pattern = TokenSequencePattern.compile("");
//				TokenSequencePattern pattern = TokenSequencePattern.compile("(?$COUNTRY_S [{ner:/COUNTRY|LOCATION/}]+)? []{0,40}? [/[aA]|[aA]n|[tT]he|[oO]ne|[aA]nother|[tT]his|[tT]hat|[mM]y|[yY]our|[Hh]is|[Hh]er|[Oo]ur|[Tt]heir/ | {tag:/POS/}] [{tag:/JJ|NN|NNS|NNP|IN|JJS/}]{0,3}? (?$NOM [{ner:NOM}]+) /of/? (?$COUNTRY_E [{ner:/COUNTRY|LOCATION/}]+)?");
//				TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
////				System.out.println(sentence.toShorterString(what));
//				while (matcher.find()) {
//					String outline = "";
//					for(CoreLabel token : tokens){
//						outline += token.originalText() + "/";
////						System.out.println(token.
//						outline += token.ner() + "  ";
//					}
//					
//					System.out.println(outline);
//					int flag = 0;
//					String matchedString = matcher.group(0);
////					System.out.println(matchedString);
//					String mention = matcher.group("$COUNTRY_S");
//					System.out.println(mention);
//					String mention_e = matcher.group("$COUNTRY_E");
//					System.out.println(mention_e);
//					String nominal = matcher.group("$NOM");
//					System.out.println(nominal);
//					String mid = "NIL";
////					System.out.println(matcher.group("$NOM") + "\t" + matcher.group("$ERSON"));
////					System.out.println(mention.replace("null", "NIL"));
////					System.out.println(mention == null);
//					
//					
////					if (mention != null){
////						if (dict.containsKey(mention)){
////							mid = dict.get(mention);
////							continue;
////						}
////						
////						System.out.println(mention);
////						 SearchHits hits = Search.getHits(mention, "PER", "eng");
////						 if (0 != hits.totalHits()){
////							 for (SearchHit hit : hits.getHits()){ //getHits 的使用
////								 mid =  hit.getId().replace("f_", "");
////								 break;
////							 }
////						 }
////						 else {
////								if(dict.containsKey(mention)){
////									mid = dict.get(mention);
////								}
////								else {
////									mid = "NIL" + count;
////									count += 1;
////									dict.put(mention, mid);
////								}
////								
////						 }
////
////					}
////					else {
////						if(dict.containsKey(nominal)){
////							mid = dict.get(nominal);
////						}
////						else {
////							mid = "NIL" + count;
////							count += 1;
////							dict.put(nominal, mid);
////						}
////						
////					}
////					System.out.println(matcher.group("$NOM") + "\t" + matcher.group("$PERSON"));
//					
//					
//					
////					int start = bias +  matcher.start("$NOM");
////					int end = start + matcher.end("$NOM");
////					String wline1 = tokens.toString();
////					String wline2 = matcher.groupNodes().toString();
//
//
//					List<CoreMap> matchedTokens = matcher.groupNodes(1);
//					CoreLabel cl = (CoreLabel) matchedTokens.get(0);
//					int start  = bias + cl.beginPosition();
//					int end = bias + cl.endPosition() - 1;
//					
//					String wline = matcher.group("$NOM") + "\t" + doc_id + ":" + start + "-" + end + "\t" + mid + "\n";
////					osw.write("***********\n");
//					osw.write(wline);
////					osw.write("############" +"\n");
////					osw.write(wline2+"\n");
////					osw.write("@@@@@@@@@@@@@@@@@@@@@" + "\n");
//					osw.flush();
//					
//					
//				}
////				for (CoreLabel token : sentence.get(TokensAnnotation.class)){
////					String word = token.originalText();
////					String w = token.ner();
////					System.out.println(word + "\t" + w);
////				}
//			}	
//
//		}
//
//	}
//
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File dir = new File(NEWSFILEINPUTDIR);
		File[] files = dir.listFiles();
		int all = files.length;
		int done = 0;
		long start = System.currentTimeMillis();
		if(files != null){
			FileOutputStream failedFilefos = new FileOutputStream("failedeng.tab");
			OutputStreamWriter failedFileosw = new OutputStreamWriter(failedFilefos, "UTF-8");
			for(File file : files){
				try {
					done += 1;
					System.out.println("doing:" + done + "\t" + "all:" + all);
					String fileName = file.getName();
					System.out.println(fileName);
					if(fileName.endsWith("xml")){
//						System.out.println("GenMention:###########start");
//						engGenMention.GetMention(fileName, type);
//						getIndexFile(fileName, "df");
						getlocnam(fileName, "df");
//						System.out.println("GenMention:###########end");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.toString());
					failedFileosw.write(file.getName()+"\n");
					failedFileosw.write(e.toString()+"\n");
					continue;	
				}

			}
			failedFileosw.close();
			failedFilefos.close();
			long end = System.currentTimeMillis();
			System.out.println((end - start) + "s");
		}
		osw.close();
		fos.close();
		getIndexFile("ENG_NW_001278_20130827_F00013RT2.xml", "df");
	}


}
 