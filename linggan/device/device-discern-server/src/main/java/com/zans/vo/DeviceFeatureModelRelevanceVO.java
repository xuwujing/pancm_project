package com.zans.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * @author beixing
 * @Title: 设备型号关联表(DeviceFeatureModelRelevance)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 15:14:57
 */
@Data
public class DeviceFeatureModelRelevanceVO implements Serializable {
    private static final long serialVersionUID = 466655231417125008L;
    private Long id;
    /**
     * 业务主键
     */
    private String businessId;
    /**
     * xml
     */
    private String xhrVersion;
    /**
     * 最大ip
     */
    private String maxIp;
    /**
     * 最小ip
     */
    private String minIp;
    /**
     * 设备型号
     */
    private String model;
    /**
     * 条数
     */
    private Integer count;
    /**
     * 1,不太肯定;2，分析确认;3标记确认
     */
    private Integer status;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
