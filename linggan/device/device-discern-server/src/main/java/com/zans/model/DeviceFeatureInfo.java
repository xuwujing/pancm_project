package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: 设备统计表(DeviceFeatureInfo)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-30 14:15:33
 */
@Data
@Table(name = "device_feature_info")
public class DeviceFeatureInfo implements Serializable {
    private static final long serialVersionUID = -66325616467987808L;
    @Column(name = "id")
    private Long id;
    /**
     * ip地址
     */
    @Column(name = "ip")
    private String ip;
    @Column(name = "business_id")
    private String businessId;
    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private Integer deviceType;
    /**
     * 设备型号
     */
    @Column(name = "model")
    private String model;
    /**
     * 实际的deviceType
     */
    @Column(name = "reality_device_type")
    private Integer realityDeviceType;
    /**
     * 实际的型号
     */
    @Column(name = "reality_model")
    private String realityModel;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
