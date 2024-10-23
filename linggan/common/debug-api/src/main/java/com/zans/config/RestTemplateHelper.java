package com.zans.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zans.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RestTemplateHelper {

    public RestTemplate getDefaultRestTemplate() {
        return getDefaultRestTemplate(60, 60);
    }

    private RestTemplate getDefaultRestTemplate(int timeout) {
        return getDefaultRestTemplate(timeout, timeout);
    }

    private RestTemplate getDefaultRestTemplate(int connectTimeoutSeconds, int readTimeoutSeconds) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeoutSeconds * 1000);
        requestFactory.setReadTimeout(readTimeoutSeconds * 1000);

//        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        httpRequestFactory.setConnectionRequestTimeout(connectTimeoutSeconds * 1000);
//        httpRequestFactory.setConnectTimeout(readTimeoutSeconds * 1000);
//        httpRequestFactory.setReadTimeout(readTimeoutSeconds * 1000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        //换上fastjson
        List<HttpMessageConverter<?>> httpMessageConverterList = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = httpMessageConverterList.iterator();
        if (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            //原有的String是ISO-8859-1编码 去掉
            if (converter instanceof StringHttpMessageConverter) {
                iterator.remove();
            }

            //由于系统中默认有jackson 在转换json时自动会启用  但是我们不想使用它 可以直接移除或者将fastjson放在首位
            if (converter instanceof GsonHttpMessageConverter || converter instanceof MappingJackson2HttpMessageConverter) {
                iterator.remove();
            }


        }
        httpMessageConverterList.add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect);
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastMediaTypes.add(MediaType.TEXT_HTML);
        fastMediaTypes.add(MediaType.TEXT_PLAIN);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        httpMessageConverterList.add(0, fastJsonHttpMessageConverter);

        return restTemplate;
    }

    public ApiResult getForObject(String url) {
        RestTemplate restTemplate = getDefaultRestTemplate();
        ApiResult result = null;
        try {
            result = restTemplate.getForObject(url, ApiResult.class);
        } catch (Exception ex) {
            log.error("getForObject error#" + url, ex);
        }
        restTemplate = null;
        return result;
    }

    public String getForStr(String url) {
        RestTemplate restTemplate = getDefaultRestTemplate(5);
        String result = null;
        try {
            result = restTemplate.getForObject(url, String.class);
        } catch (Exception ex) {
            log.error("getForObject error#" + url, ex);
        }
        restTemplate = null;
        return result;
    }


    public ApiResult getForObject(String url, int timeout) {
        RestTemplate restTemplate = getDefaultRestTemplate(timeout);
        ApiResult result = null;
        try {
            result = restTemplate.getForObject(url, ApiResult.class);
        } catch (Exception ex) {
            log.error("getForObject error#" + url, ex);
        }
        restTemplate = null;
        return result;
    }

    public ApiResult getForObject(String url, Map<String, ?> uriVariables) {
        RestTemplate restTemplate = getDefaultRestTemplate();
        ApiResult result = null;
        try {
            result = restTemplate.getForObject(url, ApiResult.class, uriVariables);
        } catch (Exception ex) {
            log.error("getForObject error#" + url, ex);
        }
        restTemplate = null;
        return result;
    }

    public ApiResult postForObject(String url, Map<String, Object> postMap) {
        RestTemplate restTemplate = getDefaultRestTemplate();
        ApiResult result = null;

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        if (postMap != null && postMap.size() > 0) {
            for (String key : postMap.keySet()) {
                Object val = postMap.get(key);
                String strVal = (val == null) ? null : val.toString();
                map.add(key, strVal);
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        result = restTemplate.postForObject(url, httpEntity, ApiResult.class);
        restTemplate = null;
        return result;
    }

    public static ApiResult sendPostRequest(String url, MultiValueMap<String, String> params, HttpMethod method) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity<ApiResult> response = client.exchange(url, method, requestEntity, ApiResult.class);
        return response.getBody();
    }


    public ApiResult postForJsonString(String url, String jsonString) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ApiResult result = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonString, headers);
        ResponseEntity<ApiResult> responseEntity = restTemplate.postForEntity(url, httpEntity, ApiResult.class);
        result = responseEntity.getBody();

        restTemplate = null;
        return result;
    }

}
