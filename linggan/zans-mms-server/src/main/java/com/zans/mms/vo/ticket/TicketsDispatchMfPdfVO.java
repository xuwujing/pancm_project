package com.zans.mms.vo.ticket;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketsDispatchMfPdfVO extends TicketBaseMfRespVO {

    /**
     * 设备ID
     */
    private Long id;


    /**
     * 设备编号,excel中的设备ID
     */
    private String deviceCode;

    /**
     * 设备价格D
     */
    private BigDecimal devicePrice;


    private String deviceName;


    private String deviceSpec;

    private String deviceModel;


    private String deviceBrand;


    private BigDecimal amount;

    private BigDecimal adjAmount;

    /** 是否进行过调整 0否，1是*/
    private Integer isAdj;

    private BigDecimal predictPrice;

    private BigDecimal adjustPrice;




}
