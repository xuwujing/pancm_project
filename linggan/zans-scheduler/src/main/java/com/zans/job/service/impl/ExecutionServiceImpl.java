package com.zans.job.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.job.dao.OpsJobExecutionMapper;
import com.zans.job.model.OpsJob;
import com.zans.job.model.OpsJobExecution;
import com.zans.job.model.OpsJobTask;
import com.zans.job.service.IExecutionService;
import com.zans.job.service.IJobService;
import com.zans.job.service.ITaskService;
import com.zans.job.vo.execution.ExecuReqVO;
import com.zans.job.vo.execution.ExecuRespVO;
import com.zans.job.vo.job.JobReqVO;
import com.zans.job.vo.job.JobRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.zans.job.config.JobConstants.EXECUTION_DONE;

/**
 * @author xv
 * @since 2020/5/7 17:26
 */
@Slf4j
@Service("executionService")
public class ExecutionServiceImpl extends BaseServiceImpl<OpsJobExecution> implements IExecutionService {

    OpsJobExecutionMapper executionMapper;

    @Autowired
    ITaskService taskService;

    @Autowired
    IJobService jobService;

    @Resource
    public void setExecutionMapper(OpsJobExecutionMapper executionMapper) {
        super.setBaseMapper(executionMapper);
        this.executionMapper = executionMapper;
    }

    @Override
    public List<OpsJobExecution> getExecutionByJob(Integer jobId) {
        return executionMapper.getExecutionByJob(jobId);
    }

    @Override
    public List<OpsJobExecution> getRunningExecutionByJob(Integer jobId) {
        return executionMapper.getRunningExecutionByJob(jobId);
    }

    @Override
    public OpsJobExecution checkExecution(Long executionId) {

        OpsJobExecution execution = this.getById(executionId);
        if (execution == null) {
            return null;
        }
        Integer count = taskService.getRunningTaskOfExecution(executionId);
        log.info("checkExecution#{}, running#{}", executionId, count);
        if (count == null) {
            count = 0;
        }

        if (count == 0) {
            execution.setJobStatus(EXECUTION_DONE);
            execution.setFinishTime(new Date());
            execution.setFinishedSharding(execution.getShardingNum());
            this.update(execution);

            OpsJob job = jobService.getById(execution.getJobId());
            job.setRunCount(job.getRunCount() + 1);
            job.setRunLastTime(new Date());
            jobService.update(job);
        } else {
            // 更新分片的状态
            execution.setFinishedSharding(execution.getShardingNum() - count);
            this.update(execution);
        }
        return execution;
    }

    @Override
    public List<OpsJobExecution> getExecutionByJobAndSharding(Integer jobId, Integer shardingNum) {
        return executionMapper.getExecutionByJobAndSharding(jobId, shardingNum);
    }


    @Override
    public PageResult<ExecuRespVO> getExecutePage(ExecuReqVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<ExecuRespVO> list = executionMapper.findExecuteList(reqVO);

        return new PageResult<ExecuRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }


}
