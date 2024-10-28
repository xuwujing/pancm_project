package com.zans.portal.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;


@ApiModel
@Data
public class AcctReqVO extends BasePage {

    @ApiModelProperty(name = "username", value = "mac地址", required = true)
    @JSONField(name = "username")
    @NotNull
    private String username;

    @ApiModelProperty(name = "nas_ip_address", value = "nas ip")
    @JSONField(name = "nas_ip_address")
    private String nasIpAddress;


}
