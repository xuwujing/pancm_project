package com.zans.portal.vo.area;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AreaRespVO {

    private Integer id;


    @ApiModelProperty(name = "region", value = "region")
    @JSONField(name = "region")
    private Integer region;

    @ApiModelProperty(name = "area_name", value = "区域名称")
    @JSONField(name = "area_name")
    private String areaName;

    @ApiModelProperty(name = "admin_area_name", value = "行政区划")
    @JSONField(name = "admin_area_name")
    private String adminAreaName;

}
