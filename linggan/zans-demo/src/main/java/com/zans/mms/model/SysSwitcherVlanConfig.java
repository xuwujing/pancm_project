package com.zans.mms.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "sys_switcher_vlan_config")
public class SysSwitcherVlanConfig implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 交换机ID
     */
    @Column(name = "sw_id")
    private Integer swId;

    /**
     * 用户备注的名称
     */
    private String name;

    /**
     * 交换机系统名，扫描
     */
    @Column(name = "sys_name")
    private String sysName;

    /**
     * 交换机描述，扫描
     */
    @Column(name = "sys_desc")
    private String sysDesc;

    /**
     * vlan
     */
    private Integer vlan;

    /**
     * 配置的IP,网关
     */
    @Column(name = "vlan_ip_addr")
    private String vlanIpAddr;

    /**
     * 掩码
     */
    @Column(name = "vlan_mask")
    private String vlanMask;

    /**
     * 段开始IP
     */
    @Column(name = "vlan_start_ip_addr")
    private String vlanStartIpAddr;

    /**
     * 段最后IP
     */
    @Column(name = "vlan_last_ip_addr")
    private String vlanLastIpAddr;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取交换机ID
     *
     * @return sw_id - 交换机ID
     */
    public Integer getSwId() {
        return swId;
    }

    /**
     * 设置交换机ID
     *
     * @param swId 交换机ID
     */
    public void setSwId(Integer swId) {
        this.swId = swId;
    }

    /**
     * 获取用户备注的名称
     *
     * @return name - 用户备注的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户备注的名称
     *
     * @param name 用户备注的名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取交换机系统名，扫描
     *
     * @return sys_name - 交换机系统名，扫描
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * 设置交换机系统名，扫描
     *
     * @param sysName 交换机系统名，扫描
     */
    public void setSysName(String sysName) {
        this.sysName = sysName == null ? null : sysName.trim();
    }

    /**
     * 获取交换机描述，扫描
     *
     * @return sys_desc - 交换机描述，扫描
     */
    public String getSysDesc() {
        return sysDesc;
    }

    /**
     * 设置交换机描述，扫描
     *
     * @param sysDesc 交换机描述，扫描
     */
    public void setSysDesc(String sysDesc) {
        this.sysDesc = sysDesc == null ? null : sysDesc.trim();
    }

    /**
     * 获取vlan
     *
     * @return vlan - vlan
     */
    public Integer getVlan() {
        return vlan;
    }

    /**
     * 设置vlan
     *
     * @param vlan vlan
     */
    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    /**
     * 获取配置的IP,网关
     *
     * @return vlan_ip_addr - 配置的IP,网关
     */
    public String getVlanIpAddr() {
        return vlanIpAddr;
    }

    /**
     * 设置配置的IP,网关
     *
     * @param vlanIpAddr 配置的IP,网关
     */
    public void setVlanIpAddr(String vlanIpAddr) {
        this.vlanIpAddr = vlanIpAddr == null ? null : vlanIpAddr.trim();
    }

    /**
     * 获取掩码
     *
     * @return vlan_mask - 掩码
     */
    public String getVlanMask() {
        return vlanMask;
    }

    /**
     * 设置掩码
     *
     * @param vlanMask 掩码
     */
    public void setVlanMask(String vlanMask) {
        this.vlanMask = vlanMask == null ? null : vlanMask.trim();
    }

    /**
     * 获取段开始IP
     *
     * @return vlan_start_ip_addr - 段开始IP
     */
    public String getVlanStartIpAddr() {
        return vlanStartIpAddr;
    }

    /**
     * 设置段开始IP
     *
     * @param vlanStartIpAddr 段开始IP
     */
    public void setVlanStartIpAddr(String vlanStartIpAddr) {
        this.vlanStartIpAddr = vlanStartIpAddr == null ? null : vlanStartIpAddr.trim();
    }

    /**
     * 获取段最后IP
     *
     * @return vlan_last_ip_addr - 段最后IP
     */
    public String getVlanLastIpAddr() {
        return vlanLastIpAddr;
    }

    /**
     * 设置段最后IP
     *
     * @param vlanLastIpAddr 段最后IP
     */
    public void setVlanLastIpAddr(String vlanLastIpAddr) {
        this.vlanLastIpAddr = vlanLastIpAddr == null ? null : vlanLastIpAddr.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", swId=").append(swId);
        sb.append(", name=").append(name);
        sb.append(", sysName=").append(sysName);
        sb.append(", sysDesc=").append(sysDesc);
        sb.append(", vlan=").append(vlan);
        sb.append(", vlanIpAddr=").append(vlanIpAddr);
        sb.append(", vlanMask=").append(vlanMask);
        sb.append(", vlanStartIpAddr=").append(vlanStartIpAddr);
        sb.append(", vlanLastIpAddr=").append(vlanLastIpAddr);
        sb.append("]");
        return sb.toString();
    }
}