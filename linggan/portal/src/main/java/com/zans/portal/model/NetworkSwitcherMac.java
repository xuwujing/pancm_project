package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "network_switcher_mac")
public class NetworkSwitcherMac implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * sys_switcher.id
     */
    @Column(name = "sw_id")
    private Integer swId;

    /**
     * 交换机IP地址
     */
    @Column(name = "sw_ip")
    private String swIp;

    /**
     * 真实的itf_index,迪普等交换机需要转换
     */
    @Column(name = "interface_index")
    private Integer interfaceIndex;

    /**
     * 接口描述
     */
    @Column(name = "interface_detail")
    private String interfaceDetail;

    /**
     * mac地址
     */
    private String mac;

    /**
     * vlan，接入交换机
     */
    private String vlan;

    /**
     * 在三层交换机对应的IP，回填字段，从t_arp中的 mac地址查询得到
     */
    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * ip写入的时间，回填字段
     */
    @Column(name = "ip_time")
    private Date ipTime;

    /**
     * t_arp.id，回填字段
     */
    @Column(name = "arp_id")
    private Integer arpId;

    /**
     * 1，当前扫描存活；2.不存活
     */
    @Column(name = "mac_alive")
    private Integer macAlive;

    private String company;

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

    public Integer getMacAlive() {
        return macAlive;
    }

    public void setMacAlive(Integer macAlive) {
        this.macAlive = macAlive;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * 获取sys_switcher.id
     *
     * @return sw_id - sys_switcher.id
     */
    public Integer getSwId() {
        return swId;
    }

    /**
     * 设置sys_switcher.id
     *
     * @param swId sys_switcher.id
     */
    public void setSwId(Integer swId) {
        this.swId = swId;
    }

    /**
     * 获取交换机IP地址
     *
     * @return sw_ip - 交换机IP地址
     */
    public String getSwIp() {
        return swIp;
    }

    /**
     * 设置交换机IP地址
     *
     * @param swIp 交换机IP地址
     */
    public void setSwIp(String swIp) {
        this.swIp = swIp == null ? null : swIp.trim();
    }

    /**
     * 获取真实的itf_index,迪普等交换机需要转换
     *
     * @return interface_index - 真实的itf_index,迪普等交换机需要转换
     */
    public Integer getInterfaceIndex() {
        return interfaceIndex;
    }

    /**
     * 设置真实的itf_index,迪普等交换机需要转换
     *
     * @param interfaceIndex 真实的itf_index,迪普等交换机需要转换
     */
    public void setInterfaceIndex(Integer interfaceIndex) {
        this.interfaceIndex = interfaceIndex;
    }

    /**
     * 获取接口描述
     *
     * @return interface_detail - 接口描述
     */
    public String getInterfaceDetail() {
        return interfaceDetail;
    }

    /**
     * 设置接口描述
     *
     * @param interfaceDetail 接口描述
     */
    public void setInterfaceDetail(String interfaceDetail) {
        this.interfaceDetail = interfaceDetail == null ? null : interfaceDetail.trim();
    }

    /**
     * 获取mac地址
     *
     * @return mac - mac地址
     */
    public String getMac() {
        return mac;
    }

    /**
     * 设置mac地址
     *
     * @param mac mac地址
     */
    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }

    /**
     * 获取vlan，接入交换机
     *
     * @return vlan - vlan，接入交换机
     */
    public String getVlan() {
        return vlan;
    }

    /**
     * 设置vlan，接入交换机
     *
     * @param vlan vlan，接入交换机
     */
    public void setVlan(String vlan) {
        this.vlan = vlan == null ? null : vlan.trim();
    }

    /**
     * 获取在三层交换机对应的IP，回填字段，从t_arp中的 mac地址查询得到
     *
     * @return ip_addr - 在三层交换机对应的IP，回填字段，从t_arp中的 mac地址查询得到
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * 设置在三层交换机对应的IP，回填字段，从t_arp中的 mac地址查询得到
     *
     * @param ipAddr 在三层交换机对应的IP，回填字段，从t_arp中的 mac地址查询得到
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    /**
     * 获取ip写入的时间，回填字段
     *
     * @return ip_time - ip写入的时间，回填字段
     */
    public Date getIpTime() {
        return ipTime;
    }

    /**
     * 设置ip写入的时间，回填字段
     *
     * @param ipTime ip写入的时间，回填字段
     */
    public void setIpTime(Date ipTime) {
        this.ipTime = ipTime;
    }

    /**
     * 获取t_arp.id，回填字段
     *
     * @return arp_id - t_arp.id，回填字段
     */
    public Integer getArpId() {
        return arpId;
    }

    /**
     * 设置t_arp.id，回填字段
     *
     * @param arpId t_arp.id，回填字段
     */
    public void setArpId(Integer arpId) {
        this.arpId = arpId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", swId=").append(swId);
        sb.append(", swIp=").append(swIp);
        sb.append(", interfaceIndex=").append(interfaceIndex);
        sb.append(", interfaceDetail=").append(interfaceDetail);
        sb.append(", mac=").append(mac);
        sb.append(", vlan=").append(vlan);
        sb.append(", ipAddr=").append(ipAddr);
        sb.append(", ipTime=").append(ipTime);
        sb.append(", arpId=").append(arpId);
        sb.append(", macAlive=").append(macAlive);
        sb.append("]");
        return sb.toString();
    }
}
