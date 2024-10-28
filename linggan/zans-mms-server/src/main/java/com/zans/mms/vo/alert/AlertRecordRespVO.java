package com.zans.mms.vo.alert;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class AlertRecordRespVO {



    @ApiModelProperty(name = "alertLevel",value = "告警级别")
    private Integer alertLevel;


    @ApiModelProperty(name = "noticeInfo",value = "告警内容")
    private String noticeInfo;


    @ApiModelProperty(name = "noticeTime",value = "告警时间")
    private String noticeTime;


    private String ipAddr;


    /**
     *  以下来源设备信息查询
     */


    @ApiModelProperty(name = "area_name", value = "所属区域")
    private String areaName;






    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
