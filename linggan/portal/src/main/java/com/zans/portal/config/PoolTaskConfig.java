package com.zans.portal.config;

import com.alibaba.fastjson.JSON;
import com.zans.base.vo.ApiResult;
import com.zans.portal.util.RestTemplateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @Title: PoolTask
 * @Description: 自定义线程池
 * @Version:1.0.0
 * @author pancm
 * @date 2020年9月21日
 */
@EnableAsync
@Configuration
@Slf4j
public class PoolTaskConfig {

	@Autowired
	RestTemplateHelper restTemplateHelper;

	@Bean("taskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//核心线程数、最大线程数、任务队列数
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(200);
		executor.setKeepAliveSeconds(60);
		executor.setThreadNamePrefix("taskExecutor-");
		//设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
		executor.setWaitForTasksToCompleteOnShutdown(true);
		//设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
		executor.setAwaitTerminationSeconds(60);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}


	@Async("taskExecutor")
	public ApiResult executeRadApiRequest(String host, String uri) {
		ApiResult result = null;
		try {
			String serverUrl = host + uri;
			log.info("reqUrl:{}",serverUrl);
			result = restTemplateHelper.getForObject(serverUrl,  60 * 1000);
			log.info("reqResult:{}",result);
			return result;
		} catch (Exception e) {
			log.error("解析远程数据异常#",e);
			return ApiResult.error("解析远程数据异常#" + JSON.toJSONString(result));
		}
	}

	@Async("taskExecutor")
	public ApiResult executeRadApiRequest2(String host, String uri,String host2,String uri2) {
		ApiResult result = null;
		try {
			String serverUrl = host + uri;
			log.info("reqUrl1:{}",serverUrl);
			result = restTemplateHelper.getForObject(serverUrl,  60 * 1000);
			log.info("reqResult1:{}",result);
			String serverUrl2 = host2 + uri2;
			log.info("reqUrl2:{}",serverUrl2);
			result = restTemplateHelper.getForObject(serverUrl2,  60 * 1000);
			log.info("reqResult2:{}",result);

			return result;
		} catch (Exception e) {
			log.error("解析远程数据异常#",e);
			return ApiResult.error("解析远程数据异常#" + JSON.toJSONString(result));
		}
	}

	@Async("taskExecutor")
	public ApiResult executeAlertApiRequest(String host, String uri, String jsonStr) {
		ApiResult result = null;
		try {
			String serverUrl = host + uri;
			log.info("reqUrl:{}  param:{}",serverUrl,jsonStr);
			result = restTemplateHelper.postForJsonString(serverUrl,  jsonStr);
			log.info("executeAlertApiRequest reqResult:{}",result);
			return result;
		} catch (Exception e) {
			log.error("解析远程数据异常#",e);
			return ApiResult.error("解析远程数据异常#" + JSON.toJSONString(result));
		}
	}

}
