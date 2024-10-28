package com.zans.job.api.back;

import com.zans.job.api.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 检测节点的的 alive状态
 * 
 * @author xv
 * @since 2020/5/12 18:45
 */
@Slf4j
@Component("TaskAliveJob")
public class TaskAliveJob implements BaseJob {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
