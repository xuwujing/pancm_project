package com.zans.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "UpdatePasswordReqVO", description = "")
@Data
public class UpdatePasswordReqVO {

    @NotBlank
    @ApiModelProperty(value = "id")
    private Integer id;


    @NotBlank
    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
