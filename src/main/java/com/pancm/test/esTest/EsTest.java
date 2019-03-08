package com.pancm.test.esTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * @Title: EsTest
 * @Description: Java High Level REST Client Es高级客户端使用教程
 * 
 * @since jdk 1.8
 * @Version:1.0.0
 * @author pancm
 * @date 2019年3月5日
 */
public class EsTest {
	
    private static String elasticIp="192.169.0.23";
    private static int elasticPort=9200;
	
    private static RestHighLevelClient client=null;
	
	public static void main(String[] args) {
		init();
		try {
			careatindex();
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	/*
	 * 初始化服务
	 */
	private static void init() {
		client = new RestHighLevelClient(
	  RestClient.builder(new HttpHost(elasticIp, elasticPort, "http")));
	  
	}
	
	/*
	 * 关闭服务
	 */
	private static void close() {
		if(client!=null) {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建索引
	 * @throws IOException 
	 */
	private static void careatindex() throws IOException {
		String index = "user";
		String type = "userindex";
		// 唯一编号
		String id = "1";

		IndexRequest request = new IndexRequest(index, type, id);
		
		/*
		 *      第一种方式，通过jsonString进行创建
		 */
		// json
		String jsonString = "{" + "\"user\":\"pancm\"," + "\"postDate\":\"2019-03-08\","
				+ "\"message\":\"study Elasticsearch\"" + "}";
		
		request.source(jsonString, XContentType.JSON);
		
		
		/*
		 *  第二种方式，通过map创建,，会自动转换成json的数据
		 */
		
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("user", "pancm");
		jsonMap.put("postDate", "2019-03-08");
		jsonMap.put("message", "study Elasticsearch");
		
		request.source(jsonMap);
		
		/*
		 *   第三种方式 :  通过XContentBuilder对象进行创建
		 */
		
		XContentBuilder builder = XContentFactory.jsonBuilder();
		builder.startObject();
		{
		    builder.field("user", "pancm");
		    builder.timeField("postDate", "2019-03-08");
		    builder.field("message", "study Elasticsearch");
		}
		builder.endObject();
		request.source(builder);

			
		IndexResponse indexResponse=client.index(request, RequestOptions.DEFAULT);
		
		
		String index1 = indexResponse.getIndex();
		String type1 = indexResponse.getType();
		String id1 = indexResponse.getId();
		long version = indexResponse.getVersion();
		if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
		    
		} else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
		    
		}
		ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
		if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
		    
		}
		if (shardInfo.getFailed() > 0) {
		    for (ReplicationResponse.ShardInfo.Failure failure :
		            shardInfo.getFailures()) {
		        String reason = failure.reason(); 
		    }
		}
		
		System.out.println("创建成功！");
	}
}
