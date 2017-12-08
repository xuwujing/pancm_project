package com.pancm.test.httpTest;


import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;

/**
 * @author ZERO
 * @Data 2017-4-18 2:25:44
 */
public class HttpUtil {
          
	/**
	 * @param String
	 * @param JSONObject
	 * @return
	 */
	public static String  doGet(String url,JSONObject json){
		HttpRequest hr = new HttpRequest(url, HttpRequest.METHOD_GET); 
    	if ("https".equalsIgnoreCase(url.substring(0, 5))) {
    		hr.trustAllCerts().trustAllHosts();
    	}
	   String contentType="application/json";  
	   if(contentType!=null){
    		hr.contentType(contentType);
    	}
	   return hr.send(json.toString()).body();
	}
	

	public static String  doGet1(String url){
		HttpRequest hr = new HttpRequest(url, HttpRequest.METHOD_GET); 
    	if ("https".equalsIgnoreCase(url.substring(0, 5))) {
    		hr.trustAllCerts().trustAllHosts();
    	}
	   String contentType="application/json";  
	   if(contentType!=null){
    		hr.contentType(contentType);
    	}
	       
	   return hr.body();
	}
	
	
	public static String  doPost(String url,JSONObject json){
		HttpRequest hr = new HttpRequest(url, HttpRequest.METHOD_POST); 
    	if ("https".equalsIgnoreCase(url.substring(0, 5))) {
    		hr.trustAllCerts().trustAllHosts();
    	}
	   String contentType="application/json"; 
	   if(contentType!=null){
    		hr.contentType(contentType);
    	}
    	return hr.send(json.toJSONString()).body();
	}
	
	public static String  doPost(String url,String json){
		HttpRequest hr = new HttpRequest(url, HttpRequest.METHOD_POST); 
    	if ("https".equalsIgnoreCase(url.substring(0, 5))) {
    		hr.trustAllCerts().trustAllHosts();
    	}
	   String contentType="application/json"; 
	   if(contentType!=null){
    		hr.contentType(contentType);
    	}
    	return hr.send(json).body();
	}
	
}
