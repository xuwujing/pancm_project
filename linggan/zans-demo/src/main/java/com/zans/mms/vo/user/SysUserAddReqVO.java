package com.zans.mms.vo.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysUserAddReqVO {


    /**
     * 用户名
     */
    @NotBlank
    @ApiModelProperty(value = "用户名")
    private String userName;
    /**
     * 昵称
     */
    @NotBlank
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 手机号
     */
    @NotBlank
    @ApiModelProperty(value = "手机号")
    private String mobile;
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String account;
    /**
     * 密码
     */
    @NotBlank
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 所属角色
     */
    @NotBlank
    @ApiModelProperty(value = "所属角色")
    private String roleNum;
    /**
     * 所属单位
     */
    @NotBlank
    @ApiModelProperty(value = "所属单位")
    private String maintainNum;

    /**
     * 1,启用；0，禁用
     */
    @ApiModelProperty(value = "1,启用；0，禁用")
    private Integer enable;


    /**
     * 微信登录 1,启用；0，禁用
     */
    @NotBlank
    @ApiModelProperty(value = "1,启用；0，禁用")
    private Integer wechatEnable;


    /**
     * PC登录,1,启用；0，禁用;
     */
    @ApiModelProperty(value = "PC登录,1,启用；0，禁用;")
    private Integer pcEnable;
    /**
     * 微信推送,1,启用；0，禁用;
     */
    @ApiModelProperty(value = "微信推送,1,启用；0，禁用;")
    private Integer wechatPushEnable;


    @ApiModelProperty(value = "创建人")
    private String creator;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
