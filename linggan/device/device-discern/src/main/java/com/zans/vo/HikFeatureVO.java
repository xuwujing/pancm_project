package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: (HikFeature)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-14 11:06:46
 */
@Data
public class HikFeatureVO  implements Serializable {
    private static final long serialVersionUID = -96542636063111320L;
    private Integer id;
    private String ip;

    private String deviceType;
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
