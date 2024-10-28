package com.zans.mms.config;



import com.zans.mms.dao.SysConstantItemMapper;
import com.zans.mms.service.IPoManagerService;
import com.zans.mms.service.IRankingService;
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
 * 绩效考核数据填入定时任务
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class RankingScheduleTask implements SchedulingConfigurer {

    @Autowired
    SysConstantItemMapper sysConstantItemMapper;

    @Autowired
    IRankingService rankingService;

    @Value("${ranking.task:0}")
    private String rankingTaskFlag;




    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        if ("1".equals(rankingTaskFlag)) {
            log.info("绩效考核数据自动生成定时任务已启动");
            scheduledTaskRegistrar.addTriggerTask(
                    //1.添加任务内容(Runnable)
                    () -> rankingService.generateRankingData(),
                    //2.设置执行周期(Trigger)
                    triggerContext -> {
                        //2.1 从数据库获取执行周期
                        String cron = sysConstantItemMapper.getConstantValueByKey("ranking");
                        //2.2 合法性校验.
                        if (StringUtils.isEmpty(cron)) {
                            // Omitted Code .. 默认设置每月1号
                            cron = "0 01 00 01 * ?";
                        }
                        //2.3 返回执行周期(Date)
                        return new CronTrigger(cron).nextExecutionTime(triggerContext);
                    }
            );
        }
    }




}
