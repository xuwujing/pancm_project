package com.zans.job.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.job.model.OpsJobTask;
import com.zans.job.vo.task.TaskReqVO;
import com.zans.job.vo.task.TaskRespVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xv
 * @since 2020/5/7 17:25
 */
public interface ITaskService extends BaseService<OpsJobTask> {

    void assignTaskToNode(Long taskId, String nodeId, Integer status);

    Integer getRunningTaskOfNode(String nodeId);

    Integer getRunningTaskOfExecution(Long executionId);

    List<OpsJobTask> getTasksOfExecution(Long executionId);

    List<OpsJobTask> getUnAllocTasksOfExecution(Long executionId);

    /**
     * 某个节点上，所有未完成的任务
     * @param nodeId
     * @return
     */
    List<OpsJobTask> getRunningTasksOfNode(String nodeId);

    void deleteTasksOfExecution(Long executionId);

    List<OpsJobTask> findByJobId(Integer jobId);

    PageResult<TaskRespVO> getTaskPage(TaskReqVO reqVO);

}
