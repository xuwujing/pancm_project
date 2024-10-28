package com.zans.mms.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 工单表(Tickets)实体类
 *
 * @author beixing
 * @since 2021-03-02 15:00:26
 */
@ApiModel(value = "Ticket", description = "工单表")
@Data
@Table(name = "ticket")
public class Ticket implements Serializable {
    private static final long serialVersionUID = 178751449409588462L;

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "自增id")
    private Long id;
    /**
     * 工单编码
     */
    @Column(name = "ticket_code")
    @ApiModelProperty(value = "工单编码")
    private String ticketCode;
    /**
     * 工单类型
     */
    @Column(name = "ticket_type")
    @ApiModelProperty(value = "工单类型")
    private Integer ticketType;
    /**
     * 辖区id
     */
    @Column(name = "area_id")
    @ApiModelProperty(value = "辖区id")
    private String areaId;
    /**
     * 设备类型
     */
    @Column(name = "device_type")
    @ApiModelProperty(value = "设备类型")
    private String deviceType;
    /**
     * 故障类型
     */
    @Column(name = "issue_type")
    @ApiModelProperty(value = "故障类型")
    private Integer issueType;
    /**
     * 故障级别
     */
    @Column(name = "issue_level")
    @ApiModelProperty(value = "故障级别")
    private Integer issueLevel;
    /**
     * 故障来源
     */
    @Column(name = "issue_source")
    @ApiModelProperty(value = "故障来源")
    private Integer issueSource;

    /**
     * 联系人姓名
     */
    @Column(name = "apply_contact")
    @ApiModelProperty(value = "联系人姓名")
    private String applyContact;
    /**
     * 联系人电话
     */
    @Column(name = "apply_phone")
    @ApiModelProperty(value = "联系人电话")
    private String applyPhone;

    @Column(name = "duty_contact")
    @ApiModelProperty(value = "责任人")
    private String dutyContact;
    /**
     * 故障发生时间
     */
    @Column(name = "occurred_time")
    @ApiModelProperty(value = "故障发生时间")
    private String occurredTime;
    /**
     * 派工预算金额
     */
    @Column(name = "predict_cost")
    @ApiModelProperty(value = "派工预算金额")
    private Double predictCost;
    /**
     * 验收预算金额
     */
    @Column(name = "accept_predict_cost")
    @ApiModelProperty(value = "验收预算金额")
    private Double acceptPredictCost;
    /**
     * 验收核算金额
     */
    @Column(name = "accept_adjust_cost")
    @ApiModelProperty(value = "验收核算金额")
    private Double acceptAdjustCost;
    /**
     * 分配单位代码
     */
    @Column(name = "alloc_department_num")
    @ApiModelProperty(value = "分配单位代码")
    private String allocDepartmentNum;
    /**
     * 预计抵达时间
     */
    @Column(name = "predict_arrive_time")
    @ApiModelProperty(value = "预计抵达时间")
    private String predictArriveTime;
    /**
     * 预计完成时间
     */
    @Column(name = "predict_complete_time")
    @ApiModelProperty(value = "预计完成时间")
    private String predictCompleteTime;

    @ApiModelProperty(value = "预计抵达")
    @Column(name = "predict_arrive")
    private Integer predictArrive;
    /**
     * 预计完成时间
     */
    @ApiModelProperty(value = "预计完成")
    @Column(name = "predict_complete")
    private Integer predictComplete;

    /**
     * 实际抵达时间
     */
    @Column(name = "practical_arrive_time")
    @ApiModelProperty(value = "实际抵达时间")
    private String practicalArriveTime;
    /**
     * 实际完成时间
     */
    @Column(name = "practical_complete_time")
    @ApiModelProperty(value = "实际完成时间")
    private String practicalCompleteTime;
    /**
     * 实际故障类型
     */
    @Column(name = "practical_issue_type")
    @ApiModelProperty(value = "实际故障类型")
    private Integer practicalIssueType;
    /**
     * 附件编号
     */
    @Column(name = "adjunct_num")
    @ApiModelProperty(value = "附件编号")
    private String adjunctId;
    /**
     * 验收附件编号
     */
    @Column(name = "accept_adjunct_id")
    @ApiModelProperty(value = "验收附件编号")
    private String acceptAdjunctId;
    /**
     * 工单状态
     */
    @Column(name = "ticket_status")
    @ApiModelProperty(value = "工单状态")
    private Integer ticketStatus;
    /**
     * 派工状态
     */
    @Column(name = "dispatch_status")
    @ApiModelProperty(value = "派工状态")
    private Integer dispatchStatus;
    /**
     * 验证状态
     */
    @Column(name = "accept_status")
    @ApiModelProperty(value = "验证状态")
    private Integer acceptStatus;

    @Column(name = "maintenance_status")
    @ApiModelProperty(value = "维修状态")
    private Integer maintenanceStatus;

    @Column(name = "edit_status")
    @ApiModelProperty(value = "编辑状态")
    private Integer editStatus;


    /**
     * 是否核算
     */
    @Column(name = "is_cost")
    @ApiModelProperty(value = "是否核算")
    private Integer isCost;
    /**
     * 描述
     */
    @Column(name = "remark")
    @ApiModelProperty(value = "描述")
    private String remark;
    /**
     * 创建用户
     */
    @Column(name = "creator")
    @ApiModelProperty(value = "创建用户")
    private String creator;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
