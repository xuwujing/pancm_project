package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.mms.model.CronJob;

/**
 * interface CronJobservice
 *
 * @author
 */
public interface ICronJobService extends BaseService<CronJob>{


    int deleteByTypeAndId(String jobType, Long id);

    int updateNextTime(String jobType, Long id, Integer period);

    int generatePatrolTask();
}
