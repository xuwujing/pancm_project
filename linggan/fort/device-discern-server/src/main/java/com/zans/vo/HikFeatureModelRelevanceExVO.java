package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;


/**
 * @author beixing
 * @Title: 设备型号关联扩展表(HikFeatureModelRelevanceEx)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-15 16:27:30
 */
@Data
public class HikFeatureModelRelevanceExVO implements Serializable {
    private static final long serialVersionUID = -54252006934145285L;

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
