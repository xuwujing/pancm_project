package com.zans.mms.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: (AssetDiagnosticThreshold)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-26 16:52:28
 */
@Data
@Table(name = "asset_diagnostic_threshold")
public class AssetDiagnosticThreshold implements Serializable {
    private static final long serialVersionUID = 400841300339007399L;
    @Column(name = "id")
    private Long id;
    @Column(name = "device_id")
    private String deviceId;
    @Column(name = "ipAddress")
    private String ipaddress;
    @Column(name = "fault_type")
    private Integer faultType;
    @Column(name = "fault_type_name")
    private String faultTypeName;
    @Column(name = "fault_type_threshold")
    private Integer faultTypeThreshold;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
