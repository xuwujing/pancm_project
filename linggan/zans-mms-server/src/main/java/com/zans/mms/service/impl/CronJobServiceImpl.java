package com.zans.mms.service.impl;


import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.mms.dao.CronJobMapper;
import com.zans.mms.model.CronJob;
import com.zans.mms.model.PatrolTask;
import com.zans.mms.service.ICronJobService;
import com.zans.mms.service.IPatrolSchemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *  CronJobServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("cronJobService")
public class CronJobServiceImpl extends BaseServiceImpl<CronJob> implements ICronJobService {
		
		
	@Autowired
	private CronJobMapper cronJobMapper;

	@Autowired
    private IPatrolSchemeService patrolSchemeService;

    @Resource
    public void setCronJobMapper(CronJobMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.cronJobMapper = baseMapper;
    }

    @Override
    public int deleteByTypeAndId(String jobType, Long id) {
        return cronJobMapper.deleteByTypeAndId(jobType,id);
    }

    @Override
    public int updateNextTime(String jobType, Long id, Integer period) {
        return cronJobMapper.updateNextTime(jobType,id,period);
    }

    @Override
    public int generatePatrolTask() {
        List<CronJob> cronJobList = cronJobMapper.getListByCondition(new HashMap<String,Object>(){{
			put("enableStatus","启用");
			put("jobType","patrol");
		}});

        CronJob updateCronJob;
        for (CronJob cronJob : cronJobList) {
            try {
                PatrolTask patrolTask =  patrolSchemeService.generateTaskBySchemeId(cronJob.getRelationId());
                if (null != patrolTask){
                Date nextTimeTemp = patrolTask.getStartTime();
                Date nextTime = patrolTask.getEndTime();
                    updateCronJob = new CronJob();
                    updateCronJob.setId(cronJob.getId());
                    updateCronJob.setPrevTime(nextTimeTemp);
                    updateCronJob.setNextTime(nextTime);
                    cronJobMapper.updateByPrimaryKeySelective(updateCronJob);
                }
            }catch (Exception e) {
                log.info("cron.generatePatrolTask "+e.getMessage());
            }

        }
        return 1;
    }


    @Override
    public void updateStatus(String patrol, Long id, String enableStatus) {
        cronJobMapper.updateStatus(patrol,id,enableStatus);
    }
}