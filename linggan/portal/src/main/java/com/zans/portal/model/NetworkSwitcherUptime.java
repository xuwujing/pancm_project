package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "network_switcher_uptime")
public class NetworkSwitcherUptime implements Serializable {
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
     * 上线状态, 1,在线；2,离线
     */
    private Boolean alive;

    /**
     * 状态持续时长，单位s
     */
    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    /**
     * 状态开始时间
     */
    @Column(name = "begin_time")
    private Date beginTime;

    /**
     * 状态结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取上线状态, 1,在线；2,离线
     *
     * @return alive - 上线状态, 1,在线；2,离线
     */
    public Boolean getAlive() {
        return alive;
    }

    /**
     * 设置上线状态, 1,在线；2,离线
     *
     * @param alive 上线状态, 1,在线；2,离线
     */
    public void setAlive(Boolean alive) {
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
    public Date getBeginTime() {
        return beginTime;
    }

    /**
     * 设置状态开始时间
     *
     * @param beginTime 状态开始时间
     */
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * 获取状态结束时间
     *
     * @return end_time - 状态结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置状态结束时间
     *
     * @param endTime 状态结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
        sb.append(", swId=").append(swId);
        sb.append(", swIp=").append(swIp);
        sb.append(", alive=").append(alive);
        sb.append(", durationSeconds=").append(durationSeconds);
        sb.append(", beginTime=").append(beginTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}