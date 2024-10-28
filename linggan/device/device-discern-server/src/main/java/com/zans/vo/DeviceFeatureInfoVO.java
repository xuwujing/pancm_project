package com.zans.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * @author beixing
 * @Title: 设备统计表(DeviceFeatureInfo)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-30 14:15:32
 */
@Data
public class DeviceFeatureInfoVO implements Serializable {
    private static final long serialVersionUID = -90834938180101879L;
    private Long id;
    /**
     * ip地址
     */
    private String ip;
    private String businessId;
    /**
     * 设备类型
     */
    private Integer deviceType;
    /**
     * 设备型号
     */
    private String model;
    /**
     * 实际的deviceType
     */
    private Integer realityDeviceType;
    /**
     * 实际的型号
     */
    private String realityModel;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
