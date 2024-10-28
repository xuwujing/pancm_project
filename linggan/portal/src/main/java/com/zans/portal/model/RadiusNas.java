package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "radius_nas")
public class RadiusNas implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 接入点描述
     */
    private String name;

    /**
     * 区域id
     */
    private Integer area;

    /**
     * 接入点IP地址
     */
    @Column(name = "nas_ip")
    private String nasIp;

    /**
     * 接入点密码
     */
    private String secret;

    /**
     * 接入点shortname
     */
    @Column(name = "short_name")
    private String shortName;

    /**
     * nas_type
     */
    @Column(name = "nas_type")
    private String nasType;

    /**
     * 接入的交换机地址
     */
    @Column(name = "nas_sw_ip")
    private String nasSwIp;

    /**
     * 删除状态
     */
    @Column(name = "delete_status")
    private Boolean deleteStatus;

    /***
     * CoA/DM端口
     */
    @Column(name = "coa_port")
    private Integer coaPort;

    /***
     * CoA启用禁用
     */
    @Column(name = "coa_enable")
    private Integer coaEnable;

    /***
     * CoA密钥
     */
    @Column(name = "coa_secret")
    private String coaSecret;


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

    public Integer getCoaPort() {
        return coaPort;
    }

    public void setCoaPort(Integer coaPort) {
        this.coaPort = coaPort;
    }

    public Integer getCoaEnable() {
        return coaEnable;
    }

    public void setCoaEnable(Integer coaEnable) {
        this.coaEnable = coaEnable;
    }

    public String getCoaSecret() {
        return coaSecret;
    }

    public void setCoaSecret(String coaSecret) {
        this.coaSecret = coaSecret;
    }

    /**
     * 获取接入点描述
     *
     * @return name - 接入点描述
     */
    public String getName() {
        return name;
    }

    /**
     * 设置接入点描述
     *
     * @param name 接入点描述
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取区域id
     *
     * @return area - 区域id
     */
    public Integer getArea() {
        return area;
    }

    /**
     * 设置区域id
     *
     * @param area 区域id
     */
    public void setArea(Integer area) {
        this.area = area;
    }

    /**
     * 获取接入点IP地址
     *
     * @return nas_ip - 接入点IP地址
     */
    public String getNasIp() {
        return nasIp;
    }

    /**
     * 设置接入点IP地址
     *
     * @param nasIp 接入点IP地址
     */
    public void setNasIp(String nasIp) {
        this.nasIp = nasIp == null ? null : nasIp.trim();
    }

    /**
     * 获取接入点密码
     *
     * @return secret - 接入点密码
     */
    public String getSecret() {
        return secret;
    }

    /**
     * 设置接入点密码
     *
     * @param secret 接入点密码
     */
    public void setSecret(String secret) {
        this.secret = secret == null ? null : secret.trim();
    }

    /**
     * 获取接入点shortname
     *
     * @return short_name - 接入点shortname
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 设置接入点shortname
     *
     * @param shortName 接入点shortname
     */
    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    /**
     * 获取nas_type
     *
     * @return nas_type - nas_type
     */
    public String getNasType() {
        return nasType;
    }

    /**
     * 设置nas_type
     *
     * @param nasType nas_type
     */
    public void setNasType(String nasType) {
        this.nasType = nasType == null ? null : nasType.trim();
    }

    /**
     * 获取接入的交换机地址
     *
     * @return nas_sw_ip - 接入的交换机地址
     */
    public String getNasSwIp() {
        return nasSwIp;
    }

    /**
     * 设置接入的交换机地址
     *
     * @param nasSwIp 接入的交换机地址
     */
    public void setNasSwIp(String nasSwIp) {
        this.nasSwIp = nasSwIp == null ? null : nasSwIp.trim();
    }

    /**
     * 获取删除状态
     *
     * @return delete_status - 删除状态
     */
    public Boolean getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * 设置删除状态
     *
     * @param deleteStatus 删除状态
     */
    public void setDeleteStatus(Boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", area=").append(area);
        sb.append(", nasIp=").append(nasIp);
        sb.append(", secret=").append(secret);
        sb.append(", shortName=").append(shortName);
        sb.append(", nasType=").append(nasType);
        sb.append(", nasSwIp=").append(nasSwIp);
        sb.append(", deleteStatus=").append(deleteStatus);
        sb.append("]");
        return sb.toString();
    }
}