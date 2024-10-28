package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;


/**
 * @author beixing
 * @Title: (DeviceFeature)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-19 14:22:33
 */
@Data
public class DeviceFeatureVO implements Serializable {
    private static final long serialVersionUID = -13672059865116858L;
    private Integer id;
    private String ip;
    private Integer deviceType;
    private String brand;
    private String model;
    private String webVersionUrl;
    private String webVersion;
    private String build;
    private String buildYear;
    private String homeUrl;
    private String homeContent;
    private String xhrVersion;
    private String jsDevice;
    private String jsWebSdk;
    private String jsWebAuth;
    private String jsResponse;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
