package com.zans.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.config.MyTools;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


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
    public static String get(String url, JSONObject json) throws Exception {
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
			// 增加http请求超时设置
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000)
					.setConnectionRequestTimeout(30000).setSocketTimeout(60000).build();
			method.setConfig(requestConfig);
		if (method != null ) {
				// 建立一个NameValuePair数组，用于存储欲传送的参数
				method.addHeader("Content-type",
						"application/json; charset=utf-8");


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
	public static String post(String postUrl) throws Exception {
		CloseableHttpClient httpClient= null;
		HttpPost method = null;
		String body = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			method = new HttpPost(postUrl);
			// 增加http请求超时设置
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(60000)
					.setConnectionRequestTimeout(60000).setSocketTimeout(60000).build();
			method.setConfig(requestConfig);
			if (method != null ) {
				// 建立一个NameValuePair数组，用于存储欲传送的参数
				method.addHeader("Content-type",
						"application/json; charset=utf-8");
				method.setHeader("Accept", "application/json");

				HttpResponse response = httpClient.execute(method);

				int statusCode = response.getStatusLine().getStatusCode();

				// logger.debug("statusCode:" + statusCode);
				if (statusCode != HttpStatus.SC_OK) {
					logger.error("Method failed:" + response.getStatusLine());
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
			// 增加http请求超时设置
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000)
					.setConnectionRequestTimeout(10000).setSocketTimeout(10000).build();
			method.setConfig(requestConfig);
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
		String url="http://192.168.8.78:9200/radius_acct_log/_search";
		String data = "{\"size\":0,\"query\":{\"bool\":{\"must\":[{\"range\":{\"acct_stop_time\":{\"gt\":\"2020-01-01 00:00:00\",\"include_upper\":true}}}],\"adjust_pure_negative\":true,\"boost\":1}},\"version\":false,\"explain\":false,\"_source\":false,\"aggregations\":{\"group\":{\"terms\":{\"field\":\"username\",\"size\":2147483647,\"min_doc_count\":1,\"shard_min_doc_count\":0,\"show_term_doc_count_error\":false,\"order\":[{\"_count\":\"desc\"},{\"_key\":\"asc\"}]},\"aggregations\":{\"having\":{\"bucket_selector\":{\"buckets_path\":{\"groupCount\":\"_count\"},\"script\":{\"source\":\"params.groupCount >= 10\",\"lang\":\"painless\"},\"gap_policy\":\"skip\"}}}}}}";



		try {

			List<Map<String,Object>> list = new ArrayList<>();
			Map<String,Object> map = new HashMap<>();
			String postTest=post(url,data);
			System.out.println("POST请求:"+postTest);
			System.out.println("解析结果:"+getAggData(postTest));
			JSONObject jsonObject =MyTools.toJson(postTest);

			String aggregations=jsonObject.getString("aggregations");
			System.out.println("aggregations:"+aggregations);
			JSONObject jsonObject2 =MyTools.toJson(aggregations);
			System.out.println("jsonObject2:"+jsonObject2);
			String groupby = jsonObject2.getString("groupby");
			System.out.println("groupby:"+groupby);
			JSONArray jsonArray = JSON.parseArray(MyTools.toJson(groupby).getString("buckets"));
			System.out.println("buckets:"+jsonArray);
//			for (int i = 0; i < jsonArray.size(); i++) {
//				JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
//				String  jsonObject.getJSONObject("key").get("files");
//
//			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private static JSONArray getAggData(String data){
		JSONObject jsonObject =MyTools.toJson(data).getJSONObject("aggregations");
		JSONArray jsonArray = JSON.parseArray(jsonObject.getJSONObject("group").getString("buckets"));
		return  jsonArray;
	}
}
