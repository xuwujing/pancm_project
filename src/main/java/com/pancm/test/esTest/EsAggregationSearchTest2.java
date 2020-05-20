package com.pancm.test.esTest;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
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
            test();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close();
        }

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
