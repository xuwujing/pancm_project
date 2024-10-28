package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备类型关联表(HikFeatureTypeRelevance)实体类
 *
 * @author beixing
 * @since 2021-07-15 14:33:07
 */
@Data
public class HikFeatureTypeRelevance implements Serializable {
    private static final long serialVersionUID = -83406552267814046L;
    private Long id;
    /**
     * 业务主键
     */
    private String businessId;
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
