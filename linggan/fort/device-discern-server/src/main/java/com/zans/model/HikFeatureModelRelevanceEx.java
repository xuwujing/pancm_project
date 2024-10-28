package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: 设备型号关联扩展表(HikFeatureModelRelevanceEx)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-15 16:27:30
 */
@Data
@Table(name = "hik_feature_model_relevance_ex")
public class HikFeatureModelRelevanceEx implements Serializable {
    private static final long serialVersionUID = -47951227896618416L;
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
