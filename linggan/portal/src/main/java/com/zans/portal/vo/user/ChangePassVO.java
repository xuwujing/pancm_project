package com.zans.portal.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="ChangePassVO修改密码",description="修改密码请求")
public class ChangePassVO {

    @NotNull(message="用户id必填")
    @ApiModelProperty(value = "用户id",required = true)
    private Integer id;

    @Pattern(regexp = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9]+$)(?![a-z\\W_!@#$%^&*`~()-+=]+$)(?![0-9\\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\\W_!@#$%^&*`~()-+=]{6,10}$",message = "长度6-10位，支持数字、字母大小写、特殊字符")
    @ApiModelProperty(value = "密码，长度6-10位，支持数字、字母大小写、特殊字符;不能有连续4位重复或者连续字符",required = true)
    private String password;

}
