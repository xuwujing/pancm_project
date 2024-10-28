package com.zans.mms.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 微信app模板配置表(WechatAppTemplate)实体类
 *
 * @author beixing
 * @since 2021-03-15 11:29:42
 */
@ApiModel(value = "WechatAppTemplate", description = "微信app模板配置表")
@Data
@Table(name = "wechat_app_template")
public class WechatAppTemplate implements Serializable {
    private static final long serialVersionUID = 455237110409343808L;
    @Column(name = "id")
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    /**
     * 应用ID
     */
    @Column(name = "appid")
    @ApiModelProperty(value = "应用ID")
    private String appid;
    /**
     * 模板ID
     */
    @Column(name = "template_id")
    @ApiModelProperty(value = "模板ID")
    private String templateId;
    /**
     * 模块类型
     */
    @Column(name = "template_type")
    @ApiModelProperty(value = "模块类型")
    private Integer templateType;
    /**
     * 模板名称(first)
     */
    @Column(name = "template_name")
    @ApiModelProperty(value = "模板名称(first)")
    private String templateName;
    /**
     * 模板内容(remark)
     */
    @Column(name = "template_msg")
    @ApiModelProperty(value = "模板内容(remark)")
    private String templateMsg;
    /**
     * 模板规则
     */
    @Column(name = "template_rule")
    @ApiModelProperty(value = "模板规则")
    private String templateRule;
    /**
     * 模板规则长度
     */
    @Column(name = "template_rule_size")
    @ApiModelProperty(value = "模板规则长度")
    private Integer templateRuleSize;

    @Column(name = "is_redirect")
    @ApiModelProperty(value = "是否跳转页面")
    private Integer isRedirect;
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
