package com.zans.mms.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (TicketDetail)实体类
 *
 * @author beixing
 * @since 2021-03-05 18:13:55
 */
@ApiModel(value = "TicketDetail", description = "")
@Data
@Table(name = "ticket_detail")
public class TicketDetail implements Serializable {
    private static final long serialVersionUID = -22114251791602257L;
    /**
     * 自增主键
     */
    @Column(name = "id")
    @ApiModelProperty(value = "自增主键")
    private Long id;
    /**
     * 工单id
     */
    @Column(name = "ticket_id")
    @ApiModelProperty(value = "工单id")
    private Long ticketId;
    /**
     * 单位价格id
     */
    @Column(name = "facility_id")
    @ApiModelProperty(value = "单位价格id")
    private Long facilityId;
    /**
     * 数量
     */
    @Column(name = "amount")
    @ApiModelProperty(value = "数量")
    private BigDecimal amount;
    /**
     * 调整数量
     */
    @Column(name = "adj_amount")
    @ApiModelProperty(value = "调整数量")
    private BigDecimal adjAmount;
    /**
     * 预算金额
     */
    @Column(name = "predict_price")
    @ApiModelProperty(value = "预算金额")
    private BigDecimal predictPrice;
    /**
     * 核算金额
     */
    @Column(name = "adjust_price")
    @ApiModelProperty(value = "核算金额")
    private BigDecimal adjustPrice;
    /**
     * 单据类型
     */
    @Column(name = "type")
    @ApiModelProperty(value = "单据类型")
    private Integer type;
    /**
     * 是否调整
     */
    @Column(name = "is_adj")
    @ApiModelProperty(value = "是否调整")
    private Integer isAdj;
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
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
