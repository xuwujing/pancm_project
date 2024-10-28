package com.zans.job.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.job.model.OpsJobExecution;
import com.zans.job.vo.execution.ExecuReqVO;
import com.zans.job.vo.execution.ExecuRespVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xv
 * @since 2020/5/7 17:24
 */
public interface IExecutionService extends BaseService<OpsJobExecution> {

    List<OpsJobExecution> getExecutionByJob(Integer jobId);

    List<OpsJobExecution> getRunningExecutionByJob(Integer jobId);

    OpsJobExecution checkExecution(Long executionId);

    List<OpsJobExecution> getExecutionByJobAndSharding(Integer jobId, Integer shardingNum);

    PageResult<ExecuRespVO> getExecutePage(ExecuReqVO reqVO);

}
