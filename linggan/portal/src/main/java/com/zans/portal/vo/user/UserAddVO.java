package com.zans.portal.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.model.TUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="User新增对象",description="User新增对象")
public class UserAddVO {

    @NotEmpty(message="用户名必填")
    @Length(max=30,message = "用户名必填小于30")
    @JSONField(name = "user_name")
    @ApiModelProperty(value = "用户姓名",required = true)
    private String userName;

    @JSONField(name = "mobile")
    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^[1][3857649][0-9]{9}|.{0}$",message = "手机号错误")
    private String mobile;

    @NotEmpty(message="真实姓名必填")
    @Length(max=20,message = "真实姓名必填小于20")
    @JSONField(name = "nick_name")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]+$",message = "真实名称只允许中文")
    @ApiModelProperty(value = "真实姓名必填",required = true)
    private String nickName;

    @NotEmpty(message="密码必填")
    @ApiModelProperty(value = "密码必填",required = true,notes = "密码，长度6-10位，支持数字、字母大小写、特殊字符")
    private String password;

    @JSONField(name = "department")
    @NotNull(message="department必填")
    @ApiModelProperty(value = "department必填",required = true)
    private Integer department;

    @NotNull(message="角色必填")
    @ApiModelProperty(value = "角色必填",required = true)
    private Integer role;

    public TUser convertToUser() {
        TUser user = new TUser();
        user.setPassword(this.password);
        user.setDepartment(this.department);
        user.setNickName(this.nickName);
        user.setUserName(this.userName);
        user.setMobile(this.mobile);
        return user;
    }
}
