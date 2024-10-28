package com.zans.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * @author beixing
 * @Title: 设备型号关联扩展表(DeviceFeatureModelRelevanceEx)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 15:22:17
 */
@Data
public class DeviceFeatureModelRelevanceExVO implements Serializable {
    private static final long serialVersionUID = -57145193574170790L;
    private Long id;
    /**
     * 业务主键
     */
    private String businessId;
    private String xhrVersion;
    /**
     * 最大ip
     */
    private String maxIp;
    /**
     * 最小IP
     */
    private String minIp;
    /**
     * 设备型号
     */
    private String model;
    /**
     * 条数
     */
    private Integer count;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
