package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_area_nas")
public class AreaNas implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     * 接入的交换机地址
     */
    @Column(name = "nas_sw_ip")
    private String nasSwIp;

    /**
     * 接入的交换机品牌
     */
    @Column(name = "nas_sw_brand")
    private Integer nasSwBrand;

    /**
     * 接入的交换机团体名
     */
    @Column(name = "nas_community")
    private String nasCommunity;

    /**
     * 接入点描述
     */
    @Column(name = "nas_desc")
    private String nasDesc;

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
     * 获取接入的交换机地址
     *
     * @return nas_switcher - 接入的交换机地址
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
     * 获取接入的交换机团体名
     *
     * @return nas_community - 接入的交换机团体名
     */
    public String getNasCommunity() {
        return nasCommunity;
    }

    /**
     * 设置接入的交换机团体名
     *
     * @param nasCommunity 接入的交换机团体名
     */
    public void setNasCommunity(String nasCommunity) {
        this.nasCommunity = nasCommunity == null ? null : nasCommunity.trim();
    }

    /**
     * 获取接入点描述
     *
     * @return nas_desc - 接入点描述
     */
    public String getNasDesc() {
        return nasDesc;
    }

    /**
     * 设置接入点描述
     *
     * @param nasDesc 接入点描述
     */
    public void setNasDesc(String nasDesc) {
        this.nasDesc = nasDesc == null ? null : nasDesc.trim();
    }

    public Integer getNasSwBrand() {
        return nasSwBrand;
    }

    public void setNasSwBrand(Integer nasSwBrand) {
        this.nasSwBrand = nasSwBrand;
    }

    @Override
    public String toString() {
        return "AreaNas{" +
                "id=" + id +
                ", area=" + area +
                ", nasIp='" + nasIp + '\'' +
                ", nasSwIp='" + nasSwIp + '\'' +
                ", nasSwBrand=" + nasSwBrand +
                ", nasCommunity='" + nasCommunity + '\'' +
                ", nasDesc='" + nasDesc + '\'' +
                '}';
    }

}