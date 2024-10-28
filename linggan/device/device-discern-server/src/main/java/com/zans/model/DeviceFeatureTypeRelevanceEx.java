package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: 设备类型关联扩展表(DeviceFeatureTypeRelevanceEx)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 15:37:38
 */
@Data
@Table(name = "device_feature_type_relevance_ex")
public class DeviceFeatureTypeRelevanceEx implements Serializable {
    private static final long serialVersionUID = -24842906525142509L;
    @Column(name = "id")
    private Long id;
    /**
     * 业务主键
     */
    @Column(name = "business_id")
    private String businessId;
    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private String deviceType;
    @Column(name = "max_ip")
    private String maxIp;
    @Column(name = "min_ip")
    private String minIp;
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
