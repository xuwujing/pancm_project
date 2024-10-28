package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author beixing
 * @Title: 设备类型关联扩展表(HikFeatureTypeRelevanceEx)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-15 16:27:54
 */
@Data
@Table(name = "hik_feature_type_relevance_ex")
public class HikFeatureTypeRelevanceEx implements Serializable {
    private static final long serialVersionUID = -87391725687677602L;
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
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
