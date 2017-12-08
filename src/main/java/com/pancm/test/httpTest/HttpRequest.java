package com.pancm.test.httpTest;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * @author ZERO
 * @Data 2017-5-12 下午4:04:18
 * @Description 
 */

public class HttpRequest {
	public static int HTTP_TIME_OUT = 600000;// 超时时间600秒
	private static final String DEFAULT_CHARSET = "UTF-8";
	public static String doJSONPost(String url, String json) {	
        CloseableHttpClient httpclient = null;
        try {
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            RequestConfig requestConfig = null;
            // 设置http的状态参数
            requestConfig = RequestConfig.custom().setSocketTimeout(HTTP_TIME_OUT).setConnectTimeout(HTTP_TIME_OUT)
                    .setConnectionRequestTimeout(HTTP_TIME_OUT).build();

            // 添加HTTP POST参数
            StringEntity input = new StringEntity(json, "UTF-8");
            input.setContentType("application/json");

            HttpUriRequest reqMethod = RequestBuilder.post().setUri(url).setEntity(input).setConfig(requestConfig)
                    .build();

            // 创建响应处理器处理服务器响应内容
            ResponseHandler<String> responseHandler = new ResponseHandler() {
                // 自定义响应处理
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        ContentType contentType = ContentType.get(entity);
                        Charset charset = (null == contentType ? Charset.forName(DEFAULT_CHARSET)
                                : (null == contentType.getCharset() ? Charset.forName(DEFAULT_CHARSET)
                                : contentType.getCharset()));
                        return new String(EntityUtils.toByteArray(entity), charset);
                    } else {
                        return null;
                    }
                }
            };

            // // 执行请求并获取结果
            String responseBody = httpclient.execute(reqMethod, responseHandler);

            return responseBody;
        } catch (Exception ex) {
//            throw new HttpPostException("doxmlpost_error", ex.getMessage(), ex);
        	ex.printStackTrace();
        	throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
            }
        }
    }
}
