package com.zans.job.api;

import com.zans.job.model.OpsJob;
import com.zans.job.model.OpsJobExecution;
import com.zans.job.service.IExecutionService;
import com.zans.job.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author xv
 * @since 2020/5/6 15:58
 * 参考： https://blog.csdn.net/haoxiaoyong1014/article/details/83339169
 * http://www.quartz-scheduler.org/documentation/quartz-2.3.0/cookbook/
 */
@Slf4j
@Component("HelloJob")
public class HelloJob implements BaseJob {

    @Autowired
    IJobService jobService;

    @Autowired
    IExecutionService executionService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        log.info("v2 say hello, execute time#{}", new Date());
        Integer jobId = null;
        if (dataMap != null) {
            for(String key : dataMap.keySet()) {
                log.info(" {}:{}", key, dataMap.get(key));
                if ("job_id".equals(key)) {
                    jobId = dataMap.getInt(key);
                }
            }
        }
        if (jobId == null) {
            return;
        }
        OpsJob job = jobService.getById(jobId);
        if (job == null) {
            return;
        }
        int shardingNum = job.getShardingNum();
        if (shardingNum > 200) {
            return;
        }

        List<OpsJobExecution> exampleList = executionService.getExecutionByJobAndSharding(jobId, shardingNum);
        if (exampleList.size() >= 3) {
            job.setShardingNum(shardingNum + 10);
            jobService.update(job);
        }
    }
}
