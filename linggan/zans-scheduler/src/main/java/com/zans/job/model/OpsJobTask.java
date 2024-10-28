package com.zans.job.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import static com.zans.job.config.JobConstants.*;

@Table(name = "ops_job_task")
public class OpsJobTask implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    /**
     * opt_job_execution.id
     */
    @Column(name = "exec_id")
    private Long execId;

    /**
     * opt_job.id
     */
    @Column(name = "job_id")
    private Integer jobId;

    /**
     * 任务名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * ops_node.id
     */
    @Column(name = "node_id")
    private String nodeId;

    /**
     * 1.任务开始；2.任务正常结束；3.任务异常终止；
     */
    @Column(name = "task_status")
    private Integer taskStatus;

    /**
     * 1.任务开始；2.任务正常结束；3.任务异常终止；
     */
    @Column(name = "alloc_status")
    private Integer allocStatus;

    /**
     * 分配时间
     */
    @Column(name = "alloc_time")
    private Date allocTime;

    /**
     * 任务开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 任务结束时间
     */
    @Column(name = "finish_time")
    private Date finishTime;

    /**
     * 待处理数据量
     */
    @Column(name = "todo_count")
    private Integer todoCount;

    /**
     * 已处理数据量
     */
    @Column(name = "done_count")
    private Integer doneCount;

    /**
     * 进度百分比，
     */
    private Integer progress;

    /**
     * 0, 正常； 1，删除
     */
    private Integer enable;

    /**
     * 任务参数
     */
    private String params;

    /**
     * 任务错误
     */
    private String error;

    /**
     * 任务摘要
     */
    private String content;

    private static final long serialVersionUID = 1L;

    /**
     * @return task_id
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * @param taskId
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取opt_job_execution.id
     *
     * @return exec_id - opt_job_execution.id
     */
    public Long getExecId() {
        return execId;
    }

    /**
     * 设置opt_job_execution.id
     *
     * @param execId opt_job_execution.id
     */
    public void setExecId(Long execId) {
        this.execId = execId;
    }

    /**
     * 获取opt_job.id
     *
     * @return job_id - opt_job.id
     */
    public Integer getJobId() {
        return jobId;
    }

    /**
     * 设置opt_job.id
     *
     * @param jobId opt_job.id
     */
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    /**
     * 获取任务名称
     *
     * @return task_name - 任务名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置任务名称
     *
     * @param taskName 任务名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    /**
     * 获取ops_node.id
     *
     * @return node_id - ops_node.id
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * 设置ops_node.id
     *
     * @param nodeId ops_node.id
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    /**
     * 获取1.任务开始；2.任务正常结束；3.任务异常终止；
     *
     * @return task_status - 1.任务开始；2.任务正常结束；3.任务异常终止；
     */
    public Integer getTaskStatus() {
        return taskStatus;
    }

    /**
     * 设置1.任务开始；2.任务正常结束；3.任务异常终止；
     *
     * @param taskStatus 1.任务开始；2.任务正常结束；3.任务异常终止；
     */
    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 获取分配时间
     *
     * @return alloc_time - 分配时间
     */
    public Date getAllocTime() {
        return allocTime;
    }

    /**
     * 设置分配时间
     *
     * @param allocTime 分配时间
     */
    public void setAllocTime(Date allocTime) {
        this.allocTime = allocTime;
    }

    /**
     * 获取任务开始时间
     *
     * @return start_time - 任务开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置任务开始时间
     *
     * @param startTime 任务开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取任务结束时间
     *
     * @return finish_time - 任务结束时间
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * 设置任务结束时间
     *
     * @param finishTime 任务结束时间
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * 获取待处理数据量
     *
     * @return todo_count - 待处理数据量
     */
    public Integer getTodoCount() {
        return todoCount;
    }

    /**
     * 设置待处理数据量
     *
     * @param todoCount 待处理数据量
     */
    public void setTodoCount(Integer todoCount) {
        this.todoCount = todoCount;
    }

    /**
     * 获取已处理数据量
     *
     * @return done_count - 已处理数据量
     */
    public Integer getDoneCount() {
        return doneCount;
    }

    /**
     * 设置已处理数据量
     *
     * @param doneCount 已处理数据量
     */
    public void setDoneCount(Integer doneCount) {
        this.doneCount = doneCount;
    }

    /**
     * 获取进度百分比，
     *
     * @return progress - 进度百分比，
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * 设置进度百分比，
     *
     * @param progress 进度百分比，
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    /**
     * 获取0, 正常； 1，删除
     *
     * @return enable - 0, 正常； 1，删除
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * 设置0, 正常； 1，删除
     *
     * @param enable 0, 正常； 1，删除
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    /**
     * 获取任务参数
     *
     * @return params - 任务参数
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置任务参数
     *
     * @param params 任务参数
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    /**
     * 获取任务错误
     *
     * @return error - 任务错误
     */
    public String getError() {
        return error;
    }

    /**
     * 设置任务错误
     *
     * @param error 任务错误
     */
    public void setError(String error) {
        this.error = error == null ? null : error.trim();
    }

    /**
     * 获取任务摘要
     *
     * @return content - 任务摘要
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置任务摘要
     *
     * @param content 任务摘要
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getAllocStatus() {
        return allocStatus;
    }

    public void setAllocStatus(Integer allocStatus) {
        this.allocStatus = allocStatus;
    }

    @Override
    public String toString() {
        return "OpsJobTask{" +
                "taskId=" + taskId +
                ", execId=" + execId +
                ", jobId=" + jobId +
                ", taskName='" + taskName + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", taskStatus=" + taskStatus +
                ", allocStatus=" + allocStatus +
                ", allocTime=" + allocTime +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", todoCount=" + todoCount +
                ", doneCount=" + doneCount +
                ", progress=" + progress +
                ", enable=" + enable +
                ", params='" + params + '\'' +
                ", error='" + error + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public OpsJobTask() {
    }

    public OpsJobTask(OpsJobExecution execution) {
        this.execId = execution.getExecId();
        this.jobId = execution.getJobId();
        this.taskName = execution.getExecName();
        this.taskStatus = TASK_NOT;
        this.enable = 1;
    }

    public void doStart() {
        this.setStartTime(new Date());
        this.setTaskStatus(TASK_START);
    }

    public void setFinishStatus() {
        this.setParams(null);
        this.setFinishTime(new Date());
        this.setTaskStatus(TASK_DONE);
    }

    public void setFailStatus() {
        this.setFinishTime(new Date());
        this.setTaskStatus(TASK_FAIL);
    }

    public void doAlloc(String nodeId) {
        this.setNodeId(nodeId);
        this.setAllocTime(new Date());
        this.setAllocStatus(ALLOC_DONE);
    }
}