package com.zans.mms.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_area")
public class Area implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 地区编码
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * 区域名称
     */
    @Column(name = "area_name")
    private String areaName;

    /**
     * 行政区域
     */
    private Integer region;

    /**
     * ip表
     */
    @Column(name = "ip_table")
    private String ipTable;

    /**
     * arp表
     */
    @Column(name = "arp_table")
    private String arpTable;

    /**
     * 核心交换机IP
     */
    @Column(name = "sw_ip")
    private String swIp;

    /**
     * 交换机团体名
     */
    @Column(name = "sw_community")
    private String swCommunity;

    /**
     * 交换机snmp协议版本
     */
    @Column(name = "sw_snmp_version")
    private String swSnmpVersion;

    /**
     * IP表中的最大记录数
     */
    @Column(name = "ip_count")
    private Integer ipCount;

    /**
     * arp表中的最大数
     */
    @Column(name = "arp_count")
    private Integer arpCount;

    @Column(name = "arp_out_table")
    private String arpOutTable;

    /**
     * 核心交换机类别： 1：7706系列；1:12800系列；
     */
    @Column(name = "sw_model")
    private Integer swModel;

    @Transient
    private Integer swType;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取地区编码
     *
     * @return area_id - 地区编码
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * 设置地区编码
     *
     * @param areaId 地区编码
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取区域名称
     *
     * @return area_name - 区域名称
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * 设置区域名称
     *
     * @param areaName 区域名称
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    /**
     * 获取ip表
     *
     * @return ip_table - ip表
     */
    public String getIpTable() {
        return ipTable;
    }

    /**
     * 设置ip表
     *
     * @param ipTable ip表
     */
    public void setIpTable(String ipTable) {
        this.ipTable = ipTable == null ? null : ipTable.trim();
    }

    /**
     * 获取arp表
     *
     * @return arp_table - arp表
     */
    public String getArpTable() {
        return arpTable;
    }

    /**
     * 设置arp表
     *
     * @param arpTable arp表
     */
    public void setArpTable(String arpTable) {
        this.arpTable = arpTable == null ? null : arpTable.trim();
    }

    /**
     * 获取核心交换机IP
     *
     * @return sw_ip - 核心交换机IP
     */
    public String getSwIp() {
        return swIp;
    }

    /**
     * 设置核心交换机IP
     *
     * @param swIp 核心交换机IP
     */
    public void setSwIp(String swIp) {
        this.swIp = swIp == null ? null : swIp.trim();
    }

    /**
     * 获取交换机团体名
     *
     * @return sw_community - 交换机团体名
     */
    public String getSwCommunity() {
        return swCommunity;
    }

    /**
     * 设置交换机团体名
     *
     * @param swCommunity 交换机团体名
     */
    public void setSwCommunity(String swCommunity) {
        this.swCommunity = swCommunity == null ? null : swCommunity.trim();
    }

    /**
     * 获取交换机snmp协议版本
     *
     * @return sw_snmp_version - 交换机snmp协议版本
     */
    public String getSwSnmpVersion() {
        return swSnmpVersion;
    }

    /**
     * 设置交换机snmp协议版本
     *
     * @param swSnmpVersion 交换机snmp协议版本
     */
    public void setSwSnmpVersion(String swSnmpVersion) {
        this.swSnmpVersion = swSnmpVersion == null ? null : swSnmpVersion.trim();
    }

    /**
     * 获取IP表中的最大记录数
     *
     * @return ip_count - IP表中的最大记录数
     */
    public Integer getIpCount() {
        return ipCount;
    }

    /**
     * 设置IP表中的最大记录数
     *
     * @param ipCount IP表中的最大记录数
     */
    public void setIpCount(Integer ipCount) {
        this.ipCount = ipCount;
    }

    /**
     * 获取arp表中的最大数
     *
     * @return arp_count - arp表中的最大数
     */
    public Integer getArpCount() {
        return arpCount;
    }

    /**
     * 设置arp表中的最大数
     *
     * @param arpCount arp表中的最大数
     */
    public void setArpCount(Integer arpCount) {
        this.arpCount = arpCount;
    }

    /**
     * @return arp_out_table
     */
    public String getArpOutTable() {
        return arpOutTable;
    }

    /**
     * @param arpOutTable
     */
    public void setArpOutTable(String arpOutTable) {
        this.arpOutTable = arpOutTable == null ? null : arpOutTable.trim();
    }

    /**
     * 获取核心交换机类别： 1：7706系列；1:12800系列；
     *
     * @return sw_model - 核心交换机类别： 1：7706系列；1:12800系列；
     */
    public Integer getSwModel() {
        return swModel;
    }

    /**
     * 设置核心交换机类别： 1：7706系列；1:12800系列；
     *
     * @param swModel 核心交换机类别： 1：7706系列；1:12800系列；
     */
    public void setSwModel(Integer swModel) {
        this.swModel = swModel;
    }

    public Integer getSwType() {
        return swType;
    }

    public void setSwType(Integer swType) {
        this.swType = swType;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", areaId=").append(areaId);
        sb.append(", region=").append(region);
        sb.append(", areaName=").append(areaName);
        sb.append(", ipTable=").append(ipTable);
        sb.append(", arpTable=").append(arpTable);
        sb.append(", swIp=").append(swIp);
        sb.append(", swCommunity=").append(swCommunity);
        sb.append(", swSnmpVersion=").append(swSnmpVersion);
        sb.append(", ipCount=").append(ipCount);
        sb.append(", arpCount=").append(arpCount);
        sb.append(", arpOutTable=").append(arpOutTable);
        sb.append(", swModel=").append(swModel);
        sb.append("]");
        return sb.toString();
    }
}