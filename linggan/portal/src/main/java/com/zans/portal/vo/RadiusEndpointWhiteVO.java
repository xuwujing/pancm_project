package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import lombok.Data;

import java.io.Serializable;


/**
 * @author beixing
 * @Title: 设备白名单表(RadiusEndpointWhite)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022-02-16 18:22:01
 */
@Data
public class RadiusEndpointWhiteVO extends BasePage implements Serializable {
    private static final long serialVersionUID = 105847460567470103L;
    private Long id;
    /**
     * 设备名称
     */
    private String deviceName;

    private String deviceTypeName;
    /**
     * mac
     */
    private String mac;
    /**
     * 设备类型
     */
    private Integer deviceType;
    /**
     * 添加人
     */
    private String creator;
    /**
     * 添加时间
     */
    private String createTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
