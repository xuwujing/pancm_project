package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (DeviceCipherRule)实体类
 *
 * @author beixing
 * @since 2021-08-23 16:15:57
 */
@Data
@Table(name = "device_cipher_rule")
public class DeviceCipherRule implements Serializable {
    private static final long serialVersionUID = -76292556158529934L;
    @Column(name = "id")
    private Integer id;
    /**
     * 规则字符 正则
     */
    @Column(name = "rule_char")
    private String ruleChar;
    /**
     * 密码长度 长度在6~10位
     */
    @Column(name = "rule_length")
    private Integer ruleLength;
    /**
     * 单位小时，-1表示永久
     */
    @Column(name = "rule_period")
    private Integer rulePeriod;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
