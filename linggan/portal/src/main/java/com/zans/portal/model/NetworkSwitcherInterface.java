package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "network_switcher_interface")
public class NetworkSwitcherInterface implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * sys_switcher.id
     */
    @Column(name = "sw_id")
    private Integer swId;

    /**
     * 交换机IP
     */
    @Column(name = "sw_ip")
    private String swIp;

    /**
     * 接口编号
     */
    @Column(name = "interface_index")
    private Integer interfaceIndex;

    /**
     * 接口描述
     */
    @Column(name = "interface_detail")
    private String interfaceDetail;

    /**
     * MAC相关的物理接口编号，排除null、vlan等接口
     */
    @Column(name = "mac_index")
    private Integer macIndex;

    /**
     * 端口状态：1up，2down
     */
    @Column(name = "up_status")
    private Boolean upStatus;

    /**
     * 接口mac地址
     */
    private String mac;

    /**
     * 接口类型：1up上行口，2down下行口;
     */
    @Column(name = "stream_type")
    private Boolean streamType;


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
     * 获取交换机IP
     *
     * @return sw_ip - 交换机IP
     */
    public String getSwIp() {
        return swIp;
    }

    /**
     * 设置交换机IP
     *
     * @param swIp 交换机IP
     */
    public void setSwIp(String swIp) {
        this.swIp = swIp == null ? null : swIp.trim();
    }

    /**
     * 获取接口编号
     *
     * @return interface_index - 接口编号
     */
    public Integer getInterfaceIndex() {
        return interfaceIndex;
    }

    /**
     * 设置接口编号
     *
     * @param interfaceIndex 接口编号
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
     * 获取MAC相关的物理接口编号，排除null、vlan等接口
     *
     * @return mac_index - MAC相关的物理接口编号，排除null、vlan等接口
     */
    public Integer getMacIndex() {
        return macIndex;
    }

    /**
     * 设置MAC相关的物理接口编号，排除null、vlan等接口
     *
     * @param macIndex MAC相关的物理接口编号，排除null、vlan等接口
     */
    public void setMacIndex(Integer macIndex) {
        this.macIndex = macIndex;
    }

    /**
     * 获取端口状态：1up，2down
     *
     * @return up_status - 端口状态：1up，2down
     */
    public Boolean getUpStatus() {
        return upStatus;
    }

    /**
     * 设置端口状态：1up，2down
     *
     * @param upStatus 端口状态：1up，2down
     */
    public void setUpStatus(Boolean upStatus) {
        this.upStatus = upStatus;
    }

    /**
     * 获取接口mac地址
     *
     * @return mac - 接口mac地址
     */
    public String getMac() {
        return mac;
    }

    /**
     * 设置接口mac地址
     *
     * @param mac 接口mac地址
     */
    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }

    /**
     * 获取接口类型：1up上行口，2down下行口;
     *
     * @return stream_type - 接口类型：1up上行口，2down下行口;
     */
    public Boolean getStreamType() {
        return streamType;
    }

    /**
     * 设置接口类型：1up上行口，2down下行口;
     *
     * @param streamType 接口类型：1up上行口，2down下行口;
     */
    public void setStreamType(Boolean streamType) {
        this.streamType = streamType;
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
        sb.append(", macIndex=").append(macIndex);
        sb.append(", upStatus=").append(upStatus);
        sb.append(", mac=").append(mac);
        sb.append(", streamType=").append(streamType);
        sb.append("]");
        return sb.toString();
    }
}
