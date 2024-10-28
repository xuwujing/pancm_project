package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import com.zans.mms.model.TicketPoint;
import com.zans.mms.model.TicketPointDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工单表(Tickets)实体类
 *
 * @author beixing
 * @since 2021-01-13 18:14:25
 */
@ApiModel(value = "ticketSaveReqVO", description = "工单新增信息")
@Data
public class AppTicketSaveReqVO implements Serializable {
    private static final long serialVersionUID = -22713165493473764L;


    private Long id;

    private Integer editStatus;


    /**
     * 工单编码
     */
    @ApiModelProperty(value = "工单编码")
    private String ticketCode;
    /**
     * 工单类型
     */
    @ApiModelProperty(value = "工单类型")
    private Integer ticketType;
    /**
     * 辖区id
     */
    @ApiModelProperty(value = "辖区id")
    private String areaId;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    /**
     * 故障类型
     */
    @ApiModelProperty(value = "故障类型")
    private Integer issueType;



    /**
     * 故障来源
     */
    @ApiModelProperty(value = "故障来源")
    private Integer issueSource;

    /**
     * 联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名")
    private String applyContact;
    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String applyPhone;
    /**
     * 故障发生时间
     */
    @ApiModelProperty(value = "故障发生时间")
    private String occurredTime;
    /**
     * 附件编号
     */
    @ApiModelProperty(value = "附件编号")
    private String adjunctId;


    /**
     * 是否核算
     */
    @ApiModelProperty(value = "是否核算")
    private Integer isCost;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;
    /**
     * 创建用户
     */
    @ApiModelProperty(value = "创建用户")
    private String creator;


    private List<TicketPoint> ticketPoints;


    private List<TicketPointDevice> ticketPointDevices;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }




}
