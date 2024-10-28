package com.zans.mms.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 工单日志表(TicketOpLogs)实体类
 *
 * @author beixing
 * @since 2021-03-03 15:38:15
 */
@ApiModel(value = "TicketOpLogs", description = "工单日志表")
@Data
@Table(name = "ticket_op_logs")
public class TicketOpLogs implements Serializable {
    private static final long serialVersionUID = 367905486517306684L;
    @Column(name = "id")
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    /**
     * 工单id
     */
    @Column(name = "ticket_id")
    @ApiModelProperty(value = "工单id")
    private Long ticketId;
    /**
     * 状态
     */
    @Column(name = "op_code")
    @ApiModelProperty(value = "状态")
    private Integer opCode;
    /**
     * 审批状态
     */
    @Column(name = "op_approve_status")
    @ApiModelProperty(value = "审批状态")
    private Integer opApproveStatus;
    /**
     * 操作类型
     */
    @Column(name = "op_type")
    @ApiModelProperty(value = "操作类型")
    private Integer opType;
    /**
     * 消息内容
     */
    @Column(name = "msg")
    @ApiModelProperty(value = "消息内容")
    private String msg;
    /**
     * 附件编号
     */
    @Column(name = "adjunct_id")
    @ApiModelProperty(value = "附件编号")
    private String adjunctId;
    /**
     * 操作平台 ;1 - PC
     * 2 - 微信app
     */
    @Column(name = "op_platform")
    @ApiModelProperty(value = "操作平台1 - PC 2-微信app")
    private String opPlatform;
    /**
     * 手机串号
     */
    @Column(name = "mobile_imei")
    @ApiModelProperty(value = "手机串号")
    private String mobileImei;
    /**
     * 打开地址
     */
    @Column(name = "oper_gpsaddr")
    @ApiModelProperty(value = "打开地址")
    private String operGpsaddr;

    /**
     * 是否打卡
     */
    @Column(name = "is_clock_in")
    @ApiModelProperty(value = "是否打卡")
    private Integer isClockIn;

    /**
     * 维修状态
     */
    @Column(name = "maintenance_status")
    @ApiModelProperty(value = "维修状态")
    private Integer maintenanceStatus;


    /**
     * gis
     */
    @Column(name = "gis")
    @ApiModelProperty(value = "gis")
    private String gis;
    /**
     * ip地址
     */
    @Column(name = "op_ipaddr")
    @ApiModelProperty(value = "ip地址")
    private String opIpaddr;


    /**
     * 创建者
     */
    @Column(name = "creator")
    @ApiModelProperty(value = "创建者")
    private String creator;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
