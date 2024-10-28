package com.zans.job.service.impl;

import com.zans.base.config.BaseConstants;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.job.model.OpsJobTask;
import com.zans.job.service.IRemoteService;
import com.zans.job.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static com.zans.job.config.JobConstants.SLEEP_DEFAULT_SECONDS;

/**
 * @author xv
 * @since 2020/5/7 18:47
 */
@Slf4j
@Service
public class RemoteServiceImpl implements IRemoteService {

    @Autowired
    ITaskService taskService;

    @Override
    public boolean checkNodeAlive(String ip, Integer port, int repeat) {
        if (repeat <= 0) {
            repeat = 1;
        }

        String url = String.format("http://%s:%d/node/alive", ip, port);
        log.info("节点检测#{}", url);
        int loop = repeat;
        boolean done = false;
        while(loop > 0) {
            ApiResult result = getForObject(url);
            if (accessSuccess(result)) {
                done = true;
                break;
            }
            log.info("节点检测#{}, 等待5s", url);
            sleep(SLEEP_DEFAULT_SECONDS);
            loop--;
        }
        return done;
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception ex) {
            log.error("sleep error", ex);
        }
    }

    @Override
    public boolean sendNewTask(String ip, Integer port, Long taskId, int repeat) {
        if (repeat <= 0) {
            repeat = 1;
        }

        String url = String.format("http://%s:%d/task/receive", ip, port);
        log.info("新任务下发[{}]#{}", taskId, url);
        Map<String, Object> form = MapBuilder.getSimpleMap("task_id", taskId);
        int loop = repeat;
        boolean done = false;
        while(loop > 0) {
            ApiResult result = postForObject(url, form);
            if (accessSuccess(result)) {
                done = true;
                break;
            }
            log.info("新任务下发#{}, 等待5s", url);
            sleep(SLEEP_DEFAULT_SECONDS);
            loop--;
        }
        return done;
    }

    @Override
    public boolean doSharding(String ip, Integer port, Long executionId, int repeat) {
        if (repeat <= 0) {
            repeat = 1;
        }

        String url = String.format("http://%s:%d/execution/sharding", ip, port);
        Map<String, Object> form = MapBuilder.getSimpleMap("id", executionId);
        log.info("任务分片[{}]#{}", executionId, url);
        int loop = repeat;
        boolean done = false;
        while(loop > 0) {
            ApiResult result = postForObject(url, form);
            if (accessSuccess(result)) {
                done = true;
                break;
            }
            taskService.deleteTasksOfExecution(executionId);
            log.info("任务分片#{}, 等待5s", url);
            sleep(SLEEP_DEFAULT_SECONDS);
            loop--;
        }
        return done;
    }

    private RestTemplate getDefaultRestTemplate() {
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
        return restTemplate;
    }

    private ApiResult getForObject(String url) {
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

    private ApiResult getForObject(String url, int timeout) {
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

    private ApiResult getForObject(String url, Map<String, ?> uriVariables) {
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

    private ApiResult postForObject(String url, Map<String, Object> postMap) {
        RestTemplate restTemplate = getDefaultRestTemplate();
        ApiResult result = null;
        try {
            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            if (postMap != null && postMap.size() > 0) {
                for (String key: postMap.keySet()) {
                    Object val = postMap.get(key);
                    String strVal = (val == null) ? null : val.toString();
                    map.add(key, strVal);
                }
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            result = restTemplate.postForObject(url, httpEntity, ApiResult.class);
        } catch (Exception ex) {
            log.error("getForObject error#" + url, ex);
        }
        restTemplate = null;
        return result;
    }

    private boolean accessSuccess(ApiResult result) {
        return result != null && result.getCode() == BaseConstants.CODE_SUCCESS;
    }

    @Override
    public boolean test(String ip, Integer port, int timeout) {
        String url = String.format("http://%s:%d/node/test", ip, port);
        log.info("api test begin");
        long t1 = System.currentTimeMillis();
        ApiResult result = getForObject(url, timeout);
        long cost = (System.currentTimeMillis() - t1)/1000;
        log.info("api test end, cost#{}", cost);
        if (accessSuccess(result)) {
           return true;
        }
        return false;
    }
}
