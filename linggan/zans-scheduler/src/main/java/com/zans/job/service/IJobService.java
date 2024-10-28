package com.zans.job.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.job.model.OpsJob;
import com.zans.job.vo.common.CircleUnit;
import com.zans.job.vo.job.JobReqVO;
import com.zans.job.vo.job.JobRespVO;

import java.util.List;
import java.util.Map;

/**
 * @author xv
 * @since 2020/5/7 17:21
 */
public interface IJobService extends BaseService<OpsJob> {

    PageResult<JobRespVO> getJobPage(JobReqVO reqVO);

    int getJobByJobNameAndId(String jobName, Integer jobId);

    List<CircleUnit> getJobCount();

    List<CircleUnit> getJobPieChart();

    List<CircleUnit> executes();

}
