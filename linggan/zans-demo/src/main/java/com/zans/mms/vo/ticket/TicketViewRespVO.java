package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import com.zans.mms.model.BaseVfs;
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
@ApiModel
@Data
public class TicketViewRespVO  implements Serializable {
    private static final long serialVersionUID = -95213275681902737L;

    @ApiModelProperty(value = "自增ID")
    private Long id;
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
     * 故障发生时间
     */
    @ApiModelProperty(value = "故障发生时间")
    private String occurredTime;
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
     * 故障级别
     */
    @ApiModelProperty(value = "故障级别")
    private Integer issueLevel;
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
     * 派工预算金额
     */
    @ApiModelProperty(value = "派工预算金额")
    private Double predictCost;
    /**
     * 验收预算金额
     */
    @ApiModelProperty(value = "验收预算金额")
    private Double acceptPredictCost;
    /**
     * 验收核算金额
     */
    @ApiModelProperty(value = "验收核算金额")
    private Double acceptAdjustCost;
    /**
     * 分配单位代码
     */
    @ApiModelProperty(value = "分配单位代码")
    private String allocDepartmentNum;
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
    /**
     * 实际抵达时间
     */
    @ApiModelProperty(value = "实际抵达时间")
    private String practicalArriveTime;
    /**
     * 实际完成时间
     */
    @ApiModelProperty(value = "实际完成时间")
    private String practicalCompleteTime;

    /**
     * 实际故障类型
     */
    @ApiModelProperty(value = "实际故障类型")
    private Integer practicalIssueType;
    /**
     * 附件编号
     */
    @ApiModelProperty(value = "附件编号")
    private String adjunctId;
    /**
     * 验收附件编号
     */
    @ApiModelProperty(value = "验收附件编号")
    private String acceptAdjunctId;
    /**
     * 工单状态
     */
    @ApiModelProperty(value = "工单状态")
    private Integer ticketStatus;
    /**
     * 派工状态
     */
    @ApiModelProperty(value = "派工状态")
    private Integer dispatchStatus;
    /**
     * 验证状态
     */
    @ApiModelProperty(value = "验证状态")
    private Integer acceptStatus;

    @ApiModelProperty(value = "维修状态")
    private Integer maintenanceStatus;
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
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    private String dutyContact;


    private List<BaseVfs> adjunctList;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }


}
