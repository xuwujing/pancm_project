package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: 设备型号关联表(DeviceFeatureModelRelevance)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 15:14:58
 */
@Data
@Table(name = "device_feature_model_relevance")
public class DeviceFeatureModelRelevance implements Serializable {
    private static final long serialVersionUID = 264917220945741991L;
    @Column(name = "id")
    private Long id;
    /**
     * 业务主键
     */
    @Column(name = "business_id")
    private String businessId;
    /**
     * xml
     */
    @Column(name = "xhr_version")
    private String xhrVersion;
    /**
     * 最大ip
     */
    @Column(name = "max_ip")
    private String maxIp;
    /**
     * 最小ip
     */
    @Column(name = "min_ip")
    private String minIp;
    /**
     * 设备型号
     */
    @Column(name = "model")
    private String model;
    /**
     * 条数
     */
    @Column(name = "count")
    private Integer count;
    /**
     * 1,不太肯定;2，分析确认;3标记确认
     */
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
