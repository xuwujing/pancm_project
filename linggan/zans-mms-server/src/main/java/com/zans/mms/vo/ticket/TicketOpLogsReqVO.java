package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工单日志表(TicketsOpLogs)实体类
 *
 * @author beixing
 * @since 2021-01-13 18:26:05
 */
@ApiModel(value = "ticketOpLogs", description = "工单日志表")
@Data
public class TicketOpLogsReqVO implements Serializable {

    private static final long serialVersionUID = -71845974760766343L;


    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id")
    private Long id;

    @ApiModelProperty(value = "任务ID")
    private String taskId;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer opCode;
    /**
     * 审批状态
     */
    @ApiModelProperty(value = "审批状态")
    private Integer opApproveStatus;
    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型 0,工单;1:派工;2:验收,3:app")
    private Integer opType;
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
    /**
     * 手机串号
     */
    @ApiModelProperty(value = "手机串号")
    private String mobileImei;
    /**
     * 打开地址
     */
    @ApiModelProperty(value = "打开地址")
    private String operGpsaddr;
    /**
     * gis
     */
    @ApiModelProperty(value = "gis")
    private String gis;
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

    private String roleId;

    /**
     * 是否通过
     * 0:否
     * 1:是
     **/
    private Integer agree;

    /** 审核人
     0: 内场
     1：监理
     2：业主 */
    private Integer userRoleType;


    private Integer ticketStatus;
    /**
     * 派工状态
     */
    private Integer dispatchStatus;
    /**
     * 验证状态
     */
    private Integer acceptStatus;

    private Integer maintenanceStatus;

    /** 扩展数据消息 */
    private String exMsg;

    private Integer isClockIn;

    private Integer isCost;

    /**
     * 分配单位代码
     */
    @ApiModelProperty(value = "分配单位代码")
    private String allocDepartmentNum;

    /**
     * 故障级别
     */
    @ApiModelProperty(value = "故障级别")
    private Integer issueLevel;
    /**
     * 预计抵达时间
     */
    @ApiModelProperty(value = "预计抵达时间")
    private String predictArriveTime;
    /**
     * 预计完成时间
     */
    @ApiModelProperty(value = "预计完成时间")
    private String predictCompleteTime;

    @ApiModelProperty(value = "预计抵达")
    private Integer predictArrive;
    /**
     * 预计完成时间
     */
    @ApiModelProperty(value = "预计完成")
    private Integer predictComplete;

    private Integer opStatus;

    private Integer dealWay;

    /**
     * 活动id
     */
    private String activityId;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
