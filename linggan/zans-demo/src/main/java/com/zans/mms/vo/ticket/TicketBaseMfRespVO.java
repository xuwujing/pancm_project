package com.zans.mms.vo.ticket;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author pancm
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/3/4
 */
@Data
public class TicketBaseMfRespVO implements Serializable {
    private static final long serialVersionUID = -27427630671959084L;

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


    private Integer amount;

    private Integer adjAmount;

    /** 是否进行过调整 0否，1是*/
    private Integer isAdj;

    private BigDecimal predictPrice;

    private BigDecimal adjustPrice;






}
