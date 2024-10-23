package com.zans.task;

import com.zans.commons.utils.GetSpringBeanUtil;
import com.zans.service.IRuleService;
import lombok.extern.slf4j.Slf4j;

import static com.zans.contants.AlertConstants.alertServerCache;

/**
 * @Author pancm
 * @Description 告警记录恢复处理线程
 * @Date 2020/10/22
 * @Param
 * @return
 **/
@Slf4j
public class AlertRecoverTask implements Runnable {

    private long ruleId;

    private long defaultServer = 2;


    private IRuleService iRuleService;


    public AlertRecoverTask(Long ruleId) {
        this.ruleId = ruleId;
        iRuleService = GetSpringBeanUtil.getBean(IRuleService.class);
    }

    @Override
    public void run() {
        log.info("ruleId:{} 的告警记录恢复线程启动！", ruleId);
        iRuleService.recover(alertServerCache.get(defaultServer),ruleId);
        log.info("ruleId:{} 的告警记录恢复处理线程任务执行完毕！", ruleId);
    }


}
