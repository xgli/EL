/**
 * 
 */
package com.freebase.samples;

/**
 *date:Jun 14, 2016 3:05:13 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 14, 2016 3:05:13 PM
 */

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;

import java.io.FileInputStream;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SearchExample {
  public static Properties properties = new Properties();
  public static void main(String[] args) {
    try {
//      properties.load(new FileInputStream("freebase.properties"));
      HttpTransport httpTransport = new NetHttpTransport();
      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
      JSONParser parser = new JSONParser();
      GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/search");
      url.put("query", "Apple");
//    url.put("filter", "(all type:/music/artist created:\"The Lady Killer\")");
      url.put("output","(description)");
      url.put("limit", "2");
      url.put("indent", "true");
      url.put("key", "AIzaSyDe-kpgimjvSitPt2H078YVWg8EWAKGOpI");
      HttpRequest request = requestFactory.buildGetRequest(url);
      HttpResponse httpResponse = request.execute();
      JSONObject response = (JSONObject)parser.parse(httpResponse.parseAsString());
      JSONArray results = (JSONArray)response.get("result");
      for (Object result : results) {
    	String name = JsonPath.read(result,"$.name").toString();
    	String mid = JsonPath.read(result,"$.mid").toString();
    	String description = JsonPath.read(result, "$.output.description./common/topic/description").toString();
    	System.out.println(name);
    	System.out.println(mid);
    	System.out.println(description);
//        System.out.println(JsonPath.read(result, "$.mid") +":"+JsonPath.read(result,"$.output.description./common/topic/description").toString());
//        System.out.println(result.toString());
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
