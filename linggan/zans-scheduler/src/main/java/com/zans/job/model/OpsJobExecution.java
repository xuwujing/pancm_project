package com.zans.job.model;

import com.zans.base.util.MapBuilder;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import static com.zans.job.config.JobConstants.*;

@Table(name = "ops_job_execution")
public class OpsJobExecution implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exec_id")
    private Long execId;

    @Column(name = "job_id")
    private Integer jobId;

    /**
     * 任务当下名称，避免改名
     */
    @Column(name = "exec_name")
    private String execName;

    /**
     * 待处理记录数
     */
    @Column(name = "record_count")
    private Integer recordCount;


    /**
     * 任务分片的节点数
     */
    @Column(name = "sharding_node_num")
    private Integer shardingNodeNum;

    /**
     * 任务分片数
     */
    @Column(name = "sharding_num")
    private Integer shardingNum;

    /**
     * 完成的任务分片数
     */
    @Column(name = "finished_sharding")
    private Integer finishedSharding;

    /**
     * 1.任务开始；2.任务正常结束；3.任务超时；4.异常终止；
     */
    @Column(name = "job_status")
    private Integer jobStatus;

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
     * 任务摘要
     */
    private String content;

    /**
     * 任务异常信息
     */
    private String error;

    private static final long serialVersionUID = 1L;

    /**
     * @return exec_id
     */
    public Long getExecId() {
        return execId;
    }

    /**
     * @param execId
     */
    public void setExecId(Long execId) {
        this.execId = execId;
    }

    /**
     * @return job_id
     */
    public Integer getJobId() {
        return jobId;
    }

    /**
     * @param jobId
     */
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    /**
     * 获取任务当下名称，避免改名
     *
     * @return exec_name - 任务当下名称，避免改名
     */
    public String getExecName() {
        return execName;
    }

    /**
     * 设置任务当下名称，避免改名
     *
     * @param execName 任务当下名称，避免改名
     */
    public void setExecName(String execName) {
        this.execName = execName == null ? null : execName.trim();
    }

    /**
     * 获取待处理记录数
     *
     * @return record_count - 待处理记录数
     */
    public Integer getRecordCount() {
        return recordCount;
    }

    /**
     * 设置待处理记录数
     *
     * @param recordCount 待处理记录数
     */
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * 获取任务分片数
     *
     * @return sharding_num - 任务分片数
     */
    public Integer getShardingNum() {
        return shardingNum;
    }

    /**
     * 设置任务分片数
     *
     * @param shardingNum 任务分片数
     */
    public void setShardingNum(Integer shardingNum) {
        this.shardingNum = shardingNum;
    }

    /**
     * 获取1.任务开始；2.任务正常结束；3.任务超时；4.异常终止；
     *
     * @return job_status - 1.任务开始；2.任务正常结束；3.任务超时；4.异常终止；
     */
    public Integer getJobStatus() {
        return jobStatus;
    }

    /**
     * 设置1.任务开始；2.任务正常结束；3.任务超时；4.异常终止；
     *
     * @param jobStatus 1.任务开始；2.任务正常结束；3.任务超时；4.异常终止；
     */
    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
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

    /**
     * 获取任务异常信息
     *
     * @return error - 任务异常信息
     */
    public String getError() {
        return error;
    }

    /**
     * 设置任务异常信息
     *
     * @param error 任务异常信息
     */
    public void setError(String error) {
        this.error = error == null ? null : error.trim();
    }

    public Integer getFinishedSharding() {
        return finishedSharding;
    }

    public void setFinishedSharding(Integer finishedSharding) {
        this.finishedSharding = finishedSharding;
    }

    public Integer getShardingNodeNum() {
        return shardingNodeNum;
    }

    public void setShardingNodeNum(Integer shardingNodeNum) {
        this.shardingNodeNum = shardingNodeNum;
    }

    @Override
    public String toString() {
        return "OpsJobExecution{" +
                "execId=" + execId +
                ", jobId=" + jobId +
                ", execName='" + execName + '\'' +
                ", recordCount=" + recordCount +
                ", shardingNodeNum=" + shardingNodeNum +
                ", shardingNum=" + shardingNum +
                ", finishedSharding=" + finishedSharding +
                ", jobStatus=" + jobStatus +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", content='" + content + '\'' +
                ", error='" + error + '\'' +
                '}';
    }

    public OpsJobExecution() {

    }

    public OpsJobExecution(OpsJob job) {
        this.setJobId(job.getJobId());
        this.setExecName(job.getJobName());
        this.setRecordCount(0);
        this.setShardingNum(0);
        this.setStartTime(new Date());
        this.setJobStatus(EXECUTION_NOT);
    }

    public void setTimeoutStatus() {
        this.setFinishTime(new Date());
        this.setContent(MapBuilder.getBuilder().put("result", "执行超时").toJsonString());
        this.setJobStatus(EXECUTION_TIMEOUT);
    }

    public void setAssignErrorStatus() {
        this.setFinishTime(new Date());
        this.setRecordCount(0);
        this.setContent(MapBuilder.getBuilder().put("result", "分配失败").toJsonString());
        this.setJobStatus(EXECUTION_ERROR);
    }

    public void setNoDataStatus() {
        this.setFinishTime(new Date());
        this.setRecordCount(0);
        this.setContent(MapBuilder.getBuilder().put("result", "没有数据").toJsonString());
        this.setJobStatus(EXECUTION_DONE);
    }
}