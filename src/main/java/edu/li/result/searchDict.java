/**
 * 
 */
package edu.li.result;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.SearchHits;

/**
 *date:Jul 25, 2016 8:58:29 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jul 25, 2016 8:58:29 PM
 */
public class searchDict {
	
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
	
	public static  SearchHits getHits(String mention,String index, String type){
		Map<String, Object> templateParams = new HashMap<String, Object>();
		templateParams.put("mention", mention);
		
		TransportClient client = geTransportClient();
		SearchResponse actionGet = client.prepareSearch(index)
										.setTypes(type)										
										.setTemplateName("template_searchDict")
										.setTemplateType(ScriptService.ScriptType.FILE)
										.setTemplateParams(templateParams)						
//										.setQuery( QueryBuilders.termQuery("_id", "2"))
										.execute()
										.actionGet();
		return actionGet.getHits();		
	}
	
 
	
	
	
	
	
	

}
