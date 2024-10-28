package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "sys_ip_segment")
public class SysIpSegment implements Serializable {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * IP段名称
     */
    private String name;

    /**
     * 地址段的IP数
     */
    @Column(name = "ip_count")
    private Integer ipCount;

    /**
     * 所属的交换机IP
     */
    @Column(name = "sw_ip")
    private String swIp;

    /**
     * 标签, 0系统输入；1/2/3，交换机扫描
     */
    private Integer tag;

    /**
     * 0, 禁用； 1启用
     */
    private Integer enable;

    /**
     * 地址段，多行用回车分隔，两种格式，192.168.0.0/24 or 192.168.0.0-192.168.0.254
     */
    private String segment;

    /**
     * @description: 与北辰、北鹏 确认不存在此字段  WLW-276
     * edit by ns_wang 2020/10/13 15:57
     */
 //   private String segmentShort;

    private static final long serialVersionUID = 1L;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }



    /**
     * 获取IP段名称
     *
     * @return name - IP段名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置IP段名称
     *
     * @param name IP段名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取地址段的IP数
     *
     * @return ip_count - 地址段的IP数
     */
    public Integer getIpCount() {
        return ipCount;
    }

    /**
     * 设置地址段的IP数
     *
     * @param ipCount 地址段的IP数
     */
    public void setIpCount(Integer ipCount) {
        this.ipCount = ipCount;
    }

    /**
     * 获取所属的交换机IP
     *
     * @return sw_ip - 所属的交换机IP
     */
    public String getSwIp() {
        return swIp;
    }

    /**
     * 设置所属的交换机IP
     *
     * @param swIp 所属的交换机IP
     */
    public void setSwIp(String swIp) {
        this.swIp = swIp == null ? null : swIp.trim();
    }

    /**
     * 获取标签, 0系统输入；1/2/3，交换机扫描
     *
     * @return tag - 标签, 0系统输入；1/2/3，交换机扫描
     */
    public Integer getTag() {
        return tag;
    }

    /**
     * 设置标签, 0系统输入；1/2/3，交换机扫描
     *
     * @param tag 标签, 0系统输入；1/2/3，交换机扫描
     */
    public void setTag(Integer tag) {
        this.tag = tag;
    }

    /**
     * 获取0, 禁用； 1启用
     *
     * @return enable - 0, 禁用； 1启用
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * 设置0, 禁用； 1启用
     *
     * @param enable 0, 禁用； 1启用
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    /**
     * 获取地址段，多行用回车分隔，两种格式，192.168.0.0/24 or 192.168.0.0-192.168.0.254
     *
     * @return segment - 地址段，多行用回车分隔，两种格式，192.168.0.0/24 or 192.168.0.0-192.168.0.254
     */
    public String getSegment() {
        return segment;
    }

    /**
     * 设置地址段，多行用回车分隔，两种格式，192.168.0.0/24 or 192.168.0.0-192.168.0.254
     *
     * @param segment 地址段，多行用回车分隔，两种格式，192.168.0.0/24 or 192.168.0.0-192.168.0.254
     */
    public void setSegment(String segment) {
        this.segment = segment == null ? null : segment.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", ipCount=").append(ipCount);
        sb.append(", swIp=").append(swIp);
        sb.append(", tag=").append(tag);
        sb.append(", enable=").append(enable);
        sb.append(", segment=").append(segment);
        sb.append("]");
        return sb.toString();
    }
}
