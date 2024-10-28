package com.zans.job.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author xv
 * @since 2020/5/6 15:34
 */
@Slf4j
@Component
public class SchedulerHelper {

    @Autowired
    private Scheduler scheduler;

    /**
     * 方法描述: 创建定时任务 <br>
     * @param jobGroup JOB分组
     * @param jobName JOB名称
     * @param jobClass JOB实现类,作业逻辑
     * @param cronExpression CRON表达式
     * @param params 自定义参数,作业中使用
     * @throws SchedulerException
     */
    public void createScheduleJob(String jobGroup, String jobName, Class<? extends Job> jobClass, String cronExpression,
                                  Map<String, Object> params) throws SchedulerException {
        if (scheduler.checkExists(JobKey.jobKey(jobName, jobGroup))) {
            throw new SchedulerException(MessageFormat.format("JobGroup: {0}, JobName: {1} 已存在", jobGroup, jobName));
        }
        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder)
                .build();
        if (!CollectionUtils.isEmpty(params)) {
            // 放入参数,运行时的方法可以获取
            jobDetail.getJobDataMap().putAll(params);
        }
        Date date = scheduler.scheduleJob(jobDetail, trigger);
        log.info("创建定时任务成功: {}, JobGroup: {}, JobName: {}, JobClass: {}, CronExpression: {}", date, jobGroup, jobName,
                jobClass, cronExpression);
    }

    /**
     * 方法描述: 更新定时任务 <br>
     * @param jobGroup JOB分组
     * @param jobName JOB名称
     * @param cronExpression CRON表达式
     * @throws SchedulerException
     */
    public void updateScheduleJob(String jobGroup, String jobName, String cronExpression) throws SchedulerException {
        if (!scheduler.checkExists(JobKey.jobKey(jobName, jobGroup))) {
            throw new SchedulerException(MessageFormat.format("JobGroup: {0}, JobName: {1} 不存在", jobGroup, jobName));
        }
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (trigger != null) {
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            // 忽略状态为暂停(PAUSED)的任务,解决集群环境中在其他机器设置定时任务为PAUSED状态后,集群环境启动另一台主机时定时任务全被唤醒的BUG
            if (!triggerState.name().equalsIgnoreCase("PAUSED")) {
                // 按新的trigger重新设置job执行
                Date date = scheduler.rescheduleJob(triggerKey, trigger);
                log.info("更新定时任务成功: {}, JobGroup: {}, JobName: {}, CronExpression: {}", date, jobGroup, jobName,
                        cronExpression);
            }
        }
    }

    /**
     * 方法描述: 运行一次定时任务 <br>
     * @param jobGroup JOB分组
     * @param jobName JOB名称
     * @throws SchedulerException
     */
    public void runOnce(String jobGroup, String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException(MessageFormat.format("JobGroup: {0}, JobName: {1} 不存在", jobGroup, jobName));
        }
        scheduler.triggerJob(jobKey);
        log.info("运行一次定时任务成功, JobGroup: {}, JobName: {}", jobGroup, jobName);
    }

    /**
     * 方法描述: 暂停定时任务 <br>
     * @param jobGroup  JOB分组
     * @param jobName JOB名称
     * @throws SchedulerException
     */
    public void pauseJob(String jobGroup, String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException(MessageFormat.format("JobGroup: {0}, JobName: {1} 不存在", jobGroup, jobName));
        }
        scheduler.pauseJob(jobKey);
        log.info("暂停定时任务成功, JobGroup: {}, JobName: {}", jobGroup, jobName);
    }

    /**
     * 方法描述: 恢复定时任务 <br>
     * @param jobGroup  JOB分组
     * @param jobName  JOB名称
     * @throws SchedulerException
     */
    public void resumeJob(String jobGroup, String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException(MessageFormat.format("JobGroup: {0}, JobName: {1} 不存在", jobGroup, jobName));
        }
        scheduler.resumeJob(jobKey);
        log.info("恢复定时任务成功, JobGroup: {}, JobName: {}", jobGroup, jobName);
    }

    /**
     * 方法描述: 删除定时任务 <br>
     *
     * @param jobGroup JOB分组
     * @param jobName JOB名称
     * @throws SchedulerException
     */
    public void deleteScheduleJob(String jobGroup, String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException(MessageFormat.format("JobGroup: {0}, JobName: {1} 不存在", jobGroup, jobName));
        }
        boolean deleteJob = scheduler.deleteJob(jobKey);
        log.info("删除定时任务{}, JobGroup: {}, JobName: {}", deleteJob ? "成功" : "失败", jobGroup, jobName);
    }

    /**
     * 方法描述: 检查定时任务是否存在 <br>
     *
     * @param jobGroup  JOB分组
     * @param jobName  JOB名称
     * @return
     * @throws SchedulerException
     */
    public boolean isExistsScheduleJob(String jobGroup, String jobName) throws SchedulerException {
        return scheduler.checkExists(JobKey.jobKey(jobName, jobGroup));
    }

}
