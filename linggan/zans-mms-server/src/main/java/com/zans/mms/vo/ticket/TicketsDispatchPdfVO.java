package com.zans.mms.vo.ticket;

import com.zans.mms.model.BaseVfs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@ApiModel(value = "TicketsDispatchPdfVO", description = "派工单pdf实体")
@Data
public class TicketsDispatchPdfVO {

    @ApiModelProperty(value = "工单号")
    private String ticketCode;

    @ApiModelProperty(value = "实际工单号")
    private String ticketCodeResult;

    @ApiModelProperty(value = "工单id")
    private Long id;

    @ApiModelProperty(value = "派工申请日期")
    private String createTime;

    @ApiModelProperty(value = "派工来源")
    private String issueSource;

    @ApiModelProperty(value = "施工单位")
    private String orgName;

    @ApiModelProperty(value = "区域")
    private String areaName;

    @ApiModelProperty(value = "工程类别")
    private String deviceTypeName;

    @ApiModelProperty(value = "工程所在路口")
    private String pointName;

    @ApiModelProperty(value = "责任")
    private String dutyContact;

    @ApiModelProperty(value = "预算总金额")
    private String predictCost;

    @ApiModelProperty(value = "验收单预算总金额")
    private String acceptPredictCost;

    @ApiModelProperty(value = "工程验收金额")
    private String adjustCost;

    @ApiModelProperty(value = "维护人")
    private String maintainPerson;

    @ApiModelProperty(value = "工程内容")
    private String projectContent;

    @ApiModelProperty(value = "监理单位意见")
    private String supervisorIdea;

    @ApiModelProperty(value = "处项目负责人")
    private String bureauBelonger;

    @ApiModelProperty(value = "限定时间")
    private String endTime;

    @ApiModelProperty(value = "是否紧急")
    private String runNow;

    @ApiModelProperty(value = "处分管领导")
    private String bureau1Idea;

    @ApiModelProperty(value = "处主管领导")
    private String bureau2Idea;

    @ApiModelProperty(value = "局领导意见")
    private String bureau3Idea;

    @ApiModelProperty(value = "派工反馈")
    private String dispatchFeedback;

    @ApiModelProperty(value = "remark")
    private String remark;

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

    @ApiModelProperty(value = "baseMFPdfRespVOList")
    private List<TicketBaseMfRespVO> baseMfPdfRespVOList;

    @ApiModelProperty(value = "vfsList")
    private List<BaseVfs> vfsList;

    @ApiModelProperty(value = "ticketFlowList")
    private List<TicketFlowRespVO> ticketFlowList;


    /**
     * 派单时间
     */
    private String dispatchTime;

    /**
     * 验收时间
     */
    private String acceptTime;

    /**
     * 负责单位联系人
     */
    private String applyContact;

    /**
     * 是否合并工单
     */
    private Integer isMerge;

    /**
     * 委托说明
     */
    private String acceptanceInstructions;


}
