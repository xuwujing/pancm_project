package com.zans.mms.config;

import com.zans.base.util.HttpClientUtil;
import com.zans.base.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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


	@Bean("taskExecutor2")
	public Executor taskExecutor2() {
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
	public ApiResult executeRadApiRequest(String url, String data) {
		try {
			HttpClientUtil.post(url, data);
			return ApiResult.success();
		} catch (Exception e) {
			log.error("解析远程数据异常!请求url:{},请求参数:{} ",url,data,e);
			return ApiResult.error("解析远程数据异常#" + data);
		}
	}
}
