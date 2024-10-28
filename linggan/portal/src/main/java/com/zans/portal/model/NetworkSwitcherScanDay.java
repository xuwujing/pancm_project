package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (NetworkSwitcherScanDay)实体类
 *
 * @author beixing
 * @since 2021-11-11 12:28:46
 */
@Data
@Table(name = "network_switcher_scan_day")
public class NetworkSwitcherScanDay implements Serializable {
    private static final long serialVersionUID = -87451266310685289L;
    @Column(name = "id")
    private Long id;
    /**
     * 交换机IP
     */
    @Column(name = "sw_ip")
    private String swIp;
    /**
     * 年月日
     */
    @Column(name = "yyyymmdd")
    private String yyyymmdd;

    /**
     * 离线断电
     */
    @Column(name = "offline_duration")
    private Integer offlineDuration;
    /**
     * 断光时长
     */
    @Column(name = "affline_duration")
    private Integer afflineDuration;
    /**
     * 在线时长
     */
    @Column(name = "alive_duration")
    private Integer aliveDuration;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
