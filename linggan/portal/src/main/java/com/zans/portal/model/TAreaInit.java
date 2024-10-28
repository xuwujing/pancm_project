package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_area_init")
public class TAreaInit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "area_id")
    private Integer areaId;

    @Column(name = "device_type_id")
    private Integer deviceTypeId;

    @Column(name = "ip_addr")
    private String ipAddr;

    private String mask;

    private String gateway;

    private String vlan;


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
     * @return area_id
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * @param areaId
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * @return device_type_id
     */
    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    /**
     * @param deviceTypeId
     */
    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
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
     * @return mask
     */
    public String getMask() {
        return mask;
    }

    /**
     * @param mask
     */
    public void setMask(String mask) {
        this.mask = mask == null ? null : mask.trim();
    }

    /**
     * @return gateway
     */
    public String getGateway() {
        return gateway;
    }

    /**
     * @param gateway
     */
    public void setGateway(String gateway) {
        this.gateway = gateway == null ? null : gateway.trim();
    }

    /**
     * @return vlan
     */
    public String getVlan() {
        return vlan;
    }

    /**
     * @param vlan
     */
    public void setVlan(String vlan) {
        this.vlan = vlan == null ? null : vlan.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", areaId=").append(areaId);
        sb.append(", deviceTypeId=").append(deviceTypeId);
        sb.append(", ipAddr=").append(ipAddr);
        sb.append(", mask=").append(mask);
        sb.append(", gateway=").append(gateway);
        sb.append(", vlan=").append(vlan);
        sb.append("]");
        return sb.toString();
    }
}
