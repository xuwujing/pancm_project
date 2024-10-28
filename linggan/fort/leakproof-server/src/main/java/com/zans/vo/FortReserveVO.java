package com.zans.vo;

import com.zans.model.FortPlayBack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FortReserveVO extends BasePage implements Serializable {
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

    private String serverName;//主机名称

    private String reserveId;

    /**
     * 预约日期
     */
    private String appointmentDate;

    /**
     * 用时
     */
    private String takingTime;

    /**
     *  申请天
     */
    private String day;

    /**
     * 申请时间
     */
    private List<String> time;

    /**
     * 当前时间
     */
    private String nowTime;

    public String currentReserve;

    public String historyReserve;

    public LastTimeVO lastTimeVO;

    public List<FortPlayBack> playBacks;

    /**
     * 应用名称
     */
    public String serverApplication;

    private String statusName;

    private Integer videoStatus;

    private Integer noApprove;

    private boolean pingSuccess;

    public FortReserveVO(Integer id,String startTime, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public FortReserveVO(Integer id,String startTime, String endTime,String serverIp) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.serverIp = serverIp;
    }

    public FortReserveVO(String serverName, String startTime, String endTime, String proposer, String nowTime) {
        this.serverName = serverName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.proposer = proposer;
        this.nowTime = nowTime;
    }
}
