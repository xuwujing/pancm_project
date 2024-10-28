package com.zans.mms.config;



import com.zans.mms.dao.SysConstantItemMapper;
import com.zans.mms.service.IPoManagerService;
import com.zans.mms.service.ITicketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

/**
 * 舆情从远端拉数据定时任务
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class PoManagerScheduleTask implements SchedulingConfigurer {

    @Autowired
    SysConstantItemMapper sysConstantItemMapper;

    @Autowired
    IPoManagerService poManagerService;


    @Value("${poManager.task:0}")
    private String poManagerTaskFlag;



    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        if ("1".equals(poManagerTaskFlag)) {
            log.info("民意平台数据获取开始");
            scheduledTaskRegistrar.addTriggerTask(
                    //1.添加任务内容(Runnable)
                    () -> poManagerService.getDataFormRemoteTask(),
                    //2.设置执行周期(Trigger)
                    triggerContext -> {
                        //2.1 从数据库获取执行周期
                        String cron = sysConstantItemMapper.getConstantValueByKey("poManager");
                        //2.2 合法性校验.
                        if (StringUtils.isEmpty(cron)) {
                            // Omitted Code .. 默认设置5分钟
                            cron = "0 */5 * * * ?";
                        }
                        //2.3 返回执行周期(Date)
                        return new CronTrigger(cron).nextExecutionTime(triggerContext);
                    }
            );
            log.info("民意平台数据获取结束");
        }else {
            log.info("民意平台数据获取未启用！");
        }

    }




}
