package com.pancm.test.esTest;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
	 * 创建链接
	 * 
	 * @param port
	 * @param hosts
	 * @return
	 */
	public static boolean build(int port, String... hosts) {
		boolean falg = false;
		Objects.requireNonNull(hosts, "nodes can not null");
		ArrayList<HttpHost> ahosts = new ArrayList<HttpHost>();
		for (String host : hosts) {
			ahosts.add(new HttpHost(host, port));
		}
		httpHosts = ahosts.toArray(new HttpHost[0]);
		try {
			init();
			falg = true;
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
			RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);
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
	private static final String COMMA_SIGN = ",";

	
	
	/*
	 * ES的mapping创建的基础类
	 */
	class EsBasicModelConfig implements Serializable {
		private static final long serialVersionUID = 1L;
		/*** 索引库 ***/
		private String index;
		private String type;
		private String settings;
		private String mappings;

		public EsBasicModelConfig(String index, String type) {
			this.index = index;
			this.type = type;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSettings() {
			return settings;
		}

		public void setSettings(SettingEntity settings) {
			this.settings = Objects.requireNonNull(settings, "setting can not null").toDSL();
		}

		public void setSettings(String settings) {
			this.settings = Objects.requireNonNull(settings, "setting can not null");
		}

		public String getMappings() {
			return mappings;
		}

		public void setMappings(String mappings) {
			this.mappings = mappings;
		}

		@Override
		public String toString() {
			return "EsBasicModelConfig [index=" + index + ", type=" + type + ", settings=" + settings + ", mappings="
					+ mappings + "]";
		}

	}

	/*
	 * setting 实体类的配置
	 */
	class SettingEntity implements Serializable {
		/**
		 * @Fields serialVersionUID : TODO
		 */
		private static final long serialVersionUID = 1L;
		private int numberOfShards = 5;
		private int number_of_routing_shards = 30;
		private int numberOfReplicas = 1;
		/***** 刷新频率 单位:秒 *********/
		private int refreshInterval = 5;
		/** 查询最大返回的时间 */
		private int maxResultWindow = 10000;

		public SettingEntity(int numberOfShards, int numberOfReplicas, int refreshInterval) {
			this.numberOfShards = numberOfShards;
			this.numberOfReplicas = numberOfReplicas;
			this.refreshInterval = refreshInterval;
		}

		public SettingEntity(int numberOfShards, int numberOfReplicas, int refreshInterval,
				int number_of_routing_shards, int maxResultWindow) {
			this.numberOfShards = numberOfShards;
			this.numberOfReplicas = numberOfReplicas;
			this.refreshInterval = refreshInterval;
			this.number_of_routing_shards = number_of_routing_shards;
			this.maxResultWindow = maxResultWindow;
		}

		public SettingEntity() {

		}

		public int getNumberOfShards() {
			return numberOfShards;
		}

		/**
		 * 分片数
		 * 
		 * @param numberOfShards 默认5
		 */
		public void setNumberOfShards(int numberOfShards) {
			this.numberOfShards = numberOfShards;
		}

		public int getNumberOfReplicas() {
			return numberOfReplicas;
		}

		/**
		 * 副本数
		 * 
		 * @param numberOfReplicas 默认1
		 */
		public void setNumberOfReplicas(int numberOfReplicas) {
			this.numberOfReplicas = numberOfReplicas;
		}

		public int getRefreshInterval() {
			return refreshInterval;
		}

		public int getNumber_of_routing_shards() {
			return number_of_routing_shards;
		}

		public void setNumber_of_routing_shards(int number_of_routing_shards) {
			this.number_of_routing_shards = number_of_routing_shards;
		}

		public int getMaxResultWindow() {
			return maxResultWindow;
		}

		public void setMaxResultWindow(int maxResultWindow) {
			this.maxResultWindow = maxResultWindow;
		}

		/**
		 * 刷新频率 单位:秒
		 * 
		 * @param refreshInterval 默认5秒 设置为-1为无限刷新
		 */
		public void setRefreshInterval(int refreshInterval) {
			if (refreshInterval < -1) {
				refreshInterval = -1;
			}
			this.refreshInterval = refreshInterval;
		}

		public String toDSL() {
			Map<String, Object> json = new HashMap<>();
			json.put("number_of_shards", numberOfShards);
			json.put("number_of_replicas", numberOfReplicas);
			json.put("refresh_interval", refreshInterval + "s");
			json.put("max_result_window", maxResultWindow);
			return json.toString();
		}

		@Override
		public String toString() {
			return "SettingEntity [numberOfShards=" + numberOfShards + ", numberOfReplicas=" + numberOfReplicas
					+ ", refreshInterval=" + refreshInterval + ", maxResultWindow=" + maxResultWindow + "]";
		}

	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		EsUtil.build(9200, "192.169.0.23,192.169.0.24");
		System.out.println("初始化成功!");

		close();
	}

}
