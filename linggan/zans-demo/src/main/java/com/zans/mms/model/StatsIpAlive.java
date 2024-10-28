package com.zans.mms.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "stats_ip_alive")
public class StatsIpAlive implements Serializable {
    @Id
    private Long id;

    @Column(name = "ip_addr")
    private String ipAddr;

    @Column(name = "node_id")
    private String nodeId;

    /**
     * 在线，离线
     */
    private String alive;

    /**
     * ping用时，单位s
     */
    private Integer cost;

    @Column(name = "packet_send")
    private Integer packetSend;

    @Column(name = "packet_rev")
    private Integer packetRev;

    /**
     * 百分比
     */
    @Column(name = "packet_loss")
    private Integer packetLoss;

    /**
     * 单位：ms
     */
    @Column(name = "packet_time")
    private Integer packetTime;

    @Column(name = "rtt_min")
    private BigDecimal rttMin;

    @Column(name = "rtt_avg")
    private BigDecimal rttAvg;

    @Column(name = "rtt_max")
    private BigDecimal rttMax;

    @Column(name = "rtt_mdev")
    private BigDecimal rttMdev;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * ops_job_execution.id
     */
    @Column(name = "exec_id")
    private Long execId;

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
     * @return node_id
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * @param nodeId
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    /**
     * 获取在线，离线
     *
     * @return alive - 在线，离线
     */
    public String getAlive() {
        return alive;
    }

    /**
     * 设置在线，离线
     *
     * @param alive 在线，离线
     */
    public void setAlive(String alive) {
        this.alive = alive == null ? null : alive.trim();
    }

    /**
     * 获取ping用时，单位s
     *
     * @return cost - ping用时，单位s
     */
    public Integer getCost() {
        return cost;
    }

    /**
     * 设置ping用时，单位s
     *
     * @param cost ping用时，单位s
     */
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    /**
     * @return packet_send
     */
    public Integer getPacketSend() {
        return packetSend;
    }

    /**
     * @param packetSend
     */
    public void setPacketSend(Integer packetSend) {
        this.packetSend = packetSend;
    }

    /**
     * @return packet_rev
     */
    public Integer getPacketRev() {
        return packetRev;
    }

    /**
     * @param packetRev
     */
    public void setPacketRev(Integer packetRev) {
        this.packetRev = packetRev;
    }

    /**
     * 获取百分比
     *
     * @return packet_loss - 百分比
     */
    public Integer getPacketLoss() {
        return packetLoss;
    }

    /**
     * 设置百分比
     *
     * @param packetLoss 百分比
     */
    public void setPacketLoss(Integer packetLoss) {
        this.packetLoss = packetLoss;
    }

    /**
     * 获取单位：ms
     *
     * @return packet_time - 单位：ms
     */
    public Integer getPacketTime() {
        return packetTime;
    }

    /**
     * 设置单位：ms
     *
     * @param packetTime 单位：ms
     */
    public void setPacketTime(Integer packetTime) {
        this.packetTime = packetTime;
    }

    /**
     * @return rtt_min
     */
    public BigDecimal getRttMin() {
        return rttMin;
    }

    /**
     * @param rttMin
     */
    public void setRttMin(BigDecimal rttMin) {
        this.rttMin = rttMin;
    }

    /**
     * @return rtt_avg
     */
    public BigDecimal getRttAvg() {
        return rttAvg;
    }

    /**
     * @param rttAvg
     */
    public void setRttAvg(BigDecimal rttAvg) {
        this.rttAvg = rttAvg;
    }

    /**
     * @return rtt_max
     */
    public BigDecimal getRttMax() {
        return rttMax;
    }

    /**
     * @param rttMax
     */
    public void setRttMax(BigDecimal rttMax) {
        this.rttMax = rttMax;
    }

    /**
     * @return rtt_mdev
     */
    public BigDecimal getRttMdev() {
        return rttMdev;
    }

    /**
     * @param rttMdev
     */
    public void setRttMdev(BigDecimal rttMdev) {
        this.rttMdev = rttMdev;
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
     * 获取ops_job_execution.id
     *
     * @return exec_id - ops_job_execution.id
     */
    public Long getExecId() {
        return execId;
    }

    /**
     * 设置ops_job_execution.id
     *
     * @param execId ops_job_execution.id
     */
    public void setExecId(Long execId) {
        this.execId = execId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ipAddr=").append(ipAddr);
        sb.append(", nodeId=").append(nodeId);
        sb.append(", alive=").append(alive);
        sb.append(", cost=").append(cost);
        sb.append(", packetSend=").append(packetSend);
        sb.append(", packetRev=").append(packetRev);
        sb.append(", packetLoss=").append(packetLoss);
        sb.append(", packetTime=").append(packetTime);
        sb.append(", rttMin=").append(rttMin);
        sb.append(", rttAvg=").append(rttAvg);
        sb.append(", rttMax=").append(rttMax);
        sb.append(", rttMdev=").append(rttMdev);
        sb.append(", createTime=").append(createTime);
        sb.append(", execId=").append(execId);
        sb.append("]");
        return sb.toString();
    }
}
