package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author beixing
 * @Title: (HikFeature)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-14 11:04:09
 */
@Data
public class HikFeature implements Serializable {
    private static final long serialVersionUID = -56219171705471602L;
    @Column(name = "id")
    private Integer id;
    @Column(name = "ip")
    private String ip;
    @Column(name = "device_type")
    private String deviceType;
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
