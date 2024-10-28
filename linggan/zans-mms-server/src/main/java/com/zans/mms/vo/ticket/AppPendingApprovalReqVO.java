package com.zans.mms.vo.ticket;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* @Title: AppPendingApprovalReqVO
* @Description: app待审批列表、已审批列表、全部列表ReqVO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 5/11/21
*/
@ApiModel(value = "AppPendingApprovalReqVO", description = "")
@Data
public class AppPendingApprovalReqVO extends BasePage implements Serializable {

    /**
     * 1.全部；2.待审批；3.已审批
     */
    @ApiModelProperty(value = "1.全部；2.待审批；3.已审批",name = "1.全部；2.待审批；3.已审批")
    private Integer approvalType;

    @ApiModelProperty(value = "类型 全部/派工单/验收单")
    private String type;

    @ApiModelProperty(value = "分配单位")
    private String allocDepartmentNum;

    @ApiModelProperty(value = "金额")
    private BigDecimal cost;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;


    ///todo 添加筛选  2021/10/14 15：17 金额/单位/工单类型 时间排序

    @ApiModelProperty(value = "金额类型 1：3000以下 2：3000-10000 3：10000以上")
    private Integer costType;

    @ApiModelProperty(value = "审批人")
    private String assign;

    /**
     * 流程id列表
     */
    private List<String> workflowIdList;

    private String keyword;

}
