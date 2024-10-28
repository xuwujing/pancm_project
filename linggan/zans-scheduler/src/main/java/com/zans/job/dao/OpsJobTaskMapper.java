package com.zans.job.dao;

import com.zans.job.model.OpsJobTask;
import com.zans.job.vo.task.TaskReqVO;
import com.zans.job.vo.task.TaskRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface OpsJobTaskMapper extends Mapper<OpsJobTask> {

    void assignTaskToNode(@Param("taskId") Long taskId, @Param("nodeId") String nodeId,
                          @Param("date") Date date, @Param("allocStatus") Integer allocStatus);

    Integer getRunningTaskOfNode(@Param("nodeId") String nodeId);

    Integer getRunningTaskOfExecution(@Param("executionId") Long executionId);

    List<OpsJobTask> getTasksOfExecution(@Param("executionId") Long executionId);

    List<OpsJobTask> getUnAllocTasksOfExecution(@Param("executionId") Long executionId);

    List<OpsJobTask> getRunningTasksOfNode(@Param("nodeId") String nodeId);

    void deleteTasksOfExecution(@Param("executionId") Long executionId);

    List<OpsJobTask> findByJobId(@Param("jobId") Integer jobId);

    List<TaskRespVO> findTaskList(@Param("reqVo") TaskReqVO reqVO);

}