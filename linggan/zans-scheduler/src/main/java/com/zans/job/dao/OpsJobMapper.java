package com.zans.job.dao;

import com.zans.job.model.OpsJob;
import com.zans.job.vo.common.CircleUnit;
import com.zans.job.vo.job.JobReqVO;
import com.zans.job.vo.job.JobRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface OpsJobMapper extends Mapper<OpsJob> {

    List<JobRespVO> findJobList(@Param("reqVo") JobReqVO reqVO);

    int getJobByJobNameAndId(@Param("jobName") String jobName, @Param("jobId") Integer jobId);

    List<CircleUnit> getJobCount();

    List<CircleUnit> getJobPieChart();

    List<CircleUnit> getJobByHours(@Param("dates") List<String> dates);

}