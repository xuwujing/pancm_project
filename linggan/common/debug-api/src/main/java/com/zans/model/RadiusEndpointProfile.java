package com.zans.model;

import java.util.Date;

import com.zans.base.vo.BasePage;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (RadiusEndpointProfile)实体类
 *
 * @author beixing
 * @since 2022-03-23 16:28:53
 */
@Data
@Table(name = "radius_endpoint_profile")
public class RadiusEndpointProfile implements Serializable {
    private static final long serialVersionUID = -56668946368050548L;
    /**
     * id
     */
    @Column(name = "endpoint_id")
    private Integer endpointId;
    /**
     * 12位mac地址
     */
    @Column(name = "mac")
    private String mac;
    /**
     * 离线检查的节点
     */
    @Column(name = "alive_not_node")
    private String aliveNotNode;
    /**
     * radius reply
     */
    @Column(name = "reply_message")
    private String replyMessage;
    /**
     * radius filter-id
     */
    @Column(name = "filter_id")
    private String filterId;
    /**
     * session，radius填写
     */
    @Column(name = "acct_session_id")
    private String acctSessionId;
    /**
     * session更新时间
     */
    @Column(name = "acct_update_time")
    private String acctUpdateTime;
    /**
     * 厂商
     */
    @Column(name = "company")
    private String company;
    /**
     * 品牌
     */
    @Column(name = "brand_name")
    private String brandName;
    @Column(name = "open_port_all")
    private String openPortAll;
    /**
     * 设备识别等级 0:未识别；1:协议扫描识别; 2:端口扫描识别;
     */
    @Column(name = "model_level")
    private Integer modelLevel;
    /**
     * 设备序列号
     */
    @Column(name = "serial_no")
    private String serialNo;
    /**
     * 软件版本
     */
    @Column(name = "version")
    private String version;
    /**
     * 协议扫描到的MAC地址
     */
    @Column(name = "mac_scan")
    private String macScan;
    @Column(name = "cur_ip_addr")
    private String curIpAddr;
    @Column(name = "cur_sw_ip")
    private String curSwIp;
    @Column(name = "cur_nas_ip_address")
    private String curNasIpAddress;
    @Column(name = "cur_nas_port_id")
    private String curNasPortId;
    /**
     * vlan
     */
    @Column(name = "cur_vlan")
    private String curVlan;
    @Column(name = "cur_open_port")
    private String curOpenPort;
    @Column(name = "cur_mute")
    private Integer curMute;
    @Column(name = "cur_server_os")
    private String curServerOs;
    @Column(name = "cur_device_type")
    private Integer curDeviceType;
    @Column(name = "cur_model_des")
    private String curModelDes;
    /**
     * 如果非0，则不用扫描修改radius_endpoint_profile的当前设备类型相关
     */
    @Column(name = "device_lock")
    private Integer deviceLock;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    @Column(name = "check_last_time")
    private String checkLastTime;
    /**
     * 数据来源:0:默认,1:来源风险预警
     */
    @Column(name = "source")
    private Object source;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
