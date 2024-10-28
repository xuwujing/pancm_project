package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备型号关联扩展表(HikFeatureRelevanceEx)实体类
 *
 * @author beixing
 * @since 2021-07-15 10:43:19
 */
@Data
public class HikFeatureRelevanceEx implements Serializable {
    private static final long serialVersionUID = -70183833088205379L;
    private Long id;
    /**
     * 业务主键
     */
    private String businessId;
    /**
     * 型号
     */
    private String model;
    /**
     * 设备类型
     */
    private String deviceType;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
