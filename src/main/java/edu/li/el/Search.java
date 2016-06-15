/**
 * 
 */
package edu.li.el;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;


/**
 *date:Jun 14, 2016 9:09:16 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 14, 2016 9:09:16 PM
 */
public class Search {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String querytext = "中国";
		JSONStringer js = new JSONStringer();
		JSONArray fileds = new JSONArray();
		String[] fileds_match = {   "rs_label_zh", 
				  					"rs_label_zh.raw", 
				  					"f_type.object.name_zh", 
				  					"f_type.object.name_zh.raw", 
				  					"f_common.topic.alias_zh", 
				  					"f_common.topic.alias_zh.raw", 
				  					"f_base.schemastaging.context_name.nickname" };
		
		String[] fileds_show = {   "rs_label_zh",
			      				   "f_common.topic.description_zh"};
		
//		fileds.put(fileds_match);	
		for(String match : fileds_match){
			fileds.put(match);
		}
		
		
		JSONObject multi_match  = new JSONObject();
		multi_match.put("query", querytext);
		multi_match.put("type", "most_fields").put("operator", "and").put("fields",fileds);
		
		JSONArray must = new JSONArray();
		must.put(new JSONObject().put("exists", new JSONObject().put("field", new JSONArray().put("f_common.topic.description_zh"))));
		must.put(new JSONObject().put("query", new JSONObject().put("match", new JSONObject().put("r_type",new JSONObject().put("operator", "and").put("query", "f_location.location")))));
		
		JSONObject filter =  new JSONObject().put("bool", new JSONObject().put("must", must));

		JSONObject filtered = new JSONObject().put("query", new JSONObject().put("multi_match", multi_match)).put("filter", filter);
		
		JSONArray fileds_show_js = new JSONArray();// .put(fileds_show);
		for (String show : fileds_show){
			fileds_show_js.put(show);
		}
		
		JSONObject queryAll = new JSONObject().put("fields", fileds_show_js).put("query", new JSONObject().put("filtered", filtered));
		
//		js.object().key("query").value(queryAll).endObject();
//		js.value(queryAll).endObject();
//		js.object().value(queryAll).endObject();

		js.object().key("fields").value(fileds_show_js).key("query").value(new JSONObject().put("filtered", filtered)).endObject();
		System.out.println(js.toString());
	}

}
