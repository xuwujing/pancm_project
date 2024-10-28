package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * (RadiusPolicyConfig)实体类
 *
 * @author beixing
 * @since 2021-09-23 17:34:17
 */
@Data
public class RadiusPolicyConfig implements Serializable {
    private static final long serialVersionUID = -84811787059782404L;
    private Integer id;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 规则说明
     */
    private String ruleDesc;
    /**
     * 规则
     */
    private String rule;
    /**
     * policy值
     */
    private Integer policy;
    /**
     * 提示状态 0,不提示;1,提示;2,不允许
     */
    private Integer hintStatus;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 结果判断,1:是存在即可;0:不存在即可;
     */
    private Integer result;
    /**
     * 启用禁用状态
     */
    private Integer enable;
    /**
     * 排序规则
     */
    private Integer seq;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
