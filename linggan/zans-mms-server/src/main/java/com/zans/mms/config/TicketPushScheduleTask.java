package com.zans.mms.config;



import com.zans.mms.dao.SysConstantItemMapper;
import com.zans.mms.service.ITicketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class TicketPushScheduleTask implements SchedulingConfigurer {

    @Autowired
    SysConstantItemMapper sysConstantItemMapper;

    @Autowired
    ITicketService ticketService;


    @Value("${ticket.task:0}")
    private String ticketTaskFlag;



    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        if ("1".equals(ticketTaskFlag)) {
            log.info("工单推送开始");
            scheduledTaskRegistrar.addTriggerTask(
                    //1.添加任务内容(Runnable)
                    () -> ticketService.pushTicketMessageTask(),
                    //2.设置执行周期(Trigger)
                    triggerContext -> {
                        //2.1 从数据库获取执行周期
                        String cron = sysConstantItemMapper.getConstantValueByKey("ticket");
                        //2.2 合法性校验.
                        if (StringUtils.isEmpty(cron)) {
                            // Omitted Code ..
                            cron = "0 0 17 * * ?";
                        }
                        //2.3 返回执行周期(Date)
                        return new CronTrigger(cron).nextExecutionTime(triggerContext);
                    }
            );
            log.info("工单推送结束");
        }else {
            log.info("工单推送未开启！");
        }

    }




}
