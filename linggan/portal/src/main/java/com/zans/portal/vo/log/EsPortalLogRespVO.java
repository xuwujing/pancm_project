package com.zans.portal.vo.log;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class EsPortalLogRespVO {

    @ApiModelProperty(name = "traceId",value = "追踪ID")
    private Long traceId;

    @ApiModelProperty(name = "message",value = "消息内容")
    private String message;

    @ApiModelProperty(name = "content",value = "内容")
    private String content;

    @ApiModelProperty(name = "logTime",value = "日志时间")
    @JSONField(name = "log_time")
    private String logTime;



}
