package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * (DeviceCipherRule)实体类
 *
 * @author beixing
 * @since 2021-08-23 16:15:56
 */
@Data
public class DeviceCipherRuleVO extends BasePage implements Serializable {
    private static final long serialVersionUID = -80728657191981865L;

    private Integer id;
    /**
     * 规则字符 正则
     */
    private String ruleChar;
    /**
     * 密码长度 长度在6~10位
     */
    private Integer ruleLength;
    /**
     * 单位小时，-1表示永久
     */
    private Integer rulePeriod;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
