package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


@Data
public class JobReqVO {

    private Integer job_id;

    private String job_name;

    private Integer enable;


    private Integer priority;

    private Integer sharding;

    private Integer sharding_num;

    private String cronExpression;

    private Integer timeout;


    private String job_status;

    private String job_type;


    private String remark;

    private String job_data;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
