package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备型号关联表(HikFeatureRelevance)实体类
 *
 * @author beixing
 * @since 2021-07-15 10:42:58
 */
@Data
public class HikFeatureRelevance implements Serializable {
    private static final long serialVersionUID = 791624460656837586L;
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
