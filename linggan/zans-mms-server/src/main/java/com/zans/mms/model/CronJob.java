package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "cron_job")
public class CronJob implements Serializable {
    /**
     * 自增id
     */
    @Id
    @GeneratedValue(generator = "JDBC")// tk 插入返回主键
    private Long id;

    /**
     * 巡检计划名称
     */
    @Column(name = "job_name")
    private String jobName;

    /**
     * 任务类型patrol
     */
    @Column(name = "job_type")
    private String jobType;

    /**
     * 关联的任务id
     */
    @Column(name = "relation_id")
    private Long relationId;

    /**
     * 上一次计划时间
     */
    @Column(name = "prev_time")
    private Date prevTime;

    /**
     * 下一次计划时间
     */
    @Column(name = "next_time")
    private Date nextTime;

    /**
     * 停用,启用
     */
    @Column(name = "enable_status")
    private String enableStatus;

    /**
     * 创建用户
     */
    private String creator;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}