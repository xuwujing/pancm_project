package com.pancm.test.esTest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title: EsHighLevelRestSearchTest
 * @Description: Java High Level REST Client Es高级客户端查询使用使用教程 (Search查询使用教程)
 *               官方文档地址:
 *               https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html
 * @Version:1.0.0
 * @author pancm
 * @date 2019年3月12日
 */
public class EsHighLevelRestSearchTest {

	private static String elasticIp = "192.169.0.23";
	private static int elasticPort = 9200;

	private static Logger logger = LoggerFactory.getLogger(EsHighLevelRestSearchTest.class);

	private static RestHighLevelClient client = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			init();

			close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 初始化服务
	 */
	private static void init() {
		client = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticIp, elasticPort, "http")));

	}

	/*
	 * 关闭服务
	 */
	private static void close() {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 查询使用示例
	 * 
	 * @throws IOException
	 */
	private void search() throws IOException {

		/*
		 * 查询集群所有的索引
		 * 
		 */
		SearchRequest searchRequestAll = new SearchRequest();

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequestAll.source(searchSourceBuilder);

		// 同步查询
		SearchResponse searchResponseAll = client.search(searchRequestAll, RequestOptions.DEFAULT);

		System.out.println("查询总数:" + searchResponseAll.getHits().getHits());

		// 查询指定的索引库
		SearchRequest searchRequest = new SearchRequest("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		// 设置查询条件
		sourceBuilder.query(QueryBuilders.termQuery("user", "pancm"));
		// 设置起止和结束
		sourceBuilder.from(0);
		sourceBuilder.size(5);
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
		searchRequest.routing("routing");
		searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
		searchRequest.preference("_local");

		searchRequest.source(sourceBuilder);

		// 同步查询
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		System.out.println("");
		
		
		
		
	}
}
