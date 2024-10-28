package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 设备型号关联表(HikFeatureModelRelevance)实体类
 *
 * @author beixing
 * @since 2021-07-15 14:33:07
 */
@Data
@Table(name = "hik_feature_model_relevance")
public class HikFeatureModelRelevance implements Serializable {
    private static final long serialVersionUID = -11769749591925332L;
    @Column(name = "id")
    private Long id;
    /**
     * 业务主键
     */
    @Column(name = "business_id")
    private String businessId;
    /**
     * 设备型号
     */
    @Column(name = "model")
    private String model;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
