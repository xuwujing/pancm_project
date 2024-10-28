package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: 设备类型关联扩展表(HikFeatureTypeRelevanceEx)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-15 16:27:53
 */
@Data
public class HikFeatureTypeRelevanceExVO  implements Serializable {
    private static final long serialVersionUID = -72136049661553584L;

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
