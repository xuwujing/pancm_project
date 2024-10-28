package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工单派工单详情(TicketsDispatchViewRespVO)实体类
 *
 * @author beixing
 * @since 2021-01-16 12:20:44
 */
@ApiModel(value = "ticketDispatchViewEditVO", description = "工单结算表")
@Data
public class TicketDispatchViewEditVO implements Serializable {
    private static final long serialVersionUID = -27477630671959084L;

    @ApiModelProperty(value = "id")
    private Long id;

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
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "责任人")
    private String dutyContact;

    private Integer opType;

    private String opPlatform;
    /**
     * ip地址
     */
    private String opIpaddr;
    /**
     * 创建者
     */
    private String creator;

    private Integer ticketStatus;
    /**
     * 派工状态
     */
    private Integer dispatchStatus;
    /**
     * 验证状态
     */
    private Integer acceptStatus;

    private String adjunctId;

    /**
     * 派单编号
     */
    private String ticketCodeResult;

    /**
     * 派单时间
     */
    private String dispatchTime;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
