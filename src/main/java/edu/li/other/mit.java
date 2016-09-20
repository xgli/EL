/**
 * 
 */
package edu.li.other;



import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

import edu.mit.ll.mitie.*;
/**
 *date:Aug 6, 2016 4:13:44 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Aug 6, 2016 4:13:44 PM
 */
public class mit {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		System.out.println(System.getProperty("java.library.path"));
//		System.loadLibrary("javamitie");
		System.out.println("loading NER model...");
		NamedEntityExtractor ner = new NamedEntityExtractor("./mit-java/MITIE-models/spanish/ner_model.dat");
//		System.out.println("tags output by this ner model are:");
		StringVector possibleTags = ner.getPossibleNerTags();
//		for (int i = 0; i < possibleTags.size(); ++i)
//			System.out.println(possibleTags.get(i));
//		tokenize
		StringVector words = global.tokenize("The Jewish War on Nordic Sweden: Israeli Government openly exports African illegal immigrants to Sweden");
		EntityMentionVector entities = ner.extractEntities(words);
		System.out.println("number of entities found:" + entities.size());
		
//		for (int j = 0;  j < words.size(); j++){
//			for (int i = 0; i < entities.size(); ++i){
//				EntityMention entityMention = entities.get(i);
//				int temp = entityMention.getStart();
//				
//				for (;j<temp;j++){
//					System.out.print(words.get(j) + " ");
//				}
//				j = temp + 1;
//				String tag = possibleTags.get(entityMention.getTag());
//				System.out.print("<" + tag + ">" + words.get(temp) + "</" + tag + ">");
//			}
//			if (j < words.size())
//				System.out.print(words.get(j));	
//					
//			}
		
		for(int i = 0; i < entities.size(); ++i){
			EntityMention entity = entities.get(i);
			String tag = possibleTags.get(entity.getTag());
			System.out.println("Entity tag:" + tag + "\t Entity text");
			
//			String word = words.get(i);
//			int j = entity.getStart();
//			int m = entity.getEnd();
			
//			System.out.println(j + words.get(j) + m);
//			System.out.println(word + ":" +tag);
			printEntity(words, entity);
		}
	}
	
	public static void printEntity(StringVector words, EntityMention ent){
		for (int i = ent.getStart(); i < ent.getEnd(); ++i){
//			System.out.println("i:" + i);
			System.out.print(words.get(i) + " ");
		}
		System.out.println("");
	}
	
	
	
	

}
