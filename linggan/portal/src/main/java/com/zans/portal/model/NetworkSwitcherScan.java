package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "network_switcher_scan")
public class NetworkSwitcherScan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * 交换机系统名称
     */
    @Column(name = "sys_name")
    private String sysName;

    /**
     * 交换机型号及版本信息
     */
    @Column(name = "sys_desc")
    private String sysDesc;

    /**
     * 系统上线时间，扫描值
     */
    @Column(name = "sys_up_time")
    private String sysUpTime;

    /**
     * 上线状态, 1,在线；2,离线
     */
    private Integer alive;

    /**
     * 状态持续时长，单位s
     */
    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    /**
     * 状态开始时间
     */
    @Column(name = "begin_time")
    private String beginTime;

    /**
     * 状态结束时间
     */
    @Column(name = "end_time")
    private String endTime;

    /**
     * 扫描时间
     */
    @Column(name = "scan_time")
    private Date scanTime;

    @Column(name = "offline_type")
    private Integer offlineType;

    @Column(name = "data_source")
    private Integer dataSource;

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
     * 获取交换机系统名称
     *
     * @return sys_name - 交换机系统名称
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * 设置交换机系统名称
     *
     * @param sysName 交换机系统名称
     */
    public void setSysName(String sysName) {
        this.sysName = sysName == null ? null : sysName.trim();
    }

    /**
     * 获取交换机型号及版本信息
     *
     * @return sys_desc - 交换机型号及版本信息
     */
    public String getSysDesc() {
        return sysDesc;
    }

    /**
     * 设置交换机型号及版本信息
     *
     * @param sysDesc 交换机型号及版本信息
     */
    public void setSysDesc(String sysDesc) {
        this.sysDesc = sysDesc == null ? null : sysDesc.trim();
    }

    /**
     * 获取系统上线时间，扫描值
     *
     * @return sys_up_time - 系统上线时间，扫描值
     */
    public String getSysUpTime() {
        return sysUpTime;
    }

    /**
     * 设置系统上线时间，扫描值
     *
     * @param sysUpTime 系统上线时间，扫描值
     */
    public void setSysUpTime(String sysUpTime) {
        this.sysUpTime = sysUpTime == null ? null : sysUpTime.trim();
    }

    /**
     * 获取上线状态, 1,在线；2,离线
     *
     * @return alive - 上线状态, 1,在线；2,离线
     */
    public Integer getAlive() {
        return alive;
    }

    /**
     * 设置上线状态, 1,在线；2,离线
     *
     * @param alive 上线状态, 1,在线；2,离线
     */
    public void setAlive(Integer alive) {
        this.alive = alive;
    }

    /**
     * 获取状态持续时长，单位s
     *
     * @return duration_seconds - 状态持续时长，单位s
     */
    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * 设置状态持续时长，单位s
     *
     * @param durationSeconds 状态持续时长，单位s
     */
    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    /**
     * 获取状态开始时间
     *
     * @return begin_time - 状态开始时间
     */
    public String getBeginTime() {
        return beginTime;
    }

    /**
     * 设置状态开始时间
     *
     * @param beginTime 状态开始时间
     */
    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * 获取状态结束时间
     *
     * @return end_time - 状态结束时间
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 设置状态结束时间
     *
     * @param endTime 状态结束时间
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取扫描时间
     *
     * @return scan_time - 扫描时间
     */
    public Date getScanTime() {
        return scanTime;
    }

    /**
     * 设置扫描时间
     *
     * @param scanTime 扫描时间
     */
    public void setScanTime(Date scanTime) {
        this.scanTime = scanTime;
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
        sb.append(", sysName=").append(sysName);
        sb.append(", sysDesc=").append(sysDesc);
        sb.append(", sysUpTime=").append(sysUpTime);
        sb.append(", alive=").append(alive);
        sb.append(", durationSeconds=").append(durationSeconds);
        sb.append(", beginTime=").append(beginTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", scanTime=").append(scanTime);
        sb.append("]");
        return sb.toString();
    }

    public Integer getOfflineType() {
        return offlineType;
    }

    public void setOfflineType(Integer offlineType) {
        this.offlineType = offlineType;
    }

    public Integer getDataSource() {
        return dataSource;
    }

    public void setDataSource(Integer dataSource) {
        this.dataSource = dataSource;
    }
}
