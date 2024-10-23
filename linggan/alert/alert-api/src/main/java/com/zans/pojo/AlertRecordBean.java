package com.zans.pojo;

import com.alibaba.fastjson.JSONObject;
import com.zans.commons.utils.MyTools;
import lombok.Data;

/**
 * @author pancm
 * @Title: alert-executor
 * @Description: 规则记录实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/4
 */
@Data
public class AlertRecordBean {
    /**规则ID */
    private Long  rule_id;
    /** 唯一ID，规则ID+业务ID生成 */
    private String  business_id;
    /** 告警内容 */
    private String  notice_info;
    /** 关键字内容 */
    private String  keyword_value;
    /** 告警内容数据来源 0:规则生成,1:接口推送*/
    private Integer  record_source;

    private String  query_sql;

    private String  query_detail_sql;

    private String  query_detail_ex_sql;

    private JSONObject sql_json;



    @Override
    public String toString() {
        return MyTools.toString(this);
    }
}
