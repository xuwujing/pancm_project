package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import com.zans.mms.model.BaseVfs;
import com.zans.mms.model.Ticket;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 工单验收单详情(TicketsDispatchViewRespVO)实体类
 *
 * @author beixing
 * @since 2021-01-16 12:20:44
 */
@ApiModel(value = "ticketsAcceptViewRespVO", description = "工单结算表")
@Data
public class TicketAcceptViewRespVO implements Serializable {
    private static final long serialVersionUID = -27477630671959084L;

    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 工单编码
     */
    @ApiModelProperty(value = "工单编码")
    private String ticketCode;


    /**
     * 实际工单编码
     */
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
     * 派工预算金额
     */
    @ApiModelProperty(value = "派工预算金额")
    private Double predictCost;

    /**
     * 验收核算金额
     */
    @ApiModelProperty(value = "验收核算金额")
    private Double acceptAdjustCost;

    /**
     * 验收核算金额
     */
    @ApiModelProperty(value = "验收核算金额")
    private Double acceptPredictCost;
    /**
     * 分配单位代码
     */
    @ApiModelProperty(value = "分配单位代码")
    private String allocDepartmentNum;

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

    private String acceptStatusName;

    private String dispatchStatusName;
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


    @ApiModelProperty(value = "点位id")
    private Long pointId;

    @ApiModelProperty(value = "责任人")
    private String dutyContact;

    private List<TicketBaseMfRespVO> baseMfRespVOList;

    private List<BaseVfs> adjunctList;

    private String pointName;

    private String acceptAdjunctId;

    /**
     * 派单时间
     */
    private String dispatchTime;


    /**
     * 验收时间
     */
    private String acceptTime;


    /**
     * 是否合并工单
     */
    private Integer isMerge;

    private Long pid;

    private String pCode;

    private String mark;

    private Map<String,Object> baseMfRespVOMap;

    private List<Ticket> childList;

    private Long patrolCheckResultId;

    private String createTime;

    private Integer dealWay;


    /**
     * 委托内容
     */
    private String acceptanceInstructions;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
