package com.zans.task;

import com.zans.commons.utils.GetSpringBeanUtil;
import com.zans.contants.AlertConstants;
import com.zans.service.IAlertAggService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警记录聚合线程
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/20
 */
@Slf4j
public class AlertAggTask extends Thread {


    private long count;
    private IAlertAggService service;

    public AlertAggTask() {
        service = GetSpringBeanUtil.getBean(IAlertAggService.class);
    }

    public void run() {
        while (true) {
            try {
                service.aggRecord();
                count++;
                if(count%600==0){
                    log.info("告警记录聚合线程休眠！");
                }
                TimeUnit.SECONDS.sleep(AlertConstants.SLEEP_TIME_AGG);
            } catch (Exception e) {
                log.error("告警记录聚合线程运行失败！原因是:", e);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e1) {
                    log.error("告警记录聚合暂停失败！", e1);
                }
                continue;
            }
        }
    }


}
