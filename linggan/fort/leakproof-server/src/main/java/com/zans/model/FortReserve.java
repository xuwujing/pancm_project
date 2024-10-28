package com.zans.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fort_reserve")
public class FortReserve implements Serializable {
    /**
     * 自增ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务ip
     */
    @Column(name = "server_ip")
    private String serverIp;

    /**
     * 预约id
     */
    @Column(name = "reserve_id")
    private String reserveId;

    /**
     * 预约开始时间
     */
    @Column(name = "start_time")
    private String startTime;

    /**
     * 预约结束时间
     */
    @Column(name = "end_time")
    private String endTime;

    /**
     * 状态0:申请中，1通过 ,2:否决，3:待开始，4:已开始，5:已结束
     */
    private Integer status;

    /**
     * 预约理由
     */
    @Column(name = "reserve_reason")
    private String reserveReason;

    /**
     * 申请人
     */
    private String proposer;

    /**
     * 审批人
     */
    private String approver;

    /**
     * 审批理由
     */
    @Column(name = "approve_reason")
    private String approveReason;

    /**
     * 审批时间
     */
    @Column(name = "approve_time")
    private String approveTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;
}
