package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 工单日志表(TicketsOpLogs)实体类
 *
 * @author beixing
 * @since 2021-01-13 18:26:05
 */
@ApiModel(value = "ticketReportReqVO", description = "工单维修上报")
@Data
public class TicketReportReqVO implements Serializable {

    private static final long serialVersionUID = -71845974760766343L;


    /**
     * 工单id
     */
    @NonNull
    @ApiModelProperty(value = "工单id")
    private Long id;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String msg;
    /**
     * 附件编号
     */
    @ApiModelProperty(value = "附件编号")
    private String adjunctId;
    /**
     * 操作平台 ;1 - PC
     * 2 - 微信app
     */
    @ApiModelProperty(value = "操作平台 ;1 - PC 2-微信app")
    private String opPlatform;

    @ApiModelProperty(value = "操作类型 0,工单;1:派工;2:验收,3:app")
    private Integer opType;
    /**
     * 打开地址
     */
    @ApiModelProperty(value = "打开地址")
    private String operGpsaddr;

    @ApiModelProperty(value = "实际故障类型")
    private Integer practicalIssueType;
    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String opIpaddr;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String creator;


    private Integer maintenanceStatus;

    /**
     * 是否打卡
     */
    private Integer isClockIn;

    /**
     * 地址
     */
    private String address;

    private BigDecimal longitude;

    private BigDecimal latitude;

    /**
     * 处理方式
     */
    private Integer dealWay;


    public TicketReportReqVO() {

    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
