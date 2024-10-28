package com.zans.mms.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "t_arp")
public class Arp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ip_addr")
    private String ipAddr;

    @Column(name = "mac_addr")
    private String macAddr;

    @Column(name = "sw_ip")
    private String swIp;

    /**
     * 开放端口列表
     */
    @Column(name = "open_port")
    private String openPort;

    /**
     * 0:初始;2:已拒绝;99:白名单
     */
    @Column(name = "ip_status")
    private Integer ipStatus;

    /**
     * 最后扫描时间
     */
    @Column(name = "check_last_time")
    private Date checkLastTime;

    /**
     * 厂商
     */
    private String company;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private Integer deviceType;

    /**
     * 设备识别等级 0:未识别；1:协议扫描识别; 2:端口扫描识别;
     */
    @Column(name = "model_level")
    private Integer modelLevel;

    @Column(name = "model_des")
    private String modelDes;

    /**
     * 上线时间
     */
    @Column(name = "online_time")
    private Date onlineTime;

    /**
     * t_arpout表中的主键
     */
    @Column(name = "arp_out_id")
    private Integer arpOutId;

    /**
     * 学习到的物理端口编号
     */
    @Column(name = "arp_out_index")
    private Integer arpOutIndex;

    /**
     * 学习到的物理端口
     */
    @Column(name = "arp_out_desc")
    private String arpOutDesc;

    /**
     * 最后处置时间
     */
    @Column(name = "deal_last_time")
    private Date dealLastTime;

    /**
     * 最后一次的处理意见
     */
    @Column(name = "deal_last_mark")
    private String dealLastMark;

    /**
     * 当前MAC地址
     */
    @Column(name = "cur_mac_addr")
    private String curMacAddr;

    /**
     * 当前公司名称
     */
    @Column(name = "cur_company")
    private String curCompany;

    /**
     * 当前开发端口
     */
    @Column(name = "cur_open_port")
    private String curOpenPort;

    @Column(name = "cur_arp_out_index")
    private Integer curArpOutIndex;

    @Column(name = "cur_arp_out_desc")
    private String curArpOutDesc;

    /**
     * 当前设备类别
     */
    @Column(name = "cur_model")
    private Integer curModel;

    /**
     * 如果设备型号有改变，记录新的设备信号
     */
    @Column(name = "cur_model_des")
    private String curModelDes;

    /**
     * 该ip是否是已经分配的IP 1：已分配；2：未分配；3:未分配但是被放行
     */
    @Column(name = "dis_status")
    private Integer disStatus;

    /**
     * IP是否在线;1:在线；2；不在线
     */
    @Column(name = "alive")
    private Integer alive;

    /**
     * 最后在线时间
     */
    @Column(name = "alive_last_time")
    private String aliveLastTime;

    /**
     * onvif服务地址(摄像机才有)
     */
    @Column(name = "onvif_addr")
    private String onvifAddr;

    /**
     * 是否哑终端 0：活跃终端；1：哑终端
     */
    @Column(name = "mute")
    private Integer mute;

    /**
     * 测试任务6的最后时间
     */
    @Column(name = "task6_last_time")
    private Date task6LastTime;

    /**
     * 服务器操作系统版本
     */
    @Column(name = "server_os")
    private String serverOs;

    /**
     * vlan号
     */
    @Column(name = "vlan")
    private Integer vlan;

    /**
     * 大区
     */
    @Column(name = "region_first")
    private Integer regionFirst;

    /**
     * 小区
     */
    @Column(name = "region_second")
    private Integer regionSecond;

    /**
     * 地区代码
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * MAC对应的临时端口号(12800系列)
     */
    @Column(name = "mac_out")
    private Integer macOut;

    /**
     * 是否检测过  0：未检测 1：已经检测
     */
    @Column(name = "check_open_port")
    private Integer checkOpenPort;

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

    /**
     * 设备型号是否改变 0：未改变；1：改变
     */
    @Column(name = "model_des_change")
    private Integer modelDesChange;

    /**
     * MAC地址是否改变 0：未改变；1：改变
     */
    @Column(name = "mac_change")
    private Integer macChange;

    /**
     * 入网路径是否改变 0：未改变；1：改变
     */
    @Column(name = "arp_out_change")
    private Integer arpOutChange;

    /**
     * 开放端口是否改变 0：未改变；1：改变
     */
    @Column(name = "open_port_change")
    private Integer openPortChange;

    /**
     * 被阻断了的MAC地址
     */
    @Column(name = "mac_addr_block")
    private String macAddrBlock;


    @Column(name = "open_port_all")
    private String openPortAll;


    @Column(name = "project_name")
    private String projectName;

    @Column(name = "point_name")
    private String pointName;

    @Column(name = "project_status")
    private Integer projectStatus;

    @Column(name = "maintain_company")
    private String maintainCompany;

    private String contractor;

    @Column(name = "contractor_person")
    private String contractorPerson;

    @Column(name = "contractor_phone")
    private String contractorPhone;

    @Column(name = "maintain_person")
    private String maintainPerson;

    @Column(name = "maintain_phone")
    private String maintainPhone;

    @Column(name = "confirm_status")
    private Integer confirmStatus;

    private String editor;

    /**
     * 风险类型
     * 0，无风险
     * 1，私接
     * 2，伪造
     * 3，替换
     * 4，其它
     */
    @Column(name = "risk_type")
    private Integer riskType;

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
     * @return ip_addr
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * @param ipAddr
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    /**
     * @return mac_addr
     */
    public String getMacAddr() {
        return macAddr;
    }

    /**
     * @param macAddr
     */
    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr == null ? null : macAddr.trim();
    }

    /**
     * @return sw_ip
     */
    public String getSwIp() {
        return swIp;
    }

    /**
     * @param swIp
     */
    public void setSwIp(String swIp) {
        this.swIp = swIp == null ? null : swIp.trim();
    }

    /**
     * 获取开放端口列表
     *
     * @return open_port - 开放端口列表
     */
    public String getOpenPort() {
        return openPort;
    }

    /**
     * 设置开放端口列表
     *
     * @param openPort 开放端口列表
     */
    public void setOpenPort(String openPort) {
        this.openPort = openPort == null ? null : openPort.trim();
    }

    /**
     * 获取0:初始;2:已拒绝;99:白名单
     *
     * @return ip_status - 0:初始;2:已拒绝;99:白名单
     */
    public Integer getIpStatus() {
        return ipStatus;
    }

    /**
     * 设置0:初始;2:已拒绝;99:白名单
     *
     * @param ipStatus 0:初始;2:已拒绝;99:白名单
     */
    public void setIpStatus(Integer ipStatus) {
        this.ipStatus = ipStatus;
    }

    /**
     * 获取最后扫描时间
     *
     * @return check_last_time - 最后扫描时间
     */
    public Date getCheckLastTime() {
        return checkLastTime;
    }

    /**
     * 设置最后扫描时间
     *
     * @param checkLastTime 最后扫描时间
     */
    public void setCheckLastTime(Date checkLastTime) {
        this.checkLastTime = checkLastTime;
    }

    /**
     * 获取厂商
     *
     * @return company - 厂商
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置厂商
     *
     * @param company 厂商
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * 获取设备类型
     *
     * @return device_type - 设备类型
     */
    public Integer getDeviceType() {
        return deviceType;
    }

    /**
     * 设置设备类型
     *
     * @param deviceType 设备类型
     */
    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }


    /**
     * 获取设备识别等级 0:未识别；1:协议扫描识别; 2:端口扫描识别;
     *
     * @return model_level - 设备识别等级 0:未识别；1:协议扫描识别; 2:端口扫描识别;
     */
    public Integer getModelLevel() {
        return modelLevel;
    }

    /**
     * 设置设备识别等级 0:未识别；1:协议扫描识别; 2:端口扫描识别;
     *
     * @param modelLevel 设备识别等级 0:未识别；1:协议扫描识别; 2:端口扫描识别;
     */
    public void setModelLevel(Integer modelLevel) {
        this.modelLevel = modelLevel;
    }

    /**
     * @return model_des
     */
    public String getModelDes() {
        return modelDes;
    }

    /**
     * @param modelDes
     */
    public void setModelDes(String modelDes) {
        this.modelDes = modelDes == null ? null : modelDes.trim();
    }

    /**
     * 获取上线时间
     *
     * @return online_time - 上线时间
     */
    public Date getOnlineTime() {
        return onlineTime;
    }

    /**
     * 设置上线时间
     *
     * @param onlineTime 上线时间
     */
    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    /**
     * 获取t_arpout表中的主键
     *
     * @return arp_out_id - t_arpout表中的主键
     */
    public Integer getArpOutId() {
        return arpOutId;
    }

    /**
     * 设置t_arpout表中的主键
     *
     * @param arpOutId t_arpout表中的主键
     */
    public void setArpOutId(Integer arpOutId) {
        this.arpOutId = arpOutId;
    }

    /**
     * 获取学习到的物理端口编号
     *
     * @return arp_out_index - 学习到的物理端口编号
     */
    public Integer getArpOutIndex() {
        return arpOutIndex;
    }

    /**
     * 设置学习到的物理端口编号
     *
     * @param arpOutIndex 学习到的物理端口编号
     */
    public void setArpOutIndex(Integer arpOutIndex) {
        this.arpOutIndex = arpOutIndex;
    }

    /**
     * 获取学习到的物理端口
     *
     * @return arp_out_desc - 学习到的物理端口
     */
    public String getArpOutDesc() {
        return arpOutDesc;
    }

    /**
     * 设置学习到的物理端口
     *
     * @param arpOutDesc 学习到的物理端口
     */
    public void setArpOutDesc(String arpOutDesc) {
        this.arpOutDesc = arpOutDesc == null ? null : arpOutDesc.trim();
    }

    /**
     * 获取最后处置时间
     *
     * @return deal_last_time - 最后处置时间
     */
    public Date getDealLastTime() {
        return dealLastTime;
    }

    /**
     * 设置最后处置时间
     *
     * @param dealLastTime 最后处置时间
     */
    public void setDealLastTime(Date dealLastTime) {
        this.dealLastTime = dealLastTime;
    }

    /**
     * 获取最后一次的处理意见
     *
     * @return deal_last_mark - 最后一次的处理意见
     */
    public String getDealLastMark() {
        return dealLastMark;
    }

    /**
     * 设置最后一次的处理意见
     *
     * @param dealLastMark 最后一次的处理意见
     */
    public void setDealLastMark(String dealLastMark) {
        this.dealLastMark = dealLastMark == null ? null : dealLastMark.trim();
    }

    /**
     * 获取当前MAC地址
     *
     * @return cur_mac_addr - 当前MAC地址
     */
    public String getCurMacAddr() {
        return curMacAddr;
    }

    /**
     * 设置当前MAC地址
     *
     * @param curMacAddr 当前MAC地址
     */
    public void setCurMacAddr(String curMacAddr) {
        this.curMacAddr = curMacAddr == null ? null : curMacAddr.trim();
    }

    /**
     * 获取当前公司名称
     *
     * @return cur_company - 当前公司名称
     */
    public String getCurCompany() {
        return curCompany;
    }

    /**
     * 设置当前公司名称
     *
     * @param curCompany 当前公司名称
     */
    public void setCurCompany(String curCompany) {
        this.curCompany = curCompany == null ? null : curCompany.trim();
    }

    /**
     * 获取当前开发端口
     *
     * @return cur_open_port - 当前开发端口
     */
    public String getCurOpenPort() {
        return curOpenPort;
    }

    /**
     * 设置当前开发端口
     *
     * @param curOpenPort 当前开发端口
     */
    public void setCurOpenPort(String curOpenPort) {
        this.curOpenPort = curOpenPort == null ? null : curOpenPort.trim();
    }

    /**
     * @return cur_arp_out_index
     */
    public Integer getCurArpOutIndex() {
        return curArpOutIndex;
    }

    /**
     * @param curArpOutIndex
     */
    public void setCurArpOutIndex(Integer curArpOutIndex) {
        this.curArpOutIndex = curArpOutIndex;
    }

    /**
     * @return cur_arp_out_desc
     */
    public String getCurArpOutDesc() {
        return curArpOutDesc;
    }

    /**
     * @param curArpOutDesc
     */
    public void setCurArpOutDesc(String curArpOutDesc) {
        this.curArpOutDesc = curArpOutDesc == null ? null : curArpOutDesc.trim();
    }

    /**
     * 获取当前设备类别
     *
     * @return cur_model - 当前设备类别
     */
    public Integer getCurModel() {
        return curModel;
    }

    /**
     * 设置当前设备类别
     *
     * @param curModel 当前设备类别
     */
    public void setCurModel(Integer curModel) {
        this.curModel = curModel;
    }

    /**
     * 获取如果设备型号有改变，记录新的设备信号
     *
     * @return cur_model_des - 如果设备型号有改变，记录新的设备信号
     */
    public String getCurModelDes() {
        return curModelDes;
    }

    /**
     * 设置如果设备型号有改变，记录新的设备信号
     *
     * @param curModelDes 如果设备型号有改变，记录新的设备信号
     */
    public void setCurModelDes(String curModelDes) {
        this.curModelDes = curModelDes == null ? null : curModelDes.trim();
    }

    /**
     * 获取该ip是否是已经分配的IP 1：已分配；2：未分配；3:未分配但是被放行
     *
     * @return dis_status - 该ip是否是已经分配的IP 1：已分配；2：未分配；3:未分配但是被放行
     */
    public Integer getDisStatus() {
        return disStatus;
    }

    /**
     * 设置该ip是否是已经分配的IP 1：已分配；2：未分配；3:未分配但是被放行
     *
     * @param disStatus 该ip是否是已经分配的IP 1：已分配；2：未分配；3:未分配但是被放行
     */
    public void setDisStatus(Integer disStatus) {
        this.disStatus = disStatus;
    }

    /**
     * 获取IP是否在线;1:在线；2；不在线
     *
     * @return is_alive - IP是否在线;1:在线；2；不在线
     */
    public Integer getAlive() {
        return alive;
    }

    /**
     * 设置IP是否在线;1:在线；2；不在线
     *
     * @param alive IP是否在线;1:在线；2；不在线
     */
    public void setAlive(Integer alive) {
        this.alive = alive;
    }

    /**
     * 获取最后在线时间
     *
     * @return alive_last_time - 最后在线时间
     */
    public String getAliveLastTime() {
        return aliveLastTime;
    }

    /**
     * 设置最后在线时间
     *
     * @param aliveLastTime 最后在线时间
     */
    public void setAliveLastTime(String aliveLastTime) {
        this.aliveLastTime = aliveLastTime == null ? null : aliveLastTime.trim();
    }

    /**
     * 获取onvif服务地址(摄像机才有)
     *
     * @return onvif_addr - onvif服务地址(摄像机才有)
     */
    public String getOnvifAddr() {
        return onvifAddr;
    }

    /**
     * 设置onvif服务地址(摄像机才有)
     *
     * @param onvifAddr onvif服务地址(摄像机才有)
     */
    public void setOnvifAddr(String onvifAddr) {
        this.onvifAddr = onvifAddr == null ? null : onvifAddr.trim();
    }

    /**
     * 获取是否哑终端 0：活跃终端；1：哑终端
     *
     * @return mute - 是否哑终端 0：活跃终端；1：哑终端
     */
    public Integer geMute() {
        return mute;
    }

    /**
     * 设置是否哑终端 0：活跃终端；1：哑终端
     *
     * @param mute 是否哑终端 0：活跃终端；1：哑终端
     */
    public void setMute(Integer mute) {
        this.mute = mute;
    }

    /**
     * 获取测试任务6的最后时间
     *
     * @return task6_last_time - 测试任务6的最后时间
     */
    public Date getTask6LastTime() {
        return task6LastTime;
    }

    /**
     * 设置测试任务6的最后时间
     *
     * @param task6LastTime 测试任务6的最后时间
     */
    public void setTask6LastTime(Date task6LastTime) {
        this.task6LastTime = task6LastTime;
    }

    /**
     * 获取服务器操作系统版本
     *
     * @return server_os - 服务器操作系统版本
     */
    public String getServerOs() {
        return serverOs;
    }

    /**
     * 设置服务器操作系统版本
     *
     * @param serverOs 服务器操作系统版本
     */
    public void setServerOs(String serverOs) {
        this.serverOs = serverOs == null ? null : serverOs.trim();
    }

    /**
     * 获取vlan号
     *
     * @return vlan - vlan号
     */
    public Integer getVlan() {
        return vlan;
    }

    /**
     * 设置vlan号
     *
     * @param vlan vlan号
     */
    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    /**
     * 获取地区代码
     *
     * @return area_id - 地区代码
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * 设置地区代码
     *
     * @param areaId 地区代码
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取MAC对应的临时端口号(12800系列)
     *
     * @return mac_out - MAC对应的临时端口号(12800系列)
     */
    public Integer getMacOut() {
        return macOut;
    }

    /**
     * 设置MAC对应的临时端口号(12800系列)
     *
     * @param macOut MAC对应的临时端口号(12800系列)
     */
    public void setMacOut(Integer macOut) {
        this.macOut = macOut;
    }

    /**
     * 获取是否检测过  0：未检测 1：已经检测
     *
     * @return check_open_port - 是否检测过  0：未检测 1：已经检测
     */
    public Integer getCheckOpenPort() {
        return checkOpenPort;
    }

    /**
     * 设置是否检测过  0：未检测 1：已经检测
     *
     * @param checkOpenPort 是否检测过  0：未检测 1：已经检测
     */
    public void setCheckOpenPort(Integer checkOpenPort) {
        this.checkOpenPort = checkOpenPort;
    }

    /**
     * 获取设备序列号
     *
     * @return serial_no - 设备序列号
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * 设置设备序列号
     *
     * @param serialNo 设备序列号
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
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
     * 获取协议扫描到的MAC地址
     *
     * @return mac_scan - 协议扫描到的MAC地址
     */
    public String getMacScan() {
        return macScan;
    }

    /**
     * 设置协议扫描到的MAC地址
     *
     * @param macScan 协议扫描到的MAC地址
     */
    public void setMacScan(String macScan) {
        this.macScan = macScan == null ? null : macScan.trim();
    }

    /**
     * 获取设备型号是否改变 0：未改变；1：改变
     *
     * @return model_des_change - 设备型号是否改变 0：未改变；1：改变
     */
    public Integer getModelDesChange() {
        return modelDesChange;
    }

    /**
     * 设置设备型号是否改变 0：未改变；1：改变
     *
     * @param modelDesChange 设备型号是否改变 0：未改变；1：改变
     */
    public void setModelDesChange(Integer modelDesChange) {
        this.modelDesChange = modelDesChange;
    }

    /**
     * 获取MAC地址是否改变 0：未改变；1：改变
     *
     * @return mac_change - MAC地址是否改变 0：未改变；1：改变
     */
    public Integer getMacChange() {
        return macChange;
    }

    /**
     * 设置MAC地址是否改变 0：未改变；1：改变
     *
     * @param macChange MAC地址是否改变 0：未改变；1：改变
     */
    public void setMacChange(Integer macChange) {
        this.macChange = macChange;
    }

    /**
     * 获取入网路径是否改变 0：未改变；1：改变
     *
     * @return arp_out_change - 入网路径是否改变 0：未改变；1：改变
     */
    public Integer getArpOutChange() {
        return arpOutChange;
    }

    /**
     * 设置入网路径是否改变 0：未改变；1：改变
     *
     * @param arpOutChange 入网路径是否改变 0：未改变；1：改变
     */
    public void setArpOutChange(Integer arpOutChange) {
        this.arpOutChange = arpOutChange;
    }

    /**
     * 获取开放端口是否改变 0：未改变；1：改变
     *
     * @return open_port_change - 开放端口是否改变 0：未改变；1：改变
     */
    public Integer getOpenPortChange() {
        return openPortChange;
    }

    /**
     * 设置开放端口是否改变 0：未改变；1：改变
     *
     * @param openPortChange 开放端口是否改变 0：未改变；1：改变
     */
    public void setOpenPortChange(Integer openPortChange) {
        this.openPortChange = openPortChange;
    }

    /**
     * 获取被阻断了的MAC地址
     *
     * @return mac_addr_block - 被阻断了的MAC地址
     */
    public String getMacAddrBlock() {
        return macAddrBlock;
    }

    /**
     * 设置被阻断了的MAC地址
     *
     * @param macAddrBlock 被阻断了的MAC地址
     */
    public void setMacAddrBlock(String macAddrBlock) {
        this.macAddrBlock = macAddrBlock == null ? null : macAddrBlock.trim();
    }

    /**
     * @return open_port_all
     */
    public String getOpenPortAll() {
        return openPortAll;
    }

    /**
     * @param openPortAll
     */
    public void setOpenPortAll(String openPortAll) {
        this.openPortAll = openPortAll == null ? null : openPortAll.trim();
    }

    public Integer getMute() {
        return mute;
    }

    public Integer getRiskType() {
        return riskType;
    }

    public void setRiskType(Integer riskType) {
        this.riskType = riskType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getRegionFirst() {
        return regionFirst;
    }

    public void setRegionFirst(Integer regionFirst) {
        this.regionFirst = regionFirst;
    }

    public Integer getRegionSecond() {
        return regionSecond;
    }

    public void setRegionSecond(Integer regionSecond) {
        this.regionSecond = regionSecond;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getContractorPerson() {
        return contractorPerson;
    }

    public void setContractorPerson(String contractorPerson) {
        this.contractorPerson = contractorPerson;
    }

    public String getContractorPhone() {
        return contractorPhone;
    }

    public void setContractorPhone(String contractorPhone) {
        this.contractorPhone = contractorPhone;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getMaintainCompany() {
        return maintainCompany;
    }

    public void setMaintainCompany(String maintainCompany) {
        this.maintainCompany = maintainCompany;
    }

    public String getMaintainPerson() {
        return maintainPerson;
    }

    public void setMaintainPerson(String maintainPerson) {
        this.maintainPerson = maintainPerson;
    }

    public String getMaintainPhone() {
        return maintainPhone;
    }

    public void setMaintainPhone(String maintainPhone) {
        this.maintainPhone = maintainPhone;
    }

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Override
    public String toString() {
        return "Arp{" +
                "id=" + id +
                ", ipAddr='" + ipAddr + '\'' +
                ", macAddr='" + macAddr + '\'' +
                ", swIp='" + swIp + '\'' +
                ", openPort='" + openPort + '\'' +
                ", ipStatus=" + ipStatus +
                ", checkLastTime=" + checkLastTime +
                ", company='" + company + '\'' +
                ", brand='" + brand + '\'' +
                ", deviceType=" + deviceType +
                ", modelLevel=" + modelLevel +
                ", modelDes='" + modelDes + '\'' +
                ", onlineTime=" + onlineTime +
                ", arpOutId=" + arpOutId +
                ", arpOutIndex=" + arpOutIndex +
                ", arpOutDesc='" + arpOutDesc + '\'' +
                ", dealLastTime=" + dealLastTime +
                ", dealLastMark='" + dealLastMark + '\'' +
                ", curMacAddr='" + curMacAddr + '\'' +
                ", curCompany='" + curCompany + '\'' +
                ", curOpenPort='" + curOpenPort + '\'' +
                ", curArpOutIndex=" + curArpOutIndex +
                ", curArpOutDesc='" + curArpOutDesc + '\'' +
                ", curModel=" + curModel +
                ", curModelDes='" + curModelDes + '\'' +
                ", disStatus=" + disStatus +
                ", alive=" + alive +
                ", aliveLastTime='" + aliveLastTime + '\'' +
                ", onvifAddr='" + onvifAddr + '\'' +
                ", mute=" + mute +
                ", task6LastTime=" + task6LastTime +
                ", serverOs='" + serverOs + '\'' +
                ", vlan=" + vlan +
                ", regionFirst=" + regionFirst +
                ", regionSecond=" + regionSecond +
                ", areaId=" + areaId +
                ", macOut=" + macOut +
                ", checkOpenPort=" + checkOpenPort +
                ", serialNo='" + serialNo + '\'' +
                ", version='" + version + '\'' +
                ", macScan='" + macScan + '\'' +
                ", modelDesChange=" + modelDesChange +
                ", macChange=" + macChange +
                ", arpOutChange=" + arpOutChange +
                ", openPortChange=" + openPortChange +
                ", macAddrBlock='" + macAddrBlock + '\'' +
                ", openPortAll='" + openPortAll + '\'' +
                ", projectName='" + projectName + '\'' +
                ", pointName='" + pointName + '\'' +
                ", projectStatus=" + projectStatus +
                ", maintainCompany='" + maintainCompany + '\'' +
                ", contractor='" + contractor + '\'' +
                ", contractorPerson='" + contractorPerson + '\'' +
                ", contractorPhone='" + contractorPhone + '\'' +
                ", maintainPerson='" + maintainPerson + '\'' +
                ", maintainPhone='" + maintainPhone + '\'' +
                ", confirmStatus=" + confirmStatus +
                ", riskType=" + riskType +
                '}';
    }
}