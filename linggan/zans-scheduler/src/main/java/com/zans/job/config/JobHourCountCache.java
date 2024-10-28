package com.zans.job.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zans.job.vo.common.CircleUnit;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author pancm
 * @Title: job
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/21
 */
@Component
public class JobHourCountCache {

    Cache<String, CircleUnit> jobHourCache;

    @PostConstruct
    private void init() {
        jobHourCache = Caffeine.newBuilder()
                .expireAfterWrite(25, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build();
    }

    public CircleUnit getJobHour(String hour) {
        Object value = this.jobHourCache.getIfPresent(hour);
        if (value != null) {
            return (CircleUnit) value;
        }
        return null;
    }

    public void putJobHour(String hour, CircleUnit circleUnit) {
        this.jobHourCache.put(hour, circleUnit);
    }

}
