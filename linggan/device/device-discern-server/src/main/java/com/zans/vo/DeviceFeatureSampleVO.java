package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;


/**
 * @author beixing
 * @Title: 设备型号样本表(DeviceFeatureSample)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-21 15:45:14
 */
@Data
public class DeviceFeatureSampleVO implements Serializable {
    private static final long serialVersionUID = 670864793707475888L;
    private Long id;
    /**
     * 业务主键
     */
    private String businessId;
    private String businessIdKey;

    /**
     * 型号
     */
    private String model;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 1,不太肯定;2，分析确认;3标记确认
     */
    private Integer modelStatus;
    /**
     * 1,不太肯定;2，分析确认;3标记确认
     */
    private Integer deviceTypeStatus;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
