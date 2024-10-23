package com.zans.pojo;

import com.zans.commons.utils.MyTools;
import lombok.Data;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警规则实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
@Data
public class AlertRuleBean {

    private Long id;
    private Long group_id;
    private String strategy_name;
    private String strategy_desc;
    private Integer alert_type;
    private Integer alert_index;
    private Integer alert_level;
    private String alert_rule;
    private String alert_dsl;
    private Integer alert_dsl_deal;
    private String alert_template;
    private String alert_keyword;

    private String alert_op;
    private Integer alert_threshold;
    private Integer alert_period;
    private Integer alert_expire;

    private Integer alert_datasource;

    private Integer alert_action;
    private String action;

    private String sql_action;

    private Integer rule_type;
    private Integer rule_status;

    private Integer auto_recover;

    private String query_sql;
    private String query_detail_sql;
    private String query_detail_ex_sql;



    @Override
    public String toString() {
        return MyTools.toString(this);
    }
}
