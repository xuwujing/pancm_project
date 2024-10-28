package com.zans.mms.model;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (WechatUserWxbind)实体类
 *
 * @author beixing
 * @since 2021-03-07 17:00:35
 */
@ApiModel(value = "WechatUserWxbind", description = "")
@Data
@Table(name = "wechat_user_wxbind")
public class WechatUserWxbind extends BasePage implements Serializable {
    private static final long serialVersionUID = 673431616470942416L;
    /**
     * 数据库主键，自增长
     */
    @Column(name = "id")
    @ApiModelProperty(value = "数据库主键，自增长")
    private Integer id;
    /**
     * 关联sys_user表的user_name
     */
    @Column(name = "user_name")
    @ApiModelProperty(value = "关联sys_user表的user_name")
    private String userName;
    /**
     * 微信小程序、公众号不同的唯一ID
     */
    @Column(name = "wx_appid")
    @ApiModelProperty(value = "微信小程序、公众号不同的唯一ID")
    private String wxAppid;
    /**
     * 关注微信公众号或授权小程序后的openid
     */
    @Column(name = "wx_openid")
    @ApiModelProperty(value = "关注微信公众号或授权小程序后的openid")
    private String wxOpenid;
    /**
     * 关注微信公众号或授权小程序后的unionid
     */
    @Column(name = "wx_unionid")
    @ApiModelProperty(value = "关注微信公众号或授权小程序后的unionid")
    private String wxUnionid;
    /**
     * 微信昵称
     */
    @Column(name = "wx_nicename")
    @ApiModelProperty(value = "微信昵称")
    private String wxNicename;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
