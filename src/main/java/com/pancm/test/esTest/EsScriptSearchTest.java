package com.pancm.test.esTest;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.cluster.storedscripts.GetStoredScriptRequest;
import org.elasticsearch.action.admin.cluster.storedscripts.GetStoredScriptResponse;
import org.elasticsearch.action.admin.cluster.storedscripts.PutStoredScriptRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
//            search();
            putApi();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close();
        }

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
