package com.zans.mms.vo.area;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AreaSearchVO extends BasePage {

    @ApiModelProperty(name = "area_name", value = "区域名称")
    @JSONField(name = "area_name")
    private String areaName;

}
