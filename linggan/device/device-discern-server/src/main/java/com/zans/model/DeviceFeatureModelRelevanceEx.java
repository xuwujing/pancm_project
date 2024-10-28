package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: 设备型号关联扩展表(DeviceFeatureModelRelevanceEx)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 15:22:19
 */
@Data
@Table(name = "device_feature_model_relevance_ex")
public class DeviceFeatureModelRelevanceEx implements Serializable {
    private static final long serialVersionUID = -91931507966827289L;
    @Column(name = "id")
    private Long id;
    /**
     * 业务主键
     */
    @Column(name = "business_id")
    private String businessId;
    @Column(name = "xhr_version")
    private String xhrVersion;
    /**
     * 最大ip
     */
    @Column(name = "max_ip")
    private String maxIp;
    /**
     * 最小IP
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
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
