/**
 * 
 */
package edu.li.es;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.netty.util.HashedWheelTimer;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import edu.li.other.testProps;


/**
 *date:Jun 15, 2016 10:07:13 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 15, 2016 10:07:13 AM
 */
public class Search {

	/**
	 * @param args
	 */
	
	public static final String CLUSTERNAME ="elasticsearch"; //集群模型
	public static final String INDEX = "base_kb"; //索引名称
	public static final String TYPE = "entity";//类型名称
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
	
	public static  SearchHits getHits(String mention,String mention_type, String lang){
		Map<String, Object> templateParams = new HashMap<String, Object>();
		templateParams.put("mention_"+mention_type, mention);
		
		TransportClient client = geTransportClient();
		SearchResponse actionGet = client.prepareSearch(INDEX)
										.setTypes(TYPE)										
										.setTemplateName("template_"+mention_type + "_" + lang)
										.setTemplateType(ScriptService.ScriptType.FILE)
										.setTemplateParams(templateParams)						
//										.setQuery( QueryBuilders.termQuery("_id", "2"))
										.execute()
										.actionGet();
		return actionGet.getHits();		
	}
	
	public static  SearchHits getHitsById(String mid){
		Map<String, Object> templateParams = new HashMap<String, Object>();
		templateParams.put("mid", mid);
		
		TransportClient client = geTransportClient();
		SearchResponse actionGet = client.prepareSearch(INDEX)
										.setTypes(TYPE)										
										.setTemplateName("template_search_id")
										.setTemplateType(ScriptService.ScriptType.FILE)
										.setTemplateParams(templateParams)						
//										.setQuery( QueryBuilders.termQuery("_id", "2"))
										.execute()
										.actionGet();
		return actionGet.getHits();		
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
//		// TODO Auto-generated method stub	
//		SearchHit hit = getHitsById("f_m.015fr").getHits()[0];
////		SearchHit hit = hits.getHits()[0];
//		System.out.println(hit.getFields().get("f_common.topic.description_zh").getValue());
		
////		
		String mention = "上海市";
		String mention_type = "GPE";
		String lang = "cmn";

		SearchHits hits = getHits(mention, mention_type, lang);
//		System.out.println(hits.totalHits());
		for (SearchHit hit : hits.getHits()){ //getHits 的使用			
			if(hit.getScore() >= 1){
				System.out.println(hit.getId());
				System.out.println(hit.getScore());
				System.out.println(hit.getFields().get("rs_label_zh").getValue());
				System.out.println(hit.getFields().get("f_common.topic.description_zh").getValue());			
			}

		}

	}

}
