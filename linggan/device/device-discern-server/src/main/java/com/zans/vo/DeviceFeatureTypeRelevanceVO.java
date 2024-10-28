package com.zans.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * @author beixing
 * @Title: 设备类型关联表(DeviceFeatureTypeRelevance)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 15:33:57
 */
@Data
public class DeviceFeatureTypeRelevanceVO implements Serializable {
    private static final long serialVersionUID = -68771986268579349L;
    private Long id;
    /**
     * 业务主键
     */
    private String businessId;
    /**
     * 设备类型
     */
    private String deviceType;
    private String maxIp;
    private String minIp;
    private Integer status;
    /**
     * 条数
     */
    private Integer count;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
