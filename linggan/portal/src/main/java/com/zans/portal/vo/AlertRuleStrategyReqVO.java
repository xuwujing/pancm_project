package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: (AlertRuleStrategy)请求响应对象
 * @Description: 告警规则策略表
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-06 15:25:10
 */
@Data
public class AlertRuleStrategyReqVO implements Serializable {
    private static final long serialVersionUID = 680987727618774370L;


    /**
     * 主键 策略id
     */
    private Long id;

    /**
     * 告警规则id
     */

    private Long rule_id;
    /**
     * 策略组ID,0表示全局
     */
    private Integer group_id;
    /**
     * 策略组名称
     */

    private String group_name;
    /**
     * 告警级别
     */
    private Integer alert_level;
    /**
     * 阈值
     */
    private String alert_threshold;

    private Integer alert_interval;
    /**
     * 是否自动恢复,0:否,1:是
     */
    private Integer auto_recover;
    /**
     * 规则状态 0:禁用,1:启用
     */

    private Integer rule_status;




    private JobReqVO jobReqVO;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
