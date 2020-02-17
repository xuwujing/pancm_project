package com.pancm.test.esTest;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: ES分词查询处理
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/10/12
 */
public class EsParticipleSearchTest {

    private static String elasticIp = "192.169.0.23";
    private static int elasticPort = 9200;
    private static Logger logger = LoggerFactory.getLogger(EsHighLevelRestSearchTest.class);

    private static RestHighLevelClient client = null;


    public static void main(String[] args) {
        try {
            init();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
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
