package com.pancm.test.esTest;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.cluster.storedscripts.GetStoredScriptRequest;
import org.elasticsearch.action.admin.cluster.storedscripts.GetStoredScriptResponse;
import org.elasticsearch.action.admin.cluster.storedscripts.PutStoredScriptRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: ES脚本编写相关代码
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/9/5
 */
public class EsScriptSearchTest {

    private static String elasticIp = "192.169.2.98";
    private static int elasticPort = 9200;
    private static Logger logger = LoggerFactory.getLogger(EsHighLevelRestSearchTest.class);

    private static RestHighLevelClient client = null;

    /**
     * @param args
     */
    public static void main(String[] args) {

        try {
            init();
            search();
            putApi();
            scriptSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close();
        }

    }


    /**
     * @Author pancm
     * @Description  通过脚本进行查询
     * @Date  2019/9/6
     * @Param []
     * @return void
     **/
    private static void scriptSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("p_test");
        searchRequest.types("_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


        /**
         *
         *{
         *     "settings" : {
         *         "number_of_shards" : 10,
         *          "refresh_interval" : "1s"
         *     },
         *     "mappings" : {
         *         "_doc" : {
         *             "properties" : {
         *                 "uid" : { "type" : "long" },
         *                 "phone" : { "type" : "long" },
         *                 "userid" : { "type" : "keyword" },
         *                 "sendday" : { "type" : "long" },
         *                 "message" : { "type" : "keyword" },
         *                 "msgcode" : { "type" : "long" },
         *                 "price" : { "type" : "double","index": "false" },
         *              "sendtime" : {
         *                   "type" : "date",
         *                   "format" : "yyyy-MM-dd HH:mm:ss.SSS"
         *               },
         *              "sendtime2" : {
         *                   "type" : "date",
         *                   "format" : "yyyy-MM-dd HH:mm:ss.SSS"
         *               },
         *                 "sendtm" : { "type" : "long" },
         *                   "sendtm2" : { "type" : "long" }
         *             }
         *         }
         *     }
         * }
         *
         *
         *
         *  对应查询语句
         *  SELECT userid,sendday, (sendtm2-sendtm)as t FROM p_test where phone=12345678919 and (sendtm2-sendtm)>3801
         **/

        String id="InternalSqlScriptUtils.nullSafeFilter(InternalSqlScriptUtils.gt(InternalSqlScriptUtils.sub(InternalSqlScriptUtils.docValue(doc,params.v0)," +
                "InternalSqlScriptUtils.docValue(doc,params.v1)),params.v2))";
        String lang="painless";
        Map<String,Object> map = new HashMap<>();
        map.put("v0","sendtm2");
        map.put("v1","sendtm");
        map.put("v2",3800);
        Script script = new Script(ScriptType.INLINE,lang,id,map);

//		searchSourceBuilder.scriptField("script",script);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.termQuery("phone", "12345678919"));
        boolQueryBuilder.must(QueryBuilders.scriptQuery(script));


        //设置查询条件
        searchSourceBuilder.query(boolQueryBuilder);

        searchSourceBuilder.storedField("_none_");
        searchSourceBuilder.docValueField("userid","use_field_mapping");
        searchSourceBuilder.docValueField("sendday","use_field_mapping");
        searchSourceBuilder.docValueField("sendtm2","use_field_mapping");
        searchSourceBuilder.docValueField("sendtm","use_field_mapping");
        System.out.println("查询语句:"+searchSourceBuilder.toString());
        searchRequest.source(searchSourceBuilder);

        // 同步查询
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        searchResponse.getHits().forEach(hit -> {
            Map<String, DocumentField> map1 =  hit.getFields();
            Map<String,Object> map2 = new HashMap<>();
            System.out.println("\n查询的Map结果:" + map1);

            map1.forEach((s, objects) -> {
                map2.put(objects.getName(),objects.getValue());
            });

            System.out.println("\n查询的Map结果2:" + map2);


        });


    }


    private static void putApi() throws IOException {
        PutStoredScriptRequest request = new PutStoredScriptRequest();
        request.id("calculate-score");
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("script");
            {
                builder.field("lang", "mustache");
                builder.field("source", "{\"query\":{\"match\":{\"title\":\"{{query_string}}\"}}}");
            }
            builder.endObject();
        }
        builder.endObject();
        request.content(BytesReference.bytes(builder), XContentType.JSON);

        AcknowledgedResponse putStoredScriptResponse = client.putScript(request, RequestOptions.DEFAULT);

        System.out.println("=="+putStoredScriptResponse.isAcknowledged());
        System.out.println("=="+putStoredScriptResponse.isFragment());

    }

    /**
     * @Author pancm
     * @Description 通过脚本进行查询
     * @Date  2019/9/5
     * @Param []
     * @return void
     **/
    private static void search() throws IOException {
        GetStoredScriptRequest request = new GetStoredScriptRequest("calculate-score");
        GetStoredScriptResponse getResponse = client.getScript(request, RequestOptions.DEFAULT);
        System.out.println("=="+getResponse.getSource().getLang());
        System.out.println("=="+getResponse.getSource().getSource());
        System.out.println("=="+getResponse.getSource().getOptions());
        System.out.println("=="+getResponse.getSource().get().getOptions());

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
