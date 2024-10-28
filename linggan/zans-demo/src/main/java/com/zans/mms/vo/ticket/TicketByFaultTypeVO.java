package com.zans.mms.vo.ticket;

import com.zans.mms.vo.asset.AssetResVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(value = "ticketByFaultTypeVO", description = "用于接收通过故障类型统计数据")
@Data
public class TicketByFaultTypeVO {

    /**
     * 故障名称
     */
   private String faultName;

    /**
     * 本月统计
     */
    private Integer monthCount;

    /**
     * 总计
     */
    private Integer totalCount;



}
