package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "patrol_task_check_result")
public class PatrolTaskCheckResult implements Serializable {
    /**
     * 自增id
     */
    @Id
    @GeneratedValue(generator = "JDBC")// tk 插入返回主键
    private Long id;

    /**
     * 巡检任务编号ID
     */
    @Column(name = "patrol_task_id")
    private Long patrolTaskId;

    /**
     * 点位编号
     */
    @Column(name = "point_id")
    private Long pointId;

    /**
     * 正常,故障,停用
     */
    @Column(name = "prev_check_result")
    private String prevCheckResult;

    /**
     * 正常,故障,停用
     */
    @Column(name = "check_result")
    private String checkResult;

    /**
     * 巡检状态,1:已巡检 0：未巡检
     */
    @Column(name = "check_status")
    private Integer checkStatus;

    /**
     * 打卡时间
     */
    @Column(name = "check_time")
    private Date checkTime;

    /**
     * GCJ02 经度
     */
    @Column(name = "check_longitude")
    private BigDecimal checkLongitude;

    /**
     * GCJ02 维度
     */
    @Column(name = "check_latitude")
    private BigDecimal checkLatitude;

    /**
     * 打卡地点
     */
    @Column(name = "check_site")
    private String checkSite;
    /**
     * 打卡地点
     */
    @Column(name = "check_user")
    private String checkUser;

    /**
     * 打卡图片附件编号
     */
    @Column(name = "adjunct_uuid")
    private String adjunctUuid;

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