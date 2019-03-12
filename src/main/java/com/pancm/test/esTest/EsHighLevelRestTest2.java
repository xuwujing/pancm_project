package com.pancm.test.esTest;

import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.index.reindex.ScrollableHitSource;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @Title: EsHighLevelRestTest2
* @Description: Java High Level REST Client Es高级客户端使用教程二 (进阶使用)
* @since jdk 1.8
* @Version:1.0.0  
* @author pancm
* @date 2019年3月11日
*/
public class EsHighLevelRestTest2 {
	
	private static String elasticIp = "192.169.0.23";
	private static int elasticPort = 9200;

	private static Logger logger = LoggerFactory.getLogger(EsHighLevelRestTest2.class);
	
	private static RestHighLevelClient client = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			init();
			multiGet();
			reindex();
			
			
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
	 *  多查询使用
	 * @throws IOException 
	 */
	private static void multiGet() throws IOException {
		
		MultiGetRequest request = new MultiGetRequest();
		request.add(new MultiGetRequest.Item("estest", "estest", "1"));
		request.add(new MultiGetRequest.Item("user", "userindex", "2"));
		//禁用源检索，默认启用
//		request.add(new MultiGetRequest.Item("user", "userindex", "2").fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE));
		
		//同步构建
		MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
		
		//异步构建
//		MultiGetResponse response2 = client.mgetAsync(request, RequestOptions.DEFAULT, listener);
		
		/*
		 返回的MultiGetResponse包含在' getResponses中的MultiGetItemResponse的列表，其顺序与请求它们的顺序相同。
		 如果成功，MultiGetItemResponse包含GetResponse或MultiGetResponse。如果失败了就失败。成功看起来就像一个正常的GetResponse
		 */
		
		for(MultiGetItemResponse item:response.getResponses()) {
			assertNull(item.getFailure());              
			GetResponse get = item.getResponse();  
			String index = item.getIndex();
			String type = item.getType();
			String id = item.getId();
			//如果请求存在
			if (get.isExists()) {
				long version = get.getVersion();
				String sourceAsString = get.getSourceAsString();        
				Map<String, Object> sourceAsMap = get.getSourceAsMap(); 
				byte[] sourceAsBytes = get.getSourceAsBytes();
				System.out.println("查询的结果:"+sourceAsMap);
			} else {
				System.out.println("没有找到该文档!");
			}
			
		}
		
	}
	
	
	/**
	 *  索引复制
	 * @throws IOException 
	 */
	private static void reindex() throws IOException {
		//创建索引复制请求并进行索引复制
		ReindexRequest request = new ReindexRequest(); 
		//需要复制的索引
		request.setSourceIndices("user"); 
		// 复制的目标索引
		request.setDestIndex("dest_test");  
		
		//表示如果在复制索引的时候有缺失的文档的话会进行创建,默认是index
		request.setDestOpType("create");
		//如果在复制的过程中发现版本冲突，那么会继续进行复制
		request.setConflicts("proceed");
		
		//只复制文档类型为 userindex 的数据
		request.setSourceDocTypes("userindex");
		//只复制 pancm 用户的数据
		request.setSourceQuery(new TermQueryBuilder("user", "pancm")); 
		//设置复制文档的数量
		request.setSize(10);
		//设置一次批量处理的条数，默认是1000
		request.setSourceBatchSize(100);
		
		//进行排序
//		request.addSortField("postDate", SortOrder.DESC);
		
		//指定切片大小
		request.setSlices(2);
		
		//同步执行
		BulkByScrollResponse bulkResponse = client.reindex(request, RequestOptions.DEFAULT);
		
		//异步执行
//		client.reindexAsync(request, RequestOptions.DEFAULT, listener); 

		
		//响应结果处理
		
		TimeValue timeTaken = bulkResponse.getTook(); 
		boolean timedOut = bulkResponse.isTimedOut(); 
		long totalDocs = bulkResponse.getTotal(); 
		long updatedDocs = bulkResponse.getUpdated(); 
		long createdDocs = bulkResponse.getCreated(); 
		long deletedDocs = bulkResponse.getDeleted(); 
		long batches = bulkResponse.getBatches(); 
		long noops = bulkResponse.getNoops(); 
		long versionConflicts = bulkResponse.getVersionConflicts(); 
		long bulkRetries = bulkResponse.getBulkRetries(); 
		long searchRetries = bulkResponse.getSearchRetries(); 
		TimeValue throttledMillis = bulkResponse.getStatus().getThrottled(); 
		TimeValue throttledUntilMillis = bulkResponse.getStatus().getThrottledUntil(); 
		List<ScrollableHitSource.SearchFailure> searchFailures = bulkResponse.getSearchFailures(); 
		List<BulkItemResponse.Failure> bulkFailures = bulkResponse.getBulkFailures(); 
		
		System.out.println("总共花费了:"+timeTaken.getMillis()+" 毫秒，总条数:"+totalDocs+",创建数:"+createdDocs+",更新数:"+updatedDocs);
	}
	
}
