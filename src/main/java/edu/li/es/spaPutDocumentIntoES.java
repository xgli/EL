/**
 * 
 */
package edu.li.es;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

/**
 *date:Jul 24, 2016 7:31:38 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jul 24, 2016 7:31:38 PM
 */
public class spaPutDocumentIntoES {

	/**
	 * @param args
	 */

	//解析成es索引格式
	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "spa" + File.separator + "news" + File.separator;
	public static final String DFFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "spa" + File.separator + "df" + File.separator;
//	public static final String INDEXOUTDIR = "data" + File.separator + "esindex" + File.separator  + "spa" + File.separator;
	
	//查看位置信息
//	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "spa" + File.separator + "nw" + File.separator;
//	public static final String DFFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "spa" + File.separator + "df" + File.separator;
	public static final String INDEXOUTDIR = "data" + File.separator + "rawloc" + File.separator  + "spa" + File.separator;
//	
	
	
	public static final String CLUSTERNAME ="elasticsearch"; //集群模型
	public static final String HOST = "10.110.6.43"; //服务器地址
	public static final int  PORT = 9300; //服务端口  TCP为9300 IP为9200
	
	static Map<String, String> map = new HashMap<String, String>();
	static Settings settings = ImmutableSettings.settingsBuilder().put(map).put("cluster.name",CLUSTERNAME)
								.put("client.transport.sniff", true).build();
	
	static Properties properties = PropertiesUtils.asProperties(
				"annotators","tokenize,ssplit",
				"ssplit.isOneSentence","true",
				"tokenize.language","es"
			);	

	static StanfordCoreNLP pipline = new StanfordCoreNLP(properties);
	
	private static TransportClient client;
	
	static {
		try {
			Class<?> clazz = Class.forName(TransportClient.class.getName());
			Constructor<?> constructor = clazz.getDeclaredConstructor(Settings.class);
			constructor.setAccessible(true);
			client = (TransportClient) constructor.newInstance(settings);
			client.addTransportAddress(new InetSocketTransportAddress(HOST, PORT));
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static synchronized TransportClient geTransportClient(){
		return client;
	}	
	
	static{//判断文件目录是否存在
		File file;
		file = new File(INDEXOUTDIR);
		if(!file.exists() && !file.isDirectory())
			file.mkdirs();
	}
	
	public static void getIndexFile(String fileName, String type) throws IOException{
		System.out.println(fileName);
//		String fileInputPath = fileName;
		String fileInputPath;
		if(type.equals("news"))
			 fileInputPath = NEWSFILEINPUTDIR + fileName;
		else
			fileInputPath = DFFILEINPUTDIR + fileName;
		
		String fileOutPath = INDEXOUTDIR + fileName;
				
		String text = IOUtils.slurpFile(fileInputPath, "utf-8");
		String[] lines = text.split("\n");
		String  textIndex = "";
		
		for(String line : lines){
			if(line.equals(""))
				continue;				
//			line = line.replace("\uFEFF", " ").replace("\u0095", " ").replace("\u2003", " ").replace("\uf09a", " ");
			String[] tokens = line.split("\t");
			int start = Integer.parseInt(tokens[0]);	
			
//			System.out.println(start);
			String lineText = line.split("\t")[1];
				
			lineText = lineText.trim();
			if(lineText.equals(""))
				continue;

//			System.out.println(lineText);
			Annotation doc = new Annotation(lineText);
			pipline.annotate(doc);
			for(CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)){
//				sentence.
				for (CoreLabel token : sentence.get(TokensAnnotation.class)){					
					String word = token.get(TextAnnotation.class);
					word = token.originalText();
					int word_start = start + token.beginPosition();
					int word_end = start + token.endPosition() - 1;
					String es_line = word + ":" + word_start + ":" + word_end + " ";
					textIndex = textIndex + es_line;
//					System.out.println(es_line);				
				}
			}
		 }
		
		 FileOutputStream fos = new FileOutputStream(fileOutPath);
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		 osw.write(textIndex);
		 osw.close();
		 fos.close();			
	}
	
	
	public static void getRawLoc(String fileName, String type) throws IOException{
		System.out.println(fileName);
//		String fileInputPath = fileName;
		String fileInputPath;
		if(type.equals("news"))
			 fileInputPath = NEWSFILEINPUTDIR + fileName;
		else
			fileInputPath = DFFILEINPUTDIR + fileName;
		
		String fileOutPath = INDEXOUTDIR + fileName;
				
		String text = IOUtils.slurpFile(fileInputPath, "utf-8");			
				
		String textIndex = "";
		Annotation doc = new Annotation(text);
		pipline.annotate(doc);
		for(CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)){
			for (CoreLabel token : sentence.get(TokensAnnotation.class)){
				String word = token.get(TextAnnotation.class);
				word = token.originalText();
				int word_start =  token.beginPosition();
				int word_end = token.endPosition() - 1;
				String es_line = word + ":" + word_start + ":" + word_end + "\t";
				textIndex = textIndex + es_line;			
			}
		}		 
		
		 FileOutputStream fos = new FileOutputStream(fileOutPath);
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		 osw.write(textIndex);
		 osw.close();
		 fos.close();		
		
	}
	
