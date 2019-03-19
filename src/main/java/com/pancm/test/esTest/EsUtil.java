package com.pancm.test.esTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @Title: EsUtil
 * @Description: ES工具类
 * @Version:1.0.0
 * @author pancm
 * @date 2019年3月19日
 */
public final class EsUtil {

	private EsUtil() {

	}
	
	/**
	 *   创建链接
	 * @param port
	 * @param hosts
	 * @return
	 */
	public static boolean build(int port,String... hosts) {
		boolean falg=false;
		Objects.requireNonNull(hosts,"nodes can not null");
		ArrayList<HttpHost> ahosts = new ArrayList<HttpHost>();
		for(String host:hosts) {
			ahosts.add(new HttpHost(host,port));
		}
		httpHosts= ahosts.toArray(new HttpHost[0]);
		try {
			init();
			falg=true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return falg;
		
	}
	
	public static boolean creatIndex() {
		
		return false;
	}
	
	
	
	/*
	 * 初始化服务
	 */
	private static void init() throws IOException {
		if (client == null) {
			RestClientBuilder restClientBuilder =RestClient.builder(httpHosts);
			client = new RestHighLevelClient(restClientBuilder);
		}
	}

	/*
	 * 关闭服务
	 */
	private static void close() throws IOException {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				throw e;
			}
		}
	}

	private static String[] elasticIps;
	private static int elasticPort;
	private static HttpHost[] httpHosts;
	private static RestHighLevelClient client = null;
	private static final String COMMA_SIGN=",";
  
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
