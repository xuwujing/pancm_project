package com.zans.job.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "ops_job")
public class OpsJob implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Integer jobId;

    /**
     * 作业名
     */
    @Column(name = "job_name")
    private String jobName;

    /**
     * 作业类型
     */
    @Column(name = "job_type")
    private String jobType;

    /**
     * 作业的Java类名
     */
    @Column(name = "job_class_name")
    private String jobClassName;

    /**
     * 运行参数：0，数值单位s；1，cron表达式字符
     */
    @Column(name = "cron_expression")
    private String cronExpression;

    /**
     * 优先级，0最高
     */
    private Integer priority;

    /**
     * 任务中文说明
     */
    private String remark;

    /**
     * 0，禁用；1，启用
     */
    private Integer enable;

    /**
     * 0，不分片；1，分片
     */
    private Integer sharding;

    /**
     * 分片数量
     */
    @Column(name = "sharding_num")
    private Integer shardingNum;

    /**
     * 任务的最长运行时间，单位秒，默认30分钟
     */
    private Integer timeout;

    /**
     * 运行函数，package+函数名
     */
    @Column(name = "job_module")
    private String jobModule;

    /**
     * 运行次数
     */
    @Column(name = "run_count")
    private Integer runCount;

    /**
     * 最近一次运行时间
     */
    @Column(name = "run_last_time")
    private Date runLastTime;

    /**
     * 扫描的记录数
     */
    @Column(name = "record_count")
    private Integer recordCount;

    /**
     * 是否初始化
     */
    private Integer init;

    /**
     * 初始化顺序，小的优先
     */
    @Column(name = "init_seq")
    private Integer initSeq;

    /**
     * 作业参数
     */
    @Column(name = "job_data")
    private String jobData;

    private static final long serialVersionUID = 1L;

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
     * 获取作业名
     *
     * @return job_name - 作业名
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置作业名
     *
     * @param jobName 作业名
     */
    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    /**
     * 获取作业的Java类名
     *
     * @return job_class_name - 作业的Java类名
     */
    public String getJobClassName() {
        return jobClassName;
    }

    /**
     * 设置作业的Java类名
     *
     * @param jobClassName 作业的Java类名
     */
    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName == null ? null : jobClassName.trim();
    }

    /**
     * 获取运行参数：0，数值单位s；1，cron表达式字符
     *
     * @return cron_expression - 运行参数：0，数值单位s；1，cron表达式字符
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 设置运行参数：0，数值单位s；1，cron表达式字符
     *
     * @param cronExpression 运行参数：0，数值单位s；1，cron表达式字符
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }

    /**
     * 获取优先级，0最高
     *
     * @return priority - 优先级，0最高
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 设置优先级，0最高
     *
     * @param priority 优先级，0最高
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * 获取任务中文说明
     *
     * @return remark - 任务中文说明
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置任务中文说明
     *
     * @param remark 任务中文说明
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取0，不分片；1，分片
     *
     * @return sharding - 0，不分片；1，分片
     */
    public Integer getSharding() {
        return sharding;
    }

    /**
     * 设置0，不分片；1，分片
     *
     * @param sharding 0，不分片；1，分片
     */
    public void setSharding(Integer sharding) {
        this.sharding = sharding;
    }

    /**
     * 获取分片数量
     *
     * @return sharding_num - 分片数量
     */
    public Integer getShardingNum() {
        return shardingNum;
    }

    /**
     * 设置分片数量
     *
     * @param shardingNum 分片数量
     */
    public void setShardingNum(Integer shardingNum) {
        this.shardingNum = shardingNum;
    }

    /**
     * 获取任务的最长运行时间，单位秒，默认30分钟
     *
     * @return timeout - 任务的最长运行时间，单位秒，默认30分钟
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * 设置任务的最长运行时间，单位秒，默认30分钟
     *
     * @param timeout 任务的最长运行时间，单位秒，默认30分钟
     */
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    /**
     * 获取运行函数，package+函数名
     *
     * @return task_module - 运行函数，package+函数名
     */
    public String getJobModule() {
        return jobModule;
    }

    /**
     * 设置运行函数，package+函数名
     *
     * @param jobModule 运行函数，package+函数名
     */
    public void setJobModule(String jobModule) {
        this.jobModule = jobModule == null ? null : jobModule.trim();
    }

    /**
     * 获取运行次数
     *
     * @return run_count - 运行次数
     */
    public Integer getRunCount() {
        return runCount;
    }

    /**
     * 设置运行次数
     *
     * @param runCount 运行次数
     */
    public void setRunCount(Integer runCount) {
        this.runCount = runCount;
    }

    /**
     * 获取最近一次运行时间
     *
     * @return run_last_time - 最近一次运行时间
     */
    public Date getRunLastTime() {
        return runLastTime;
    }

    /**
     * 设置最近一次运行时间
     *
     * @param runLastTime 最近一次运行时间
     */
    public void setRunLastTime(Date runLastTime) {
        this.runLastTime = runLastTime;
    }

    /**
     * 获取扫描的记录数
     *
     * @return record_count - 扫描的记录数
     */
    public Integer getRecordCount() {
        return recordCount;
    }

    /**
     * 设置扫描的记录数
     *
     * @param recordCount 扫描的记录数
     */
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * 获取是否初始化
     *
     * @return init - 是否初始化
     */
    public Integer getInit() {
        return init;
    }

    /**
     * 设置是否初始化
     *
     * @param init 是否初始化
     */
    public void setInit(Integer init) {
        this.init = init;
    }

    /**
     * 获取初始化顺序，小的优先
     *
     * @return init_seq - 初始化顺序，小的优先
     */
    public Integer getInitSeq() {
        return initSeq;
    }

    /**
     * 设置初始化顺序，小的优先
     *
     * @param initSeq 初始化顺序，小的优先
     */
    public void setInitSeq(Integer initSeq) {
        this.initSeq = initSeq;
    }

    /**
     * 获取作业参数
     *
     * @return job_data - 作业参数
     */
    public String getJobData() {
        return jobData;
    }

    /**
     * 设置作业参数
     *
     * @param jobData 作业参数
     */
    public void setJobData(String jobData) {
        this.jobData = jobData == null ? null : jobData.trim();
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Override
    public String toString() {
        return "OpsJob{" +
                "jobId=" + jobId +
                ", jobName='" + jobName + '\'' +
                ", jobType=" + jobType +
                ", jobClassName='" + jobClassName + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", priority=" + priority +
                ", remark='" + remark + '\'' +
                ", enable=" + enable +
                ", sharding=" + sharding +
                ", shardingNum=" + shardingNum +
                ", timeout=" + timeout +
                ", jobModule='" + jobModule + '\'' +
                ", runCount=" + runCount +
                ", runLastTime=" + runLastTime +
                ", recordCount=" + recordCount +
                ", init=" + init +
                ", initSeq=" + initSeq +
                ", jobData='" + jobData + '\'' +
                '}';
    }

}