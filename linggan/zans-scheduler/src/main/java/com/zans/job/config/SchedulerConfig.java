package com.zans.job.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author xv
 * @since 2020/5/6 15:33
 */
@Configuration
@Slf4j
public class SchedulerConfig implements SchedulerFactoryBeanCustomizer {

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setStartupDelay(30);
        log.info("quartz init....");
        log.info("quartz config#{}", schedulerFactoryBean);
    }
}