	public static void testCharater(String fileName, String type) throws IOException{

//		String fileInputPath = fileName;
		String fileInputPath;
		if(type.equals("news"))
			 fileInputPath = NEWSFILEINPUTDIR + fileName;
		else
			fileInputPath = DFFILEINPUTDIR + fileName;
		
				
		String text = IOUtils.slurpFile(fileInputPath, "utf-8");
		int a = text.length();
		int b = text.codePointCount(0, text.length());
		if(a != b)
			System.out.println(fileName);
//			System.out.println(a);
//			System.out.println(b);

	}	
	
	
	public static void genES() throws IOException{
//		File dir = new File(NEWSFILEINPUTDIR);
//		File[] files = dir.listFiles();
//		for(File file : files){
////			String fileName = "CMN_NW_000020_20150604_F00100013.nw.ltf.xml";
//			String fileName = file.getName();
//			getIndexFile(fileName, "news");
//		}
//		
		File dir = new File(DFFILEINPUTDIR);
		File[] files = dir.listFiles();
		for(File file : files){
//			String fileName = "CMN_NW_000020_20150604_F00100013.nw.ltf.xml";
			String fileName = file.getName();
			getIndexFile(fileName, "df");
		}
		
	}
	
	public static void genRawloc() throws IOException{
		File dir = new File(NEWSFILEINPUTDIR);
		File[] files = dir.listFiles();
		for(File file : files){
//			String fileName = "CMN_NW_000020_20150604_F00100013.nw.ltf.xml";
			String fileName = file.getName();
			getRawLoc(fileName, "news");
		}
		
		dir = new File(DFFILEINPUTDIR);
		files = dir.listFiles();
		for(File file : files){
//			String fileName = "CMN_NW_000020_20150604_F00100013.nw.ltf.xml";
			String fileName = file.getName();
			getRawLoc(fileName, "df");
		}
		
	}
	
	public static void testCh() throws IOException{
		
		File dir = new File(NEWSFILEINPUTDIR);
		File[] files = dir.listFiles();
		for(File file : files){
//			String fileName = "CMN_NW_000020_20150604_F00100013.nw.ltf.xml";
			String fileName = file.getName();
			testCharater(fileName, "news");
		}
		
		dir = new File(DFFILEINPUTDIR);
		files = dir.listFiles();
		for(File file : files){
//			String fileName = "CMN_NW_000020_20150604_F00100013.nw.ltf.xml";
			String fileName = file.getName();
			testCharater(fileName, "df");
		}	
		
	}	
	
	
	
	public static void putES() throws IOException{
		
//		将文件放到es中
		File dir = new File(INDEXOUTDIR);
		File[] files = dir.listFiles();
		for(File file : files){
			System.out.println(file.getName());
			String fileName = file.getName().replace(".xml", "");
			String indexText = IOUtils.slurpFile(file, "utf-8");

			TransportClient client = geTransportClient();
			IndexResponse response =  client.prepareIndex("spanish_raw_kbp2016", "text", fileName)
							.setSource("snt",indexText)
							.get();
			if(!response.isCreated()){
				System.out.println("fialed" + fileName);
			}
		}
	//	
		
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		//测试特殊字符
//		testCh();		
		//产生es索引文件
//		genES();		
		//产生位置
//		genRawloc();
//		putES();
		
		String textIndex = "";
		String lineText = "Cámara Americana-Nicaragüense (Amcham) a--";
		Annotation doc = new Annotation(lineText);
		pipline.annotate(doc);
		for(CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)){
			for (CoreLabel token : sentence.get(TokensAnnotation.class)){
//				String word = token.get(TextAnnotation.class);
				String word = token.originalText();
				System.out.println(word);
//				if(word.indexOf("-") > 0){
//					int s = token.beginPosition();
//					String[] segs = word.split("-");
//					int num = segs.length;
//					for (int i = 0; i < num-1; i++){
//						String seg = segs[i];
//						int seg_start = s;
//						int seg_end = s + seg.length() - 1;
//						String outline = seg + ":" + seg_start + ":" + seg_end + " ";
//						System.out.println(outline);
//						System.out.println("-" + ":" + (seg_end + 1) + ":" + (seg_end + 1));
//						s = seg_end + 2;
//					}
//					System.out.println(segs[num-1] + ":" + s + ":" + (segs[num-1].length() + s - 1));				
//					continue;
//				}
//				System.out.print(word + ":");
//				System.out.print(start);
//				System.out.print(":");
				
				int word_start = token.beginPosition();
				int word_end = token.endPosition() - 1;
				
				int token_len = word_end - word_start + 1;
				int word_len = word.length();
//				System.out.println(token.);
//				if (token_len != word_len)
//					System.out.println(word);
				System.out.println(token.size());
//				token.
				
				String es_line = word + ":" + word_start + ":" + word_end + " ";
				textIndex = textIndex + es_line;
				System.out.println(es_line);				
			}

		}
		
		
	


	}

}
