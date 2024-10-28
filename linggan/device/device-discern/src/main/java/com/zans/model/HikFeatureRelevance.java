package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 设备型号关联表(HikFeatureRelevance)实体类
 *
 * @author beixing
 * @since 2021-07-15 10:42:58
 */
@Data
@Table(name = "hik_feature_relevance")
public class HikFeatureRelevance implements Serializable {
    private static final long serialVersionUID = -28692281744652556L;
    @Column(name = "id")
    private Long id;
    /**
     * 业务主键
     */
    @Column(name = "business_id")
    private String businessId;
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
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
