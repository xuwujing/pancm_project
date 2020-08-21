package com.pancm.test.esTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pancm.test.httpTest.HttpClientUtil;
import com.pancm.util.MyTools;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.pipeline.bucketsort.BucketSortPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:ES聚合查询测试用例
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/4/2
 */
public class EsAggregationSearchTest2 {



//    private static String elasticIp = "192.169.0.23";
    private static String elasticIp = "192.169.2.98";
    private static int elasticPort = 9200;
    private static Logger logger = LoggerFactory.getLogger(EsAggregationSearchTest2.class);

    private static RestHighLevelClient client = null;

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            init();
//            test();
//            test2();
            getHttpData();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close();
        }

    }

    /**
     * @Author pancm
     * @Description 通过http请求ES并解析having聚合数据
     * @Date  2020/8/21
     * @Param
     * @return void
     **/
    private static void getHttpData() throws Exception {
        // group by 加 having的dsl语句
        // 对应的sql语句为 SELECT nas_ip_address,COUNT(1) AS c FROM radius_acct GROUP BY nas_ip_address HAVING c >1000 ;
        String dsl="{\"query\":{\"bool\":{\"must\":[{\"range\":{\"acct_start_time\":{\"from\":\"2020-08-01 13:25:55\",\"to\":null,\"include_lower\":true,\"include_upper\":true,\"boost\":1}}},{\"range\":{\"acct_start_time\":{\"from\":null,\"to\":\"2020-08-20 13:26:55\",\"include_lower\":true,\"include_upper\":true,\"boost\":1}}}],\"adjust_pure_negative\":true,\"boost\":1}},\"size\":0,\"version\":false,\"explain\":false,\"_source\":false,\"aggregations\":{\"groupby\":{\"terms\":{\"field\":\"nas_ip_address\",\"size\":2147483647,\"min_doc_count\":1,\"shard_min_doc_count\":0,\"show_term_doc_count_error\":false,\"order\":[{\"_count\":\"desc\"},{\"_key\":\"asc\"}]},\"aggregations\":{\"having\":{\"bucket_selector\":{\"buckets_path\":{\"groupCount\":\"_count\"},\"script\":{\"source\":\"params.groupCount >= 1000\",\"lang\":\"painless\"},\"gap_policy\":\"skip\"}}}}}}";
        String postUrl = "http://192.168.8.78:9200/radius_acct_log/_search";
        String postData =HttpClientUtil.post(postUrl,dsl);
        System.out.println(getAggData(postData));

    }

    private static JSONArray getAggData(String data){
        JSONObject jsonObject = MyTools.toJson(data).getJSONObject("aggregations");
        // 这个groupby对于dsl语句中的自定义字段的那个，建议写死
        JSONArray jsonArray = JSON.parseArray(jsonObject.getJSONObject("groupby").getString("buckets"));
        return  jsonArray;
    }



    private static void test2() throws IOException {
        AggregationBuilder aggregation = AggregationBuilders.filters("ecid", QueryBuilders.termQuery("id",1));
        SearchResponse searchResponse = search(aggregation);
        // 获取聚合结果
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String,Object> map =  new HashMap<>();
        List<Map<String,Object>> list = new ArrayList<>();
        agg(map,list,aggregations);
        logger.info("test2聚合查询结果:"+list);
        logger.info("------------------------------------");
    }


    private static  void test() throws IOException {
//        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("ecid").field("ecid").size(99999);
//        List<FieldSortBuilder> fieldSorts=new ArrayList<>();
//        fieldSorts.add(new FieldSortBuilder("sum_field").order(SortOrder.DESC));
//        termsBuilder.subAggregation(new BucketSortPipelineAggregationBuilder("bucket_field", fieldSorts).from(6000).size(10));
//

        AggregationBuilder aggregation = AggregationBuilders.terms("ecid").field("ecid");
        List<FieldSortBuilder> fieldSorts=new ArrayList<>();
        fieldSorts.add(new FieldSortBuilder("id_").order(SortOrder.DESC));

        aggregation.subAggregation(new BucketSortPipelineAggregationBuilder("id", fieldSorts).from(0).size(10));
        SearchResponse searchResponse = search(aggregation);
        // 获取聚合结果
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String,Object> map =  new HashMap<>();
        List<Map<String,Object>> list = new ArrayList<>();
        agg(map,list,aggregations);
        logger.info("聚合查询结果:"+list);
        logger.info("------------------------------------");
    }


    private static SearchResponse search(AggregationBuilder aggregation) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("mt_task_j20200401");
        searchRequest.types("mt_task_hh");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //不需要解释
        searchSourceBuilder.explain(false);
        //不需要原始数据
        searchSourceBuilder.fetchSource(false);
        //不需要版本号
        searchSourceBuilder.version(false);
        searchSourceBuilder.aggregation(aggregation);
        logger.info("查询的语句:"+searchSourceBuilder.toString());
        searchRequest.source(searchSourceBuilder);
        // 同步查询
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        return  searchResponse;
    }


    private static void agg(Map<String,Object> map, List<Map<String,Object>> list, Aggregations aggregations) {
        aggregations.forEach(aggregation -> {
            String name = aggregation.getName();
            Terms genders = aggregations.get(name);
            for (Terms.Bucket entry : genders.getBuckets()) {
                String key = entry.getKey().toString();
                long t = entry.getDocCount();
                map.put(name,key);
                map.put(name+"_"+"count",t);

                //判断里面是否还有嵌套的数据
                List<Aggregation> list2 = entry.getAggregations().asList();
                if (list2.isEmpty()) {
                    Map<String,Object> map2 = new HashMap<>();
                    BeanUtils.copyProperties(map,map2);
                    list.add(map2);
                }else{
                    agg(map, list, entry.getAggregations());
                }
            }
        });
    }

    /*
     * 初始化服务
     */
    private static void init() {
        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(elasticIp, elasticPort));
        client = new RestHighLevelClient(restClientBuilder);
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
            }finally{
                client=null;
            }
        }
    }



}
