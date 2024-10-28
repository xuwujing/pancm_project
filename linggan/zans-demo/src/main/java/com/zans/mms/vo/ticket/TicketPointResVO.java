package com.zans.mms.vo.ticket;

import com.zans.mms.vo.asset.AssetResVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(value = "ticketPointResVO", description = "")
@Data
public class TicketPointResVO {

    private String pointCode;

    private Long pointId;

    private String pointName;

    private String areaId;

    private Integer pointType;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private List<AssetResVO> deviceLists;

    private String deviceType;

}
