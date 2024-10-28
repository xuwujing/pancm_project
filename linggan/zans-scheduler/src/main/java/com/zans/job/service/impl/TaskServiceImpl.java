package com.zans.job.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.job.dao.OpsJobTaskMapper;
import com.zans.job.model.OpsJobTask;
import com.zans.job.service.ITaskService;
import com.zans.job.vo.job.JobReqVO;
import com.zans.job.vo.job.JobRespVO;
import com.zans.job.vo.task.TaskReqVO;
import com.zans.job.vo.task.TaskRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author xv
 * @since 2020/5/7 17:27
 */
@Service("taskService")
@Slf4j
public class TaskServiceImpl extends BaseServiceImpl<OpsJobTask> implements ITaskService {

    OpsJobTaskMapper taskMapper;

    @Resource
    public void setTaskMapper(OpsJobTaskMapper taskMapper) {
        setBaseMapper(taskMapper);
        this.taskMapper = taskMapper;
    }

    @Override
    public void assignTaskToNode(Long taskId, String nodeId, Integer status) {
        this.taskMapper.assignTaskToNode(taskId, nodeId, new Date(), status);
    }

    @Override
    public Integer getRunningTaskOfNode(String nodeId) {
        return taskMapper.getRunningTaskOfNode(nodeId);
    }

    @Override
    public Integer getRunningTaskOfExecution(Long executionId) {
        return taskMapper.getRunningTaskOfExecution(executionId);
    }

    @Override
    public List<OpsJobTask> getTasksOfExecution(Long executionId) {
        return taskMapper.getTasksOfExecution(executionId);
    }

    @Override
    public List<OpsJobTask> getUnAllocTasksOfExecution(Long executionId) {
        return taskMapper.getUnAllocTasksOfExecution(executionId);
    }

    @Override
    public List<OpsJobTask> getRunningTasksOfNode(String nodeId) {
        return taskMapper.getRunningTasksOfNode(nodeId);
    }

    @Override
    public void deleteTasksOfExecution(Long executionId) {
        taskMapper.deleteTasksOfExecution(executionId);
    }

    @Override
    public List<OpsJobTask> findByJobId(Integer jobId) {
        return taskMapper.findByJobId(jobId);
    }

    @Override
    public PageResult<TaskRespVO> getTaskPage(TaskReqVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<TaskRespVO> list = taskMapper.findTaskList(reqVO);
        return new PageResult<TaskRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

}
