package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
* @Title: AppPendingApprovalRespVO
* @Description: app待审批列表、已审批列表、全部列表RespVO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 5/11/21
*/
@ApiModel(value = "AppPendingApprovalRespVO", description = "")
@Data
public class AppPendingApprovalRespVO implements Serializable {


    /**
     * 流程文件  派工单 dispatch / 派工单 acceptance
     */
    @ApiModelProperty(value = "流程文件 派工单 dispatch / 派工单 acceptance")
    private String workflowStr;


    @ApiModelProperty(value = "自增id")
    private Long id;
    /**
     * 工单编码
     */
    @ApiModelProperty(value = "工单编码",name = "ticketCode")
    private String ticketCode;

    private String ticketCodeResult;
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

    @ApiModelProperty(value = "责任人")
    private String dutyContact;
    /**
     * 故障发生时间
     */
    @ApiModelProperty(value = "故障发生时间")
    private String occurredTime;
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
     * 实际故障类型
     */
    @ApiModelProperty(value = "实际故障类型")
    private Integer practicalIssueType;

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

    @ApiModelProperty(value = "编辑状态")
    private Integer editStatus;


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
     * 验收单状态名称
     */
    private String acceptanceStatusName;

    /**
     * 派工单状态名称
     */
    private String dispatchStatusName;


    private String backDispatchRoleName;


    private String backAcceptanceRoleName;

    /**
     * 是否合并工单
     */
    private Integer isMerge;

    private String acceptanceInstructions;
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
