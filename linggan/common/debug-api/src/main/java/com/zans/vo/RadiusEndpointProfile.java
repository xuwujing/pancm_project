package com.zans.vo;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;

/**
 * (RadiusEndpointProfile)实体类
 *
 * @author beixing
 * @since 2022-03-23 16:28:53
 */
@Data
public class RadiusEndpointProfile implements Serializable {
    private static final long serialVersionUID = -26741138441310566L;
    /**
     * id
     */
    private Integer endpointId;
    /**
     * 12位mac地址
     */
    private String mac;
    /**
     * 离线检查的节点
     */
    private String aliveNotNode;
    /**
     * radius reply
     */
    private String replyMessage;
    /**
     * radius filter-id
     */
    private String filterId;
    /**
     * session，radius填写
     */
    private String acctSessionId;
    /**
     * session更新时间
     */
    private String acctUpdateTime;
    /**
     * 厂商
     */
    private String company;
    /**
     * 品牌
     */
    private String brandName;
    private String openPortAll;
    /**
     * 设备识别等级 0:未识别；1:协议扫描识别; 2:端口扫描识别;
     */
    private Integer modelLevel;
    /**
     * 设备序列号
     */
    private String serialNo;
    /**
     * 软件版本
     */
    private String version;
    /**
     * 协议扫描到的MAC地址
     */
    private String macScan;
    private String curIpAddr;
    private String curSwIp;
    private String curNasIpAddress;
    private String curNasPortId;
    /**
     * vlan
     */
    private String curVlan;
    private String curOpenPort;
    private Integer curMute;
    private String curServerOs;
    private Integer curDeviceType;
    private String curModelDes;
    /**
     * 如果非0，则不用扫描修改radius_endpoint_profile的当前设备类型相关
     */
    private Integer deviceLock;
    /**
     * 备注
     */
    private String remark;
    private String checkLastTime;
    /**
     * 数据来源:0:默认,1:来源风险预警
     */
    private Object source;
    private Date createTime;
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
