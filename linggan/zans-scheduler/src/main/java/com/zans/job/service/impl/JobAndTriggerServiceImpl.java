package com.zans.job.service.impl;

import com.zans.job.vo.JobAndTrigger;
import com.zans.job.service.IJobAndTriggerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xv
 * @since 2020/5/6 16:16
 */
@Slf4j
@Service
public class JobAndTriggerServiceImpl implements IJobAndTriggerService {

    @Override
    public List<JobAndTrigger> getJobAndTriggerDetails() {
        return null;
    }
}
