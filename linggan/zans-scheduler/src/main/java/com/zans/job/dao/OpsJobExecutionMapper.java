package com.zans.job.dao;

import com.zans.job.model.OpsJobExecution;
import com.zans.job.vo.execution.ExecuReqVO;
import com.zans.job.vo.execution.ExecuRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface OpsJobExecutionMapper extends Mapper<OpsJobExecution> {

    List<OpsJobExecution> getExecutionByJob(Integer jobId);

    List<OpsJobExecution> getRunningExecutionByJob(Integer jobId);

    List<OpsJobExecution> getExecutionByJobAndSharding(@Param("jobId") Integer jobId, @Param("shardingNum") Integer shardingNum);

    List<ExecuRespVO> findExecuteList(@Param("reqVo") ExecuReqVO reqVo);

}