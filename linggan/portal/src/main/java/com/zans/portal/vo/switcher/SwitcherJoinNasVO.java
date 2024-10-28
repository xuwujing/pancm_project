package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class SwitcherJoinNasVO {

    private Integer id;

    @ApiModelProperty(name = "name", value = "接入点描述")
    @JSONField(name = "name")
    private String name;


    @ApiModelProperty(name = "delete_status", value = "删除状态")
    @JSONField(name = "delete_status")
    private Integer deleteStatus;

    @ApiModelProperty(name = "nas_ip", value = "nas ip")
    @JSONField(name = "nas_ip")
    private String nasIp;

    @ApiModelProperty(name = "secret", value = "nas密码")
    @JSONField(name = "secret")
    private String secret;

    @ApiModelProperty(name = "short_name", value = "nas简称")
    @JSONField(name = "short_name")
    private String shortName;

    @ApiModelProperty(name = "nas_type", value = "nas类型")
    @JSONField(name = "nas_type")
    private String nasType;

}
