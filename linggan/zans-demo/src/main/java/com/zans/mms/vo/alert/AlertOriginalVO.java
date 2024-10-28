package com.zans.mms.vo.alert;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


@Data
public class AlertOriginalVO {


    private String keywordValue;

    private Long ruleId;

    private String businessId;

    private String userName;

    private String ipAddr;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
