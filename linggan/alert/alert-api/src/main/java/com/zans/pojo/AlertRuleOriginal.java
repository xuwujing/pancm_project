package com.zans.pojo;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: (AlertRuleOriginal)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-26 09:51:11
 */
@Data

public class AlertRuleOriginal implements Serializable {
    private static final long serialVersionUID = -63157186278342267L;
    /**
     * 自增ID
     */

    private Long id;
    /**
     * 告警信息所属规则编号
     */

    private Long rule_id;

    /**
     * 策略id
     */
    private Long strategy_id;
    /**
     * 业务主键，策略ID+业务名称组成
     */

    private String business_id;
    /**
     * 通知信息
     */

    private String notice_info;

    /**
     * 站内通知  0:否，1:是
     */

    private String notice_local;
    /**
     * 备注
     */

    private String notice_remark;
    /**
     * 推送状态 0:未推送,1:推送成功
     */

    private Integer notice_status;
    /**
     * 处理状态 0:否,1:人工处理,2:自动处理
     */

    private Integer deal_status;
    /**
     * 关键字内容值
     */

    private String keyword_value;

    /**
     * 处置状态
     */

    private Integer dispose_status;
    /**
     * 处置人
     */
    private String dispose_user;
    /**
     * 选择的mac,0:基准,1:当前
     */

    private Integer choose_mac_type;
    /**
     * 删除状态,0:否,1:是
     */

    private Integer delete_status;

    private Integer alert_level;
    /**
     * 点位名称
     */

    private String point_name;
    /**
     * 地区名称
     */

    private String area_name;
    /**
     * 设备类型名称
     */

    private String device_type_name;
    /**
     * mac地址
     */

    private String mac;
    /**
     * IP地址
     */

    private String ip_addr;
    /**
     * 规则查询sql的json数据
     */

    private String sql_json;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
