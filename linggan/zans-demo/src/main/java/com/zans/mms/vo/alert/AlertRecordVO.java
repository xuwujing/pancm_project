package com.zans.mms.vo.alert;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


@Data
public class AlertRecordVO {


    private String keywordValue;

    private Long ruleId;

    private String businessId;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
