package com.zans.portal.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "asset_scan")
@Data
public class AssetScan implements Serializable {
    @Id
    private Long id;

    /**
     * IP
     */
    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * 在线 离线
     */
    private Integer alive;

    /**
     * 状态开始时间
     */
    @Column(name = "begin_time")
    private String beginTime;

    /**
     * 状态结束时间
     */
    @Column(name = "end_time")
    private String endTime;

    /**
     * 状态持续时长，单位s
     */
    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    /**
     * 1-当前ip最近一条记录
     */
    @Column(name = "last_flag")
    private Integer lastFlag;

    /**
     * 每扫描一次 此值加一，初始值为1
     */
    @Column(name = "scan_num")
    private Integer scanNum;

    /**
     * 每发送一次，此值加一，初始值为0
     */
    @Column(name = "qrcode_send_num")
    private Integer qrcodeSendNum;

    @Column(name = "qrcode_send_num_alive")
    private Integer qrcodeSendNumAlive;

    /**
     * 0为未推送 1为已推送
     */
    @Column(name = "push_status")
    private Integer pushStatus;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    @Transient
    private Integer diffHour;

    @Transient
    private Integer stateStatus;

    @Transient
    private String onlineName;

}
