package com.zans.mms.vo.alert;

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
public class AlertIpClashRespVO {

    private String businessId;
    private String nasIpAddress;
    private String nasPortId;
    private String aliveLastTime;
    private String username;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

