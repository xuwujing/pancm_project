package com.zans.task;

import com.zans.commons.utils.GetSpringBeanUtil;
import com.zans.contants.AlertConstants;
import com.zans.service.IAlertMsgService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警内容推送线程
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/20
 */
@Slf4j
public class AlertMessageTask extends Thread {



    private long count;
    private IAlertMsgService iAlertMsgService;

    public AlertMessageTask() {
        iAlertMsgService = GetSpringBeanUtil.getBean(IAlertMsgService.class);
    }

    public void run() {
        while (true) {
            try {
                iAlertMsgService.deal();
                count++;
                if(count%600==0){
                    log.info("推送线程休眠！");
                }
                TimeUnit.SECONDS.sleep(AlertConstants.SLEEP_TIME_QUEUE);
            } catch (Exception e) {
                log.error("加载推送线程运行失败！原因是:", e);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e1) {
                    log.error("线程暂停失败！", e1);
                }
                continue;
            }
        }
    }


}
