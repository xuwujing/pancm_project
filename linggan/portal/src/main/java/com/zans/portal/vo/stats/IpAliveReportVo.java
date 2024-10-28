package com.zans.portal.vo.stats;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class IpAliveReportVo {

    @ApiModelProperty(name = "date", value = "横坐标时间")
    @JSONField(name = "date")
    private String[] date;

    @ApiModelProperty(name = "value", value = "纵坐标值")
    @JSONField(name = "value")
    private String[] value;

}
