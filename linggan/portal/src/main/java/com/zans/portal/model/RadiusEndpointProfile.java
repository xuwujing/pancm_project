package com.zans.portal.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name = "radius_endpoint_profile")
public class RadiusEndpointProfile implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "endpoint_id")
    private Integer endpointId;

    /**
     * 用户名
     */
    private String mac;



    /**
     * ip，radius填写
     */
    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * 是否在线;1:在线；2；不在线
     */
    private Integer alive;

    /**
     * 离线检查的节点
     */
    @Column(name = "alive_not_node")
    private String aliveNotNode;

    /**
     * 上次在线时间
     */
    @Column(name = "alive_last_time")
    private Date aliveLastTime;

    /**
     * 检疫区是否在线;1:在线；2；不在线
     */
    @Column(name = "alive_qz")
    private Integer aliveQz;

    /**
     * 检疫区上次在线时间
     */
    @Column(name = "alive_qz_last_time")
    private Date aliveQzLastTime;

    /**
     * arp表所在的交换机
     */
    @Column(name = "sw_ip")
    private String swIp;


    /**
     * nas
     */
    @Column(name = "nas_ip_address")
    private String nasIpAddress;

    /**
     * 端口，radius填写
     */
    @Column(name = "nas_port_id")
    private String nasPortId;

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
    private Date acctUpdateTime;

    /**
     * 厂商
     */
    private String company;

    /**
     * 品牌
     */
    @Column(name = "brand_name")
    private String brandName;

    /**
     * 开放端口列表
     */
    @Column(name = "open_port")
    private String openPort;

    /**
     * 是否哑终端 0：活跃终端；1：哑终端
     */
    private Integer mute;

    /**
     * 服务器操作系统版本
     */
    @Column(name = "server_os")
    private String serverOs;

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

    /**
     * 设备型号
     */
    @Column(name = "model_des")
    private String modelDes;

    /**
     * 设备序列号
     */
    @Column(name = "serial_no")
    private String serialNo;

    /**
     * 软件版本
     */
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

    @Column(name = "cur_vlan")
    private String curVlan;

    @Column(name = "cur_open_port")
    private String curOpenPort;

    @Column(name = "cur_mute")
    private Integer curMute;

    @Column(name = "cur_device_type")
    private Integer curDeviceType;

    @Column(name = "cur_model_des")
    private String curModelDes;

    @Column(name = "check_last_time")
    private Date checkLastTime;

    /**
     * 数据来源:0:默认,1:来源风险预警
     */
    private Boolean source;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "open_port_all")
    private String openPortAll;

    @Column(name = "cur_server_os")
    private String curServerOs;

    private static final long serialVersionUID = 1L;


    public String getCurVlan() {
        return curVlan;
    }

    public void setCurVlan(String curVlan) {
        this.curVlan = curVlan;
    }

    /**
     * 获取id
     *
     * @return endpoint_id - id
     */
    public Integer getEndpointId() {
        return endpointId;
    }

    /**
     * 设置id
     *
     * @param endpointId id
     */
    public void setEndpointId(Integer endpointId) {
        this.endpointId = endpointId;
    }


    /**
     * 获取ip，radius填写
     *
     * @return ip_addr - ip，radius填写
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * 设置ip，radius填写
     *
     * @param ipAddr ip，radius填写
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    /**
     * 获取是否在线;1:在线；2；不在线
     *
     * @return alive - 是否在线;1:在线；2；不在线
     */
    public Integer getAlive() {
        return alive;
    }

    /**
     * 设置是否在线;1:在线；2；不在线
     *
     * @param alive 是否在线;1:在线；2；不在线
     */
    public void setAlive(Integer alive) {
        this.alive = alive;
    }

    /**
     * 获取离线检查的节点
     *
     * @return alive_not_node - 离线检查的节点
     */
    public String getAliveNotNode() {
        return aliveNotNode;
    }

    /**
     * 设置离线检查的节点
     *
     * @param aliveNotNode 离线检查的节点
     */
    public void setAliveNotNode(String aliveNotNode) {
        this.aliveNotNode = aliveNotNode == null ? null : aliveNotNode.trim();
    }

    /**
     * 获取上次在线时间
     *
     * @return alive_last_time - 上次在线时间
     */
    public Date getAliveLastTime() {
        return aliveLastTime;
    }

    /**
     * 设置上次在线时间
     *
     * @param aliveLastTime 上次在线时间
     */
    public void setAliveLastTime(Date aliveLastTime) {
        this.aliveLastTime = aliveLastTime;
    }

    /**
     * 获取检疫区是否在线;1:在线；2；不在线
     *
     * @return alive_qz - 检疫区是否在线;1:在线；2；不在线
     */
    public Integer getAliveQz() {
        return aliveQz;
    }

    /**
     * 设置检疫区是否在线;1:在线；2；不在线
     *
     * @param aliveQz 检疫区是否在线;1:在线；2；不在线
     */
    public void setAliveQz(Integer aliveQz) {
        this.aliveQz = aliveQz;
    }

    /**
     * 获取检疫区上次在线时间
     *
     * @return alive_qz_last_time - 检疫区上次在线时间
     */
    public Date getAliveQzLastTime() {
        return aliveQzLastTime;
    }

    /**
     * 设置检疫区上次在线时间
     *
     * @param aliveQzLastTime 检疫区上次在线时间
     */
    public void setAliveQzLastTime(Date aliveQzLastTime) {
        this.aliveQzLastTime = aliveQzLastTime;
    }

    /**
     * 获取arp表所在的交换机
     *
     * @return sw_ip - arp表所在的交换机
     */
    public String getSwIp() {
        return swIp;
    }

    /**
     * 设置arp表所在的交换机
     *
     * @param swIp arp表所在的交换机
     */
    public void setSwIp(String swIp) {
        this.swIp = swIp == null ? null : swIp.trim();
    }



    /**
     * 获取nas
     *
     * @return nas_ip_address - nas
     */
    public String getNasIpAddress() {
        return nasIpAddress;
    }

    /**
     * 设置nas
     *
     * @param nasIpAddress nas
     */
    public void setNasIpAddress(String nasIpAddress) {
        this.nasIpAddress = nasIpAddress == null ? null : nasIpAddress.trim();
    }

    /**
     * 获取端口，radius填写
     *
     * @return nas_port_id - 端口，radius填写
     */
    public String getNasPortId() {
        return nasPortId;
    }

    /**
     * 设置端口，radius填写
     *
     * @param nasPortId 端口，radius填写
     */
    public void setNasPortId(String nasPortId) {
        this.nasPortId = nasPortId == null ? null : nasPortId.trim();
    }

    /**
     * 获取radius reply
     *
     * @return reply_message - radius reply
     */
    public String getReplyMessage() {
        return replyMessage;
    }

    /**
     * 设置radius reply
     *
     * @param replyMessage radius reply
     */
    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage == null ? null : replyMessage.trim();
    }

    /**
     * 获取radius filter-id
     *
     * @return filter_id - radius filter-id
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * 设置radius filter-id
     *
     * @param filterId radius filter-id
     */
    public void setFilterId(String filterId) {
        this.filterId = filterId == null ? null : filterId.trim();
    }

    /**
     * 获取session，radius填写
     *
     * @return acct_session_id - session，radius填写
     */
    public String getAcctSessionId() {
        return acctSessionId;
    }

    /**
     * 设置session，radius填写
     *
     * @param acctSessionId session，radius填写
     */
    public void setAcctSessionId(String acctSessionId) {
        this.acctSessionId = acctSessionId == null ? null : acctSessionId.trim();
    }

    /**
     * 获取session更新时间
     *
     * @return acct_update_time - session更新时间
     */
    public Date getAcctUpdateTime() {
        return acctUpdateTime;
    }

    /**
     * 设置session更新时间
     *
     * @param acctUpdateTime session更新时间
     */
    public void setAcctUpdateTime(Date acctUpdateTime) {
        this.acctUpdateTime = acctUpdateTime;
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
     * 获取品牌
     *
     * @return brand_name - 品牌
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 设置品牌
     *
     * @param brandName 品牌
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
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
     * 获取是否哑终端 0：活跃终端；1：哑终端
     *
     * @return mute - 是否哑终端 0：活跃终端；1：哑终端
     */
    public Integer getMute() {
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
     * 获取设备型号
     *
     * @return model_des - 设备型号
     */
    public String getModelDes() {
        return modelDes;
    }

    /**
     * 设置设备型号
     *
     * @param modelDes 设备型号
     */
    public void setModelDes(String modelDes) {
        this.modelDes = modelDes == null ? null : modelDes.trim();
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
     * @return cur_ip_addr
     */
    public String getCurIpAddr() {
        return curIpAddr;
    }

    /**
     * @param curIpAddr
     */
    public void setCurIpAddr(String curIpAddr) {
        this.curIpAddr = curIpAddr == null ? null : curIpAddr.trim();
    }

    /**
     * @return cur_sw_ip
     */
    public String getCurSwIp() {
        return curSwIp;
    }

    /**
     * @param curSwIp
     */
    public void setCurSwIp(String curSwIp) {
        this.curSwIp = curSwIp == null ? null : curSwIp.trim();
    }



    /**
     * @return cur_nas_ip_address
     */
    public String getCurNasIpAddress() {
        return curNasIpAddress;
    }

    /**
     * @param curNasIpAddress
     */
    public void setCurNasIpAddress(String curNasIpAddress) {
        this.curNasIpAddress = curNasIpAddress == null ? null : curNasIpAddress.trim();
    }

    /**
     * @return cur_nas_port_id
     */
    public String getCurNasPortId() {
        return curNasPortId;
    }

    /**
     * @param curNasPortId
     */
    public void setCurNasPortId(String curNasPortId) {
        this.curNasPortId = curNasPortId == null ? null : curNasPortId.trim();
    }

    /**
     * @return cur_open_port
     */
    public String getCurOpenPort() {
        return curOpenPort;
    }

    /**
     * @param curOpenPort
     */
    public void setCurOpenPort(String curOpenPort) {
        this.curOpenPort = curOpenPort == null ? null : curOpenPort.trim();
    }

    /**
     * @return cur_mute
     */
    public Integer getCurMute() {
        return curMute;
    }

    /**
     * @param curMute
     */
    public void setCurMute(Integer curMute) {
        this.curMute = curMute;
    }

    /**
     * @return cur_device_type
     */
    public Integer getCurDeviceType() {
        return curDeviceType;
    }

    /**
     * @param curDeviceType
     */
    public void setCurDeviceType(Integer curDeviceType) {
        this.curDeviceType = curDeviceType;
    }

    /**
     * @return cur_model_des
     */
    public String getCurModelDes() {
        return curModelDes;
    }

    /**
     * @param curModelDes
     */
    public void setCurModelDes(String curModelDes) {
        this.curModelDes = curModelDes == null ? null : curModelDes.trim();
    }

    /**
     * @return check_last_time
     */
    public Date getCheckLastTime() {
        return checkLastTime;
    }

    /**
     * @param checkLastTime
     */
    public void setCheckLastTime(Date checkLastTime) {
        this.checkLastTime = checkLastTime;
    }

    /**
     * 获取数据来源:0:默认,1:来源风险预警
     *
     * @return source - 数据来源:0:默认,1:来源风险预警
     */
    public Boolean getSource() {
        return source;
    }

    /**
     * 设置数据来源:0:默认,1:来源风险预警
     *
     * @param source 数据来源:0:默认,1:来源风险预警
     */
    public void setSource(Boolean source) {
        this.source = source;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    /**
     * @return cur_server_os
     */
    public String getCurServerOs() {
        return curServerOs;
    }

    /**
     * @param curServerOs
     */
    public void setCurServerOs(String curServerOs) {
        this.curServerOs = curServerOs;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", endpointId=").append(endpointId);
        sb.append(", mac=").append(mac);
        sb.append(", ipAddr=").append(ipAddr);
        sb.append(", alive=").append(alive);
        sb.append(", aliveNotNode=").append(aliveNotNode);
        sb.append(", aliveLastTime=").append(aliveLastTime);
        sb.append(", aliveQz=").append(aliveQz);
        sb.append(", aliveQzLastTime=").append(aliveQzLastTime);
        sb.append(", swIp=").append(swIp);
        sb.append(", nasIpAddress=").append(nasIpAddress);
        sb.append(", nasPortId=").append(nasPortId);
        sb.append(", replyMessage=").append(replyMessage);
        sb.append(", filterId=").append(filterId);
        sb.append(", acctSessionId=").append(acctSessionId);
        sb.append(", acctUpdateTime=").append(acctUpdateTime);
        sb.append(", company=").append(company);
        sb.append(", brandName=").append(brandName);
        sb.append(", openPort=").append(openPort);
        sb.append(", mute=").append(mute);
        sb.append(", serverOs=").append(serverOs);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", modelLevel=").append(modelLevel);
        sb.append(", modelDes=").append(modelDes);
        sb.append(", serialNo=").append(serialNo);
        sb.append(", version=").append(version);
        sb.append(", macScan=").append(macScan);
        sb.append(", curIpAddr=").append(curIpAddr);
        sb.append(", curSwIp=").append(curSwIp);
        sb.append(", curNasIpAddress=").append(curNasIpAddress);
        sb.append(", curNasPortId=").append(curNasPortId);
        sb.append(", curOpenPort=").append(curOpenPort);
        sb.append(", curMute=").append(curMute);
        sb.append(", curDeviceType=").append(curDeviceType);
        sb.append(", curModelDes=").append(curModelDes);
        sb.append(", checkLastTime=").append(checkLastTime);
        sb.append(", source=").append(source);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", openPortAll=").append(openPortAll);
        sb.append(", curServerOs=").append(curServerOs);
        sb.append("]");
        return sb.toString();
    }
}
