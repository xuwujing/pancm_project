package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (RadiusPolicyConfig)实体类
 *
 * @author beixing
 * @since 2021-09-23 17:34:18
 */
@Data
@Table(name = "radius_policy_config")
public class RadiusPolicyConfig implements Serializable {
    private static final long serialVersionUID = -68406676485957402L;
    @Column(name = "id")
    private Integer id;
    /**
     * 规则名称
     */
    @Column(name = "rule_name")
    private String ruleName;
    /**
     * 规则说明
     */
    @Column(name = "rule_desc")
    private String ruleDesc;
    /**
     * 规则
     */
    @Column(name = "rule")
    private String rule;
    /**
     * policy值
     */
    @Column(name = "policy")
    private Integer policy;
    /**
     * 提示状态 0,不提示;1,提示;2,不允许
     */
    @Column(name = "hint_status")
    private Integer hintStatus;
    /**
     * 级别
     */
    @Column(name = "level")
    private Integer level;
    /**
     * 结果判断,1:是存在即可;0:不存在即可;
     */
    @Column(name = "result")
    private Integer result;
    /**
     * 启用禁用状态
     */
    @Column(name = "enable")
    private Integer enable;
    /**
     * 排序规则
     */
    @Column(name = "seq")
    private Integer seq;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
