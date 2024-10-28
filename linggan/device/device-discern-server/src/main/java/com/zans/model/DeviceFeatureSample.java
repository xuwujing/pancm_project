package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author beixing
 * @Title: 设备型号样本表(DeviceFeatureSample)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-21 15:45:16
 */
@Data
@Table(name = "device_feature_sample")
public class DeviceFeatureSample implements Serializable {
    private static final long serialVersionUID = 108466355228796967L;
    @Column(name = "id")
    private Long id;
    /**
     * 业务主键
     */
    @Column(name = "business_id")
    private String businessId;

    @Column(name = "business_id_key")
    private String businessIdKey;

    /**
     * 型号
     */
    @Column(name = "model")
    private String model;
    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private String deviceType;
    /**
     * 1,不太肯定;2，分析确认;3标记确认
     */
    @Column(name = "model_status")
    private Integer modelStatus;
    /**
     * 1,不太肯定;2，分析确认;3标记确认
     */
    @Column(name = "device_type_status")
    private Integer deviceTypeStatus;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
