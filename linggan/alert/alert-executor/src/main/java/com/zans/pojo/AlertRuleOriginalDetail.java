package com.zans.pojo;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: (AlertRuleOriginalDetail)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-26 09:51:07
 */
@Data
public class AlertRuleOriginalDetail implements Serializable {
    private static final long serialVersionUID = -90050199000986453L;
    /**
     * 自增ID
     */

    private Long id;
    /**
     * 告警信息所属规则编号
     */
    private Long rule_id;
    /**
     * 业务主键，策略ID+业务名称组成
     */
    private String business_id;
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

    private String username;
    /**
     * ip地址
     */

    private String ip_addr;
    /**
     * 品牌
     */
    private String brand_name;
    /**
     * 设备型号
     */
    private String model_des;
    /**
     * nasIp
     */
    private String nas_ip_address;
    /**
     * nas端口(nas接口需要转换)
     */
    private String nas_port_id;
    /**
     * 点位名称
     */

    private String point_name;
    /**
     * 交换机点位名称
     */

    private String sw_point_name;
    /**
     * 交换机类型名称
     */
    private String sw_type_name;
    /**
     * vlan
     */

    private String vlan;
    /**
     * 项目名称
     */
    private String project_name;
    /**
     * 承建单位
     */
    private String contractor;
    /**
     * 联系人
     */
    private String contractor_person;
    /**
     * 联系电话
     */
    private String contractor_phone;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
