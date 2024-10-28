package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "asset_scan_day")
public class AssetScanDay implements Serializable {
    @Id
    private Long id;

    /**
     * IP
     */
    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * 年月日
     */
    private String yyyymmdd;

    /**
     * 申述
     */
    @Column(name = "approve_duration")
    private Integer approveDuration;

    /**
     * 离线断电
     */
    @Column(name = "offline_duration")
    private Integer offlineDuration;

    /**
     * 断光时长
     */
    @Column(name = "affline_duration")
    private Integer afflineDuration;

    /**
     * 在线时长
     */
    @Column(name = "alive_duration")
    private Integer aliveDuration;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取IP
     *
     * @return ip_addr - IP
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * 设置IP
     *
     * @param ipAddr IP
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    /**
     * 获取年月日
     *
     * @return yyyymmdd - 年月日
     */
    public String getYyyymmdd() {
        return yyyymmdd;
    }

    /**
     * 设置年月日
     *
     * @param yyyymmdd 年月日
     */
    public void setYyyymmdd(String yyyymmdd) {
        this.yyyymmdd = yyyymmdd == null ? null : yyyymmdd.trim();
    }

    /**
     * 获取申述
     *
     * @return approve_duration - 申述
     */
    public Integer getApproveDuration() {
        return approveDuration;
    }

    /**
     * 设置申述
     *
     * @param approveDuration 申述
     */
    public void setApproveDuration(Integer approveDuration) {
        this.approveDuration = approveDuration;
    }

    /**
     * 获取离线断电
     *
     * @return offline_duration - 离线断电
     */
    public Integer getOfflineDuration() {
        return offlineDuration;
    }

    /**
     * 设置离线断电
     *
     * @param offlineDuration 离线断电
     */
    public void setOfflineDuration(Integer offlineDuration) {
        this.offlineDuration = offlineDuration;
    }

    /**
     * 获取断光时长
     *
     * @return affline_duration - 断光时长
     */
    public Integer getAfflineDuration() {
        return afflineDuration;
    }

    /**
     * 设置断光时长
     *
     * @param afflineDuration 断光时长
     */
    public void setAfflineDuration(Integer afflineDuration) {
        this.afflineDuration = afflineDuration;
    }

    /**
     * 获取在线时长
     *
     * @return alive_duration - 在线时长
     */
    public Integer getAliveDuration() {
        return aliveDuration;
    }

    /**
     * 设置在线时长
     *
     * @param aliveDuration 在线时长
     */
    public void setAliveDuration(Integer aliveDuration) {
        this.aliveDuration = aliveDuration;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ipAddr=").append(ipAddr);
        sb.append(", yyyymmdd=").append(yyyymmdd);
        sb.append(", approveDuration=").append(approveDuration);
        sb.append(", offlineDuration=").append(offlineDuration);
        sb.append(", afflineDuration=").append(afflineDuration);
        sb.append(", aliveDuration=").append(aliveDuration);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}