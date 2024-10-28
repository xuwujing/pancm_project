package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import com.zans.mms.model.BaseVfs;
import com.zans.mms.model.Ticket;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel
@Data
public class TicketSearchRespVO {

    private static final long serialVersionUID = -22713165493473764L;
    @ApiModelProperty(value = "工单id")
    private Long id;


    @ApiModelProperty(value = "工单编码")
    private String ticketCode;


    @ApiModelProperty(value = "实际工单编码")
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

    private String dispatchStatusName;
    /**
     * 验证状态
     */
    @ApiModelProperty(value = "验证状态")
    private Integer acceptStatus;

    private String acceptStatusName;

    @ApiModelProperty(value = "维修状态")
    private Integer maintenanceStatus;

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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "责任人")
    private String dutyContact;

    private String pointName;

    private List<BaseVfs> adjunctList;

    private Integer isClockIn;

    /** 剩余时间 */
    private String remainingTime;

    private String distance;

    /**
     * 能否删除 1代表是 0代表否
     */
    private Integer deleteAble;


    /**
     * 20210727拓展字段 来源需求tapd ID1000888
     */
    /**
     * 创建人姓名
     */
    private String creatorNickname;

    /**
     * 创建人角色
     */
    private String creatorRole;

    /**
     * 创建来源 1.pc 2.小程序
     */
    private String createSource;

    /**
     * 派工时间
     */
    private String dispatchTime;

    /**
     * 是否合并工单
     */
    private Integer isMerge;


    /**
     * 是否有父工单
     */
    private Long pid;

    /**
     * 父工单code
     */
    private String pCode;

    /**
     * 标记字段
     */
    private String mark;

    /**
     * 导入的点位名称
     */
    private String uploadPointName;

    /**
     * 验收时间
     */
    private String acceptTime;

    private String assetCode;

    private Long assetId;

    private Long pointId;

    private List<Long> assetIds;

    private List<Ticket> childList;

    private List<Map<Long,String>> assetList;


    /**
     * 巡检结果id
     */
    private Long patrolCheckResultId;

    /**
     * 处理方式
     */
    private Integer dealWay;

    private String creator;


    /**
     * 派工单最近审批时间
     */
    private String dispatchApprovalTime;

    /**
     * 验收单最近审批时间
     */
    private String acceptApprovalTime;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
