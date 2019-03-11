package com.pancm.test.esTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;

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

	private static String elasticIp = "192.169.0.23";
	private static int elasticPort = 9200;

	private static RestHighLevelClient client = null;

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
	 * 创建索引
	 * 
	 * @throws IOException
	 */
	private static void careatindex() throws IOException {
		String index = "user";
		String type = "userindex";
		// 唯一编号
		String id = "1";

		IndexRequest request = new IndexRequest(index, type, id);

		/*
		 * 第一种方式，通过jsonString进行创建
		 */
		// json
		String jsonString = "{" + "\"user\":\"pancm\"," + "\"postDate\":\"2019-03-08\","
				+ "\"message\":\"study Elasticsearch\"" + "}";

		request.source(jsonString, XContentType.JSON);

		/*
		 * 第二种方式，通过map创建,，会自动转换成json的数据
		 */

		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("user", "pancm");
		jsonMap.put("postDate", "2019-03-08");
		jsonMap.put("message", "study Elasticsearch");

		request.source(jsonMap);

		/*
		 * 第三种方式 : 通过XContentBuilder对象进行创建
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

		IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
		
		
		
		//对响应结果进行处理
		
		String index1 = indexResponse.getIndex();
		String type1 = indexResponse.getType();
		String id1 = indexResponse.getId();
		long version = indexResponse.getVersion();
		// 如果是新增/修改的话的话
		if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {

		} else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {

		}
		ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
		if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

		}
		if (shardInfo.getFailed() > 0) {
			for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
				String reason = failure.reason();
			}
		}

		System.out.println("创建成功！");
	}

	/**
	 * 查询数据
	 * 
	 * @throws IOException
	 */
	private static void get() {
		String index = "user";
		String type = "userindex";
		// 唯一编号
		String id = "1";
		// 创建查询请求
		GetRequest getRequest = new GetRequest(index, type, id);

		GetResponse getResponse = null;
		try {
			getResponse = client.get(getRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElasticsearchException e) {
			// 如果是索引不存在
			if (e.status() == RestStatus.NOT_FOUND) {
				System.out.println("该索引库不存在！"+index);
			}

		}
		// 如果存在该数据则返回对应的结果
		if (getResponse.isExists()) {
			long version = getResponse.getVersion();
			String sourceAsString = getResponse.getSourceAsString();
			Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
			byte[] sourceAsBytes = getResponse.getSourceAsBytes();
			System.out.println("查询返回结果String:"+sourceAsString);
			System.out.println("查询返回结果Map:"+sourceAsMap);
		} else {
			System.out.println("没有找到该数据！");
		}
	}
	
	
	/**
	 *    是否存在
	 * @throws IOException 
	 */
	private static void exists() throws IOException {
		String index = "user";
		String type = "userindex";
		// 唯一编号
		String id = "1";
		// 创建查询请求
		GetRequest getRequest = new GetRequest(index, type, id);
		
		boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
		

		ActionListener<Boolean> listener = new ActionListener<Boolean>() {
		    @Override
		    public void onResponse(Boolean exists) {
		    	System.out.println("=="+exists);
		    }

		    @Override
		    public void onFailure(Exception e) {
		        System.out.println("失败的原因："+e.getMessage());
		    }
		};
		//进行异步监听
//		client.existsAsync(getRequest, RequestOptions.DEFAULT, listener);
		
		System.out.println("是否存在："+exists);
	}
	
	
	/**
	 *    更新操作
	 * @throws IOException
	 */
	private static void update() throws IOException {
		String index = "user";
		String type = "userindex";
		// 唯一编号
		String id = "1";
		UpdateRequest upateRequest=new UpdateRequest();
		upateRequest.id(id);
		upateRequest.index(index);
		upateRequest.type(type);
		
		//依旧可以使用Map这种集合作为更新条件
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("user", "xuwujing");
		jsonMap.put("postDate", "2019-03-11");
		
		upateRequest.doc(jsonMap);
		
		//
		upateRequest.docAsUpsert(true);
		// upsert 方法表示如果数据不存在，那么就新增一条
		upateRequest.upsert(jsonMap);
		
		client.update(upateRequest, RequestOptions.DEFAULT);
		
		
	}
	
	/**
	 * 删除
	 * @throws IOException 
	 * 
	 */
	private static void delete() throws IOException {

		String index = "user";
		String type = "userindex";
		// 唯一编号
		String id = "1";
		DeleteRequest deleteRequest=new DeleteRequest();
		deleteRequest.id(id);
		deleteRequest.index(index);
		deleteRequest.type(type);
		
		//设置超时时间
		deleteRequest.timeout(TimeValue.timeValueMinutes(2)); 
		//设置刷新策略"wait_for" 
		//保持此请求打开，直到刷新使此请求的内容可以搜索为止。此刷新策略与高索引和搜索吞吐量兼容，但它会导致请求等待响应，直到发生刷新
		deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
		
		//同步删除
		DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
		
		/*
		 *    异步删除操作
		 */
		
		//进行监听
		ActionListener<DeleteResponse> listener = new ActionListener<DeleteResponse>() {
		    @Override
		    public void onResponse(DeleteResponse deleteResponse) {
		        System.out.println("响应:"+deleteResponse);
		    }

		    @Override
		    public void onFailure(Exception e) {
		        System.out.println("异常:"+e.getMessage());
		    }
		};
		
		//异步删除
		 client.deleteAsync(deleteRequest, RequestOptions.DEFAULT, listener);
		
		ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
		//如果处理成功碎片的数量少于总碎片的情况,说明还在处理或者处理发生异常 
		if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
		    System.out.println("需要处理的碎片总量:"+shardInfo.getTotal());
		    System.out.println("处理成功的碎片总量:"+shardInfo.getSuccessful());
		}
		
		if (shardInfo.getFailed() > 0) {
		    for (ReplicationResponse.ShardInfo.Failure failure :
		            shardInfo.getFailures()) {
		        String reason = failure.reason(); 
		    }
		}
		
	}
	
}
