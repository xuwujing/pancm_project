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
 * 微信配置文件表(WechatConfig)实体类
 *
 * @author beixing
 * @since 2021-03-11 11:45:07
 */
@ApiModel(value = "WechatConfig", description = "微信配置文件表")
@Data
@Table(name = "wechat_config")
public class WechatConfig extends BasePage implements Serializable {
    private static final long serialVersionUID = -33373192829393346L;
    @Column(name = "id")
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    /**
     * 微信公众号APPID
     */
    @Column(name = "md_appid")
    @ApiModelProperty(value = "微信公众号APPID")
    private String mdAppid;
    /**
     * 微信公众号secret
     */
    @Column(name = "md_secret")
    @ApiModelProperty(value = "微信公众号secret")
    private String mdSecret;
    /**
     * 微信公众号名称
     */
    @Column(name = "md_appname")
    @ApiModelProperty(value = "微信公众号名称")
    private String mdAppname;
    /**
     * 微信小程序APPID
     */
    @Column(name = "mp_appid")
    @ApiModelProperty(value = "微信小程序APPID")
    private String mpAppid;
    /**
     * 微信小程序secret
     */
    @Column(name = "mp_secret")
    @ApiModelProperty(value = "微信小程序secret")
    private String mpSecret;
    /**
     * 微信小程序名称
     */
    @Column(name = "mp_appname")
    @ApiModelProperty(value = "微信小程序名称")
    private String mpAppname;
    @Column(name = "create_time")
    @ApiModelProperty(value = "${column.comment}")
    private Date createTime;
    @Column(name = "update_time")
    @ApiModelProperty(value = "${column.comment}")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
