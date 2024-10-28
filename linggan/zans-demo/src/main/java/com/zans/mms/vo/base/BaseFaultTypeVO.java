package com.zans.mms.vo.base;


import lombok.Data;

/**
 * @author wang
 */
@Data
public class BaseFaultTypeVO {

    /**
     * 故障编号
     */
    private String faultId;

    /**
     * 故障名称
     */
    private String faultName;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备名称
     */
    private String deviceName;

}
