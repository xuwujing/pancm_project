package com.pancm.test.httpTest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.pancm.util.MyTools;


/**
 * The type Http client util.
 *
 * @author pancm
 * @Title: HttpUtil
 * @Description: Http请求工具类
 * @Version:1.0.0
 * @date 2018年4月3日
 */
public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);


    /**
     * * 封装HTTP GET方法
     * 有参数的Get请求
     *
     * @param url 发送请求的URL
     * @param map 请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @return URL 所代表远程资源的响应结果
     * @throws Exception the exception
     */
    public static String get(String url,Map<String,Object> map) throws Exception {
		 StringBuffer sb=new StringBuffer();
		  //构建请求参数
		 if(map!=null&&map.size()>0){
	            for (Entry<String, Object> e : map.entrySet()) {  
	            	sb.append(e.getKey());
	            	sb.append("=");  
	            	sb.append(e.getValue());  
	            	sb.append("&");  
	            }  
		  }
	   return  get(url+"?"+sb.toString());
	}

    /**
     * 封装HTTP GET方法
     *
     * @param url  发送请求的URL
     * @param json 请求json参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @return string
     * @throws Exception the exception
     */
    public static String get(String url,JSONObject json) throws Exception {
		 StringBuffer sb=new StringBuffer();
		  //构建请求参数
		 if(json!=null&&json.size()>0){
	            for (Entry<String, Object> e : json.entrySet()) {  
	            	sb.append(e.getKey());
	            	sb.append("=");  
	            	sb.append(e.getValue());  
	            	sb.append("&");  
	            }  
		  }
	     return  get(url+"?"+sb.toString());
	}

    /**
     * 封装HTTP GET方法
     * 有参数的Get请求
     *
     * @param url 请求url     请求参数应该是  localhost:8080/test?name1=value1&name2=value2 的形式。
     * @return string
     * @throws Exception the exception
     */
    public static String get(String url) throws Exception {
		CloseableHttpClient httpClient= null;
		HttpGet method = null;
		String body = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			method = new HttpGet(url);
		if (method != null ) {
				// 建立一个NameValuePair数组，用于存储欲传送的参数
				method.addHeader("Content-type",
						"application/json; charset=utf-8");
				method.setHeader("Accept", "application/json");

				HttpResponse response = httpClient.execute(method);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					logger.error("Method failed:" + response.getStatusLine());
					throw new Exception("statusCode：" + statusCode);
				}
				body = EntityUtils.toString(response.getEntity());
			}	
		} catch (IOException e) {
			// 网络错误
			throw e;
		} finally {
			if(httpClient!=null){
				httpClient.close();
				httpClient= null;
				method = null;
			}
		}
		return body;
	}


    /**
     * post 请求
     *
     * @param postUrl    the post url
     * @param parameters the parameters
     * @return string
     * @throws Exception the exception
     */
    public static String post(String postUrl,String parameters) throws Exception {
		CloseableHttpClient httpClient= null;
		HttpPost method = null;
		String body = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			method = new HttpPost(postUrl);
		if (method != null & parameters!=null) {
				// 建立一个NameValuePair数组，用于存储欲传送的参数
				method.addHeader("Content-type",
						"application/json; charset=utf-8");
				method.setHeader("Accept", "application/json");
				method.setEntity(new StringEntity(parameters, Charset
						.forName("UTF-8")));

				HttpResponse response = httpClient.execute(method);

				int statusCode = response.getStatusLine().getStatusCode();

				// logger.debug("statusCode:" + statusCode);
				if (statusCode != HttpStatus.SC_OK) {
					logger.error("Method failed:" + response.getStatusLine());
					logger.error("Request:"+parameters);
					throw new Exception("statusCode：" + statusCode);
				}

				// Read the response body
				body = EntityUtils.toString(response.getEntity());
			}	
		} catch (IOException e) {
			// 网络错误
			throw e;
		} finally {
			if(httpClient!=null){
				httpClient.close();
				httpClient= null;
				method = null;
			}
			// logger.debug("调用接口状态：" + status);
		}
		return body;
	}

    /**
     * put 请求
     *
     * @param postUrl    the post url
     * @param parameters the parameters
     * @return string
     * @throws Exception the exception
     */
    public static String put(String postUrl,String parameters) throws Exception {
		CloseableHttpClient httpClient= null;
		HttpPut method = null;
		String body = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			method = new HttpPut(postUrl);
		if (method != null & MyTools.isNotEmpty(parameters)) {
				// 建立一个NameValuePair数组，用于存储欲传送的参数
				method.addHeader("Content-type",
						"application/json; charset=utf-8");
				method.setHeader("Accept", "application/json");
				method.setEntity(new StringEntity(parameters, Charset
						.forName("UTF-8")));
				HttpResponse response = httpClient.execute(method);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					logger.error("Method failed:" + response.getStatusLine());
					logger.error("Request:"+parameters);
					throw new Exception("statusCode：" + statusCode);
				}

				// Read the response body
				body = EntityUtils.toString(response.getEntity());
			}	
		} catch (IOException e) {
			// 网络错误
			throw e;
		} finally {
			if(httpClient!=null){
				httpClient.close();
				httpClient= null;
				method = null;
			}
		}
		return body;
	}

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		String url="http://localhost:8080/montnets/validphone";
		JSONObject json=new JSONObject();
		JSONObject json2=new JSONObject();
		json2.put("datatype", 1);
		json2.put("index", 1);
		json2.put("pagesize", 10);
		json2.put("startlastupdatetm", "2018-03-12 09:49:47.000");
		
		json.put("seqid", "201707052020666666");
		json.put("timestamp", "20170705202015666");
		json.put("token", "890ecc582bc0512df9e3548178782eda");
		json.put("encryptsign", 1);
		
		try {
			
//			String  url1="http://localhost:8080/montnets/validphone?svccont=85a7c5523ccecbc5b7753e00246603cc76bb30486b212a43b4c8c3347a0b6972160eaad6df64d7d9e3af9529594dc48bfa39213404803aa461ddf5abc37152fa9f1e2d7c318d0d5f7dc85673dfa12cf559a044f3bd909e99a800b9057c7d81ba&encryptsign=1&seqid=201707052020666666&timestamp=2017-07-05%2020:20:15%20666&token=b48630d4ec5d11982fc85e2e1456c2e0&";
			String getTest1=get(url,json);
			System.out.println("GET请求:"+getTest1);
			
			String postTest=post(url,json.toJSONString());
			System.out.println("POST请求:"+postTest);
			
			String pupTest=put(url,json.toJSONString());
			System.out.println("PUT请求:"+pupTest);
			
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
