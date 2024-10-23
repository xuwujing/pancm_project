package com.zans.task;


import com.zans.commons.config.GetProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zans.contants.AlertConstants.*;


/**
 * @author pancm
 * @Title: RefreshConfigTask
 * @Description: 刷新配置线程
 * @Version:1.0.0
 * @date 2018年1月30日
 */
@Slf4j
public class RefreshConfigTask {

    public void startTask() {
        try {
            Runnable runnable = () -> {
                GetProperties.refresh();
                SLEEP_TIME_RECOVER = Long.valueOf(GetProperties.getAppSettings().getOrDefault("recover.time","60"));
                SLEEP_TIME_AGG =  Long.valueOf(GetProperties.getAppSettings().getOrDefault("agg.time","60"));
                SLEEP_TIME_QUEUE =  Long.valueOf(GetProperties.getAppSettings().getOrDefault("pull.queue.time","60"));
                log.debug("配置刷新成功！");
            };
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(runnable, 0, 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("配置刷新失败！原因是:",e);
        }
    }
}
