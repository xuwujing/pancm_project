package com.zans.portal.vo.alert;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author pancm
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/11/20
 */
@Data
public class AlertLoopRespVO {

    private String port;
    private String port2;
    private String ipAddr;
    private String username;
    private String pointName;
    private Integer status;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

