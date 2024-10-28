package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备型号关联表(HikFeatureModelRelevance)实体类
 *
 * @author beixing
 * @since 2021-07-15 14:33:07
 */
@Data
public class HikFeatureModelRelevance implements Serializable {
    private static final long serialVersionUID = -53117947669513178L;
    private Long id;
    /**
     * 业务主键
     */
    private String businessId;
    /**
     * 设备型号
     */
    private String model;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
