package com.pancm.test.httpTest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Author pancm
 * @Description https请求调用
 * @Date  2023/6/9
 * @Param
 * @return
 **/
public class HttpsUtils {

    public static String sendGetRequest(String urlStr) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(urlStr);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }
    }

    public static void main(String[] args) throws IOException {
        String response = HttpsUtils.sendGetRequest("https://jsonplaceholder.typicode.com/posts/1");
        System.out.println(response);
    }

}
