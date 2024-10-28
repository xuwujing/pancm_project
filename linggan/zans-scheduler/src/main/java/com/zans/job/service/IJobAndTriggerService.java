package com.zans.job.service;

import com.zans.job.vo.JobAndTrigger;

import java.util.List;

/**
 * @author xv
 * @since 2020/5/6 16:13
 */
public interface IJobAndTriggerService {

    List<JobAndTrigger> getJobAndTriggerDetails();
}
