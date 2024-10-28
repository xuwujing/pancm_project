package com.zans.mms.vo.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (SysUserWxbind)实体类
 *
 * @author beixing
 * @since 2021-01-29 14:25:01
 */
@ApiModel(value = "SysUserWxbind", description = "")
@Data
public class SysUserWxbindSaveReqVO implements Serializable {
    private static final long serialVersionUID = -99399853881658414L;
    /**
     * 数据库主键，自增长
     */
    @ApiModelProperty(value = "数据库主键，自增长")
    private Integer id;
    /**
     * 关联sys_user表的user_name
     */
    @ApiModelProperty(value = "关联sys_user表的user_name")
    private String userName;
    /**
     * 微信小程序、公众号不同的唯一ID
     */
    @ApiModelProperty(value = "微信小程序、公众号不同的唯一ID")
    private String wxAppid;
    /**
     * 关注微信公众号或授权小程序后的openid
     */
    @ApiModelProperty(value = "关注微信公众号或授权小程序后的openid")
    private String wxOpenid;
    /**
     * 关注微信公众号或授权小程序后的unionid
     */
    @ApiModelProperty(value = "关注微信公众号或授权小程序后的unionid")
    private String wxUnionid;
    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    private String wxNicename;
    /**
     * 微信用户的头像路径
     */
    @ApiModelProperty(value = "微信用户的头像路径")
    private String wxAvatar;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
