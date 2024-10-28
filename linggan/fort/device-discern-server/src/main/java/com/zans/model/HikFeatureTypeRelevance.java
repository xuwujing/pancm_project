package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 设备类型关联表(HikFeatureTypeRelevance)实体类
 *
 * @author beixing
 * @since 2021-07-15 14:33:08
 */
@Data
@Table(name = "hik_feature_type_relevance")
public class HikFeatureTypeRelevance implements Serializable {
    private static final long serialVersionUID = 154657370243389547L;
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
