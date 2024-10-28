package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import com.zans.mms.model.BaseVfs;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工单日志表(TicketsOpLogs)实体类
 *
 * @author beixing
 * @since 2021-01-13 18:26:05
 */
@Data
public class TicketOpLogsRespVO  implements Serializable {

    private static final long serialVersionUID = -71845974760766343L;

    @ApiModelProperty(value = "日志主键")
    private Long id;
    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id")
    private Long ticketId;
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
    @ApiModelProperty(value = "操作类型")
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
     * 是否打卡
     */
    @ApiModelProperty(value = "是否打卡")
    private Integer isClockIn;

    /**
     * 维修状态
     */
    @ApiModelProperty(value = "维修状态")
    private Integer maintenanceStatus;
    /**
     * gis
     */
    @ApiModelProperty(value = "gis")
    private Object gis;
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

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    private List<BaseVfs> adjunctList;


    private String maintainNum;

    private String roleNum;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
