package com.zans.mms.vo.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysUserEditReqVO extends SysUserAddReqVO{

    @NotNull
    @ApiModelProperty(value = "${column.comment}",required = true)
    private Integer id;


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
    @ApiModelProperty(value = "1,启用；0，禁用;2，锁定")
    private Integer enable;


    /**
     * 1,启用；0，禁用
     */
    @ApiModelProperty(value = "1,启用；0，禁用")
    private Integer wechatEnable;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
