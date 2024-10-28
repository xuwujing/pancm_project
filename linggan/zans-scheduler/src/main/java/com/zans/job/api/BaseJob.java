package com.zans.job.api;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author xv
 * @since 2020/5/6 15:57
 */
public interface BaseJob extends Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException;
}
