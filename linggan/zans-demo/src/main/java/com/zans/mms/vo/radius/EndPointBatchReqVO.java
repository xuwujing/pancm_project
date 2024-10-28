package com.zans.mms.vo.radius;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@ApiModel
@Data
public class EndPointBatchReqVO  {

    @ApiModelProperty(name = "id", value = "id")
    @NotNull
    private String ids;

    @ApiModelProperty(name = "policy", value = "policy")
    private Integer policy;


    @ApiModelProperty(name = "authMark", value = "authMark")
    private String authMark;

}
