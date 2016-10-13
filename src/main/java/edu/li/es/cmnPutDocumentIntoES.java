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
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;

import edu.stanford.nlp.io.IOUtils;

/**
 *date:Jul 24, 2016 7:31:38 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jul 24, 2016 7:31:38 PM
 */
public class cmnPutDocumentIntoES {

	/**
	 * @param args
	 */

	//产生es索引文件
	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator + "news" + File.separator;
	public static final String DFFILEINPUTDIR = "data" + File.separator + "xmlParse" + File.separator + "cmn" + File.separator + "df" + File.separator;
//	public static final String INDEXOUTDIR = "data" + File.separator + "esindex" + File.separator  + "cmn" + File.separator;
	
	//产生raw文件位置
//	public static final String NEWSFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator + "nw" + File.separator;
//	public static final String DFFILEINPUTDIR = "data" + File.separator + "raw" + File.separator + "cmn" + File.separator + "df" + File.separator;
	public static final String INDEXOUTDIR = "data" + File.separator + "rawloc" + File.separator  + "cmn" + File.separator;
	
	
	
	public static final String CLUSTERNAME ="elasticsearch"; //集群模型
	public static final String HOST = "10.110.6.43"; //服务器地址
	public static final int  PORT = 9300; //服务端口  TCP为9300 IP为9200
	
	static Map<String, String> map = new HashMap<String, String>();
	static Settings settings = ImmutableSettings.settingsBuilder().put(map).put("cluster.name",CLUSTERNAME)
								.put("client.transport.sniff", true).build();
	
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
//			line = TraToSim.TraToSim(line);
			
			int start = Integer.parseInt(line.split("\t")[0]);
			String lineText = line.split("\t")[1];
			
			List<Pinyin> pinyins = HanLP.convertToPinyinList(lineText);
	
			for (int i = 0; i < lineText.length(); i++){
				textIndex += lineText.substring(i,i+1) + ":" + (start + i) + ":" + pinyins.get(i) + " ";
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
		String fileInputPath;
		if(type.equals("news"))
			 fileInputPath = NEWSFILEINPUTDIR + fileName;
		else
			fileInputPath = DFFILEINPUTDIR + fileName;
		
		String fileOutPath = INDEXOUTDIR + fileName;
				
		String text = IOUtils.slurpFile(fileInputPath, "utf-8");
	
	
//		text = TraToSim.TraToSim(text);

		
		List<Pinyin> pinyins = HanLP.convertToPinyinList(text);
		String pinyinStr = "";
		for (int i = 0; i < text.length(); i++){
			pinyinStr += text.substring(i,i+1) + ":" + i + ":" + pinyins.get(i) + "\t";
		}				
		
		 FileOutputStream fos = new FileOutputStream(fileOutPath);
		 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		 osw.write(pinyinStr);
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
		File dir = new File(NEWSFILEINPUTDIR);
		File[] files = dir.listFiles();
		for(File file : files){
//			String fileName = "CMN_NW_000020_20150604_F00100013.nw.ltf.xml";
			String fileName = file.getName();
			getIndexFile(fileName, "news");
		}
		
		dir = new File(DFFILEINPUTDIR);
		files = dir.listFiles();
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
				IndexResponse response =  client.prepareIndex("chinese_row_kbp2016", "text", fileName)
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
		
//		产生es索引文件
//		genES();
		putES();
		
		//产生位置
//		genRawloc();
		
	


	}

}
