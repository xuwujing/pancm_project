package com.zans.netty;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Iterator;
import java.util.Map;

/**
 * 清理不活跃的Channel
 */
@Configuration
@EnableScheduling
@Slf4j
public class ChannelScheduleTask {

    // 定时删除不活跃的连接
    @Scheduled(cron = "0/10 * * * * ?")
    private void configureTasks() {
        if (NettyChannelMap.getChannelHashMap() != null && NettyChannelMap.getChannelHashMap().size() > 0) {
            Iterator<Map.Entry<String, Channel>> iterator = NettyChannelMap.getChannelHashMap().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Channel> next = iterator.next();
                String key = next.getKey();
                Channel channel = next.getValue();
                if (!channel.isActive()) {
                    NettyChannelMap.removeChannelByName(key);
                    log.warn("name:{},不活跃,进行清理!",key);
                }
            }
        }
    }

}
