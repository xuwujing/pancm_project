package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: (DeviceFeature)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-19 14:22:34
 */
@Data
@Table(name = "device_feature")
public class DeviceFeature implements Serializable {
    private static final long serialVersionUID = 958776822006816852L;
    @Column(name = "id")
    private Integer id;
    @Column(name = "ip")
    private String ip;
    @Column(name = "device_type")
    private Integer deviceType;
    @Column(name = "brand")
    private String brand;
    @Column(name = "model")
    private String model;
    @Column(name = "web_version_url")
    private String webVersionUrl;
    @Column(name = "web_version")
    private String webVersion;
    @Column(name = "build")
    private String build;
    @Column(name = "build_year")
    private String buildYear;
    @Column(name = "home_url")
    private String homeUrl;
    @Column(name = "home_content")
    private String homeContent;
    @Column(name = "xhr_version")
    private String xhrVersion;
    @Column(name = "js_device")
    private String jsDevice;
    @Column(name = "js_web_sdk")
    private String jsWebSdk;
    @Column(name = "js_web_auth")
    private String jsWebAuth;
    @Column(name = "js_response")
    private String jsResponse;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
