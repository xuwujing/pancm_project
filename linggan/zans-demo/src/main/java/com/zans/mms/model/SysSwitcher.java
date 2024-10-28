package com.zans.mms.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "sys_switcher")
public class SysSwitcher implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 交换机系统名
     */
    @Column(name = "name")
    private String name;

    /**
     * 交换机系统名
     */
    @Column(name = "sys_name")
    private String sysName;

    /**
     * 登陆地址
     */
    @Column(name = "sw_host")
    private String swHost;

    /**
     * 交换机类型，0core; 1convergence; 2access
     */
    @Column(name = "sw_type")
    private Integer swType;

    /**
     * 区域
     */
    private Integer area;

    /**
     * 描述
     */
    @Column(name = "sys_desc")
    private String sysDesc;

    /**
     * ssh端口，允许为空，默认22
     */
    @Column(name = "ssh_port")
    private Integer sshPort;

    /**
     * telnet端口，允许为空，默认23
     */
    @Column(name = "telnet_port")
    private Integer telnetPort;

    /**
     * 账号
     */
    @Column(name = "sw_account")
    private String swAccount;

    /**
     * 密码
     */
    @Column(name = "sw_password")
    private String swPassword;

    /**
     * 写权限的团体名
     */
    @Column(name = "sw_community")
    private String swCommunity;

    /**
     * snmp版本，默认2c
     */
    @Column(name = "sw_snmp_version")
    private String swSnmpVersion;

    /**
     * 登陆协议，all|ssh|telnet
     */
    private String protocol;

    /**
     * 品牌
     */
    private Integer brand;

    /**
     * 硬件型号
     */
    private String model;

    /**
     * 软件版本
     */
    private String version;

    /**
     * blackhole数量
     */
    @Column(name = "black_hole_count")
    private Integer blackHoleCount;

    /**
     * 0,未配置radius；1.已配置radius
     */
    @Column(name = "radius_config")
    private Integer radiusConfig;

    @Column(name = "scan_interface_count")
    private Integer scanInterfaceCount;

    @Column(name = "scan_mac_alive")
    private Integer scanMacAlive;

    @Column(name = "scan_mac_all")
    private Integer scanMacAll;
    /***
     * 纬度
     */
    private BigDecimal lon;
    /**
     * 经度
     */
    private BigDecimal lat;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 下行物理接口数
     */
    private Integer interfacePhyDown;
    /**
     * 有设备接入的下行物理接口数
     */
    private Integer interfacePhyDownUsed;

    /**
     * 启用arp扫描 根节点
     */
    @Column(name = "arp_enable")
    private Integer arpEnable;

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

    public Integer getScanMacAlive() {
        return scanMacAlive;
    }

    public void setScanMacAlive(Integer scanMacAlive) {
        this.scanMacAlive = scanMacAlive;
    }

    public Integer getScanMacAll() {
        return scanMacAll;
    }

    public void setScanMacAll(Integer scanMacAll) {
        this.scanMacAll = scanMacAll;
    }

    public Integer getScanInterfaceCount() {
        return scanInterfaceCount;
    }

    public void setScanInterfaceCount(Integer scanInterfaceCount) {
        this.scanInterfaceCount = scanInterfaceCount;
    }

    /**
     * 获取交换机系统名
     *
     * @return sys_name - 交换机系统名
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * 设置交换机系统名
     *
     * @param sysName 交换机系统名
     */
    public void setSysName(String sysName) {
        this.sysName = sysName == null ? null : sysName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取登陆地址
     *
     * @return sw_host - 登陆地址
     */
    public String getSwHost() {
        return swHost;
    }

    /**
     * 设置登陆地址
     *
     * @param swHost 登陆地址
     */
    public void setSwHost(String swHost) {
        this.swHost = swHost == null ? null : swHost.trim();
    }

    /**
     * 获取交换机类型，0core; 1convergence; 2access
     *
     * @return sw_type - 交换机类型，0core; 1convergence; 2access
     */
    public Integer getSwType() {
        return swType;
    }

    /**
     * 设置交换机类型，0core; 1convergence; 2access
     *
     * @param swType 交换机类型，0core; 1convergence; 2access
     */
    public void setSwType(Integer swType) {
        this.swType = swType;
    }

    /**
     * 获取区域
     *
     * @return area - 区域
     */
    public Integer getArea() {
        return area;
    }

    /**
     * 设置区域
     *
     * @param area 区域
     */
    public void setArea(Integer area) {
        this.area = area;
    }

    public String getSysDesc() {
        return sysDesc;
    }

    public void setSysDesc(String sysDesc) {
        this.sysDesc = sysDesc;
    }

    /**
     * 获取ssh端口，允许为空，默认22
     *
     * @return ssh_port - ssh端口，允许为空，默认22
     */
    public Integer getSshPort() {
        return sshPort;
    }

    /**
     * 设置ssh端口，允许为空，默认22
     *
     * @param sshPort ssh端口，允许为空，默认22
     */
    public void setSshPort(Integer sshPort) {
        this.sshPort = sshPort;
    }

    /**
     * 获取telnet端口，允许为空，默认23
     *
     * @return telnet_port - telnet端口，允许为空，默认23
     */
    public Integer getTelnetPort() {
        return telnetPort;
    }

    /**
     * 设置telnet端口，允许为空，默认23
     *
     * @param telnetPort telnet端口，允许为空，默认23
     */
    public void setTelnetPort(Integer telnetPort) {
        this.telnetPort = telnetPort;
    }

    /**
     * 获取账号
     *
     * @return sw_account - 账号
     */
    public String getSwAccount() {
        return swAccount;
    }

    /**
     * 设置账号
     *
     * @param swAccount 账号
     */
    public void setSwAccount(String swAccount) {
        this.swAccount = swAccount == null ? null : swAccount.trim();
    }

    /**
     * 获取密码
     *
     * @return sw_password - 密码
     */
    public String getSwPassword() {
        return swPassword;
    }

    /**
     * 设置密码
     *
     * @param swPassword 密码
     */
    public void setSwPassword(String swPassword) {
        this.swPassword = swPassword == null ? null : swPassword.trim();
    }

    /**
     * 获取写权限的团体名
     *
     * @return sw_community - 写权限的团体名
     */
    public String getSwCommunity() {
        return swCommunity;
    }

    /**
     * 设置写权限的团体名
     *
     * @param swCommunity 写权限的团体名
     */
    public void setSwCommunity(String swCommunity) {
        this.swCommunity = swCommunity == null ? null : swCommunity.trim();
    }

    /**
     * 获取snmp版本，默认2c
     *
     * @return sw_snmp_version - snmp版本，默认2c
     */
    public String getSwSnmpVersion() {
        return swSnmpVersion;
    }

    /**
     * 设置snmp版本，默认2c
     *
     * @param swSnmpVersion snmp版本，默认2c
     */
    public void setSwSnmpVersion(String swSnmpVersion) {
        this.swSnmpVersion = swSnmpVersion == null ? null : swSnmpVersion.trim();
    }

    /**
     * 获取登陆协议，all|ssh|telnet
     *
     * @return protocol - 登陆协议，all|ssh|telnet
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * 设置登陆协议，all|ssh|telnet
     *
     * @param protocol 登陆协议，all|ssh|telnet
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol == null ? null : protocol.trim();
    }

    /**
     * 获取品牌
     *
     * @return brand - 品牌
     */
    public Integer getBrand() {
        return brand;
    }

    /**
     * 设置品牌
     *
     * @param brand 品牌
     */
    public void setBrand(Integer brand) {
        this.brand = brand;
    }

    /**
     * 获取硬件型号
     *
     * @return model - 硬件型号
     */
    public String getModel() {
        return model;
    }

    /**
     * 设置硬件型号
     *
     * @param model 硬件型号
     */
    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    /**
     * 获取软件版本
     *
     * @return version - 软件版本
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置软件版本
     *
     * @param version 软件版本
     */
    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    /**
     * 获取blackhole数量
     *
     * @return black_hole_count - blackhole数量
     */
    public Integer getBlackHoleCount() {
        return blackHoleCount;
    }

    /**
     * 设置blackhole数量
     *
     * @param blackHoleCount blackhole数量
     */
    public void setBlackHoleCount(Integer blackHoleCount) {
        this.blackHoleCount = blackHoleCount;
    }

    /**
     * 获取0,未配置radius；1.已配置radius
     *
     * @return radius_config - 0,未配置radius；1.已配置radius
     */
    public Integer getRadiusConfig() {
        return radiusConfig;
    }

    /**
     * 设置0,未配置radius；1.已配置radius
     *
     * @param radiusConfig 0,未配置radius；1.已配置radius
     */
    public void setRadiusConfig(Integer radiusConfig) {
        this.radiusConfig = radiusConfig;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getInterfacePhyDown() {
        return interfacePhyDown;
    }

    public void setInterfacePhyDown(Integer interfacePhyDown) {
        this.interfacePhyDown = interfacePhyDown;
    }

    public Integer getInterfacePhyDownUsed() {
        return interfacePhyDownUsed;
    }

    public void setInterfacePhyDownUsed(Integer interfacePhyDownUsed) {
        this.interfacePhyDownUsed = interfacePhyDownUsed;
    }

    public Integer getArpEnable() {
        return arpEnable;
    }

    public void setArpEnable(Integer arpEnable) {
        this.arpEnable = arpEnable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", sysName=").append(sysName);
        sb.append(", swHost=").append(swHost);
        sb.append(", swType=").append(swType);
        sb.append(", area=").append(area);
        sb.append(", sysDesc=").append(sysDesc);
        sb.append(", sshPort=").append(sshPort);
        sb.append(", telnetPort=").append(telnetPort);
        sb.append(", swAccount=").append(swAccount);
        sb.append(", swPassword=").append(swPassword);
        sb.append(", swCommunity=").append(swCommunity);
        sb.append(", swSnmpVersion=").append(swSnmpVersion);
        sb.append(", protocol=").append(protocol);
        sb.append(", brand=").append(brand);
        sb.append(", model=").append(model);
        sb.append(", version=").append(version);
        sb.append(", blackHoleCount=").append(blackHoleCount);
        sb.append(", radiusConfig=").append(radiusConfig);
        sb.append(", lat=").append(lat);
        sb.append(", lon=").append(lon);
        sb.append(", projectName=").append(projectName);
        sb.append(", scanPhysicsDownInterface=").append(interfacePhyDown);
        sb.append(", scanPhysicsDownInterfaceUse=").append(interfacePhyDownUsed);
        sb.append("]");
        return sb.toString();
    }
}