package com.zans.portal.vo.alert;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author pancm
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/12/9
 */
@Data
public class AlertDetailRespVO {

    private Long ruleId;

    private String businessId;

    private String mac;

    private String brandName;

    private String ipAddr;

    private String pointName;

    private String deviceTypeName;

    private String modelDes;

    private String nasIpAddress;

    private String nasPortId;

    private String swPointName;



    private String swTypeName;

    private String vlan;

    private String projectName;

    private String contractor;

    private String contractorPerson;

    private String contractorPhone;

    private String createTime;

    private String updateTime;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
