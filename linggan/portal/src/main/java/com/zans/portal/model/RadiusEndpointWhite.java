package com.zans.portal.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author beixing
 * @Title: 设备白名单表(RadiusEndpointWhite)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022-02-16 18:22:04
 */
@Data
@Table(name = "radius_endpoint_white")
public class RadiusEndpointWhite implements Serializable {
    private static final long serialVersionUID = 132541233303977309L;
    @Column(name = "id")
    private Long id;
    /**
     * 设备名称
     */
    @Column(name = "device_name")
    private String deviceName;
    /**
     * mac
     */
    @Column(name = "mac")
    private String mac;
    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private Integer deviceType;
    /**
     * 添加人
     */
    @Column(name = "creator")
    private String creator;
    /**
     * 添加时间
     */
    @Column(name = "create_time")
    private String createTime;

    @Column(name = "device_type_name")
    private String deviceTypeName;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
