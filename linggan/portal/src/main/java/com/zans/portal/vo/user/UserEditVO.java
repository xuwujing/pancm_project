package com.zans.portal.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.model.TUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="User编辑对象",description="User编辑对象")
public class UserEditVO {

    @NotNull(message="用户id必填")
    @ApiModelProperty(value = "id",required = true)
    private Integer id;

    @JSONField(name = "user_name")
    @Length(max=30,message = "用户名小于30")
    private String userName;

    @JSONField(name = "mobile")
    @Pattern(regexp = "^[1][3857649][0-9]{9}|.{0}$",message = "手机号错误")
    private String mobile;

    @JSONField(name = "nick_name")
    @Length(max=20,message = "真实用户名小于20")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]+$",message = "真实名称只允许中文")
    private String nickName;

    @JSONField(name = "department")
    private Integer department;

    private Integer role;

    @ApiModelProperty(value = "password",notes = "密码，长度6-10位，支持数字、字母大小写、特殊字符")
    private String password;
    @ApiModelProperty(value = "1,启用；0，禁用")
    private Integer enable;

    public TUser convertToUser() {
        TUser user = new TUser();
        user.setId(this.id);
        user.setDepartment(this.department);
        user.setNickName(this.nickName);
        user.setUserName(this.userName);
        user.setPassword(this.password);
        user.setMobile(this.mobile);
        user.setEnable(this.enable);
        return user;
    }
}
