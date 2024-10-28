package com.zans.job.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "ops_node")
public class OpsNode implements Serializable {
    /**
     * id, uuid
     */
    @Id
    @Column(name = "node_id")
    private String nodeId;

    /**
     * agent、executor
     */
    @Column(name = "node_type")
    private String nodeType;

    /**
     * 节点IP
     */
    private String ip;

    /**
     * 节点端口
     */
    private Integer port;

    /**
     * ssh 的端口
     */
    @Column(name = "ssh_port")
    private Integer sshPort;

    /**
     * root账号的密码
     */
    @Column(name = "root_password")
    private String rootPassword;


    /**
     * 最大任务数
     */
    @Column(name = "max_task_count")
    private Integer maxTaskCount;

    /**
     * 权重，0-100的整数
     */
    private Integer weight;

    /**
     * 优先级，0最高
     */
    private Integer priority;

    /**
     * 给低优先级的额度，默认30
     */
    @Column(name = "temp_quota")
    private Integer tempQuota;

    /**
     * 节点注释
     */
    private String remark;

    /**
     * 0,注册失败；1，注册成功
     */
    private Integer register;

    /**
     * 0，活跃；1，不活跃
     */
    private Integer alive;

    /**
     * 租约到期时间
     */
    @Column(name = "expire_time")
    private Date expireTime;

    /**
     * 0, 正常； 1，删除
     */
    private Integer enable;

    private static final long serialVersionUID = 1L;

    /**
     * 获取id, uuid
     *
     * @return node_id - id, uuid
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * 设置id, uuid
     *
     * @param nodeId id, uuid
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    /**
     * 获取agent、executor
     *
     * @return node_type - agent、executor
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * 设置agent、executor
     *
     * @param nodeType agent、executor
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType == null ? null : nodeType.trim();
    }

    /**
     * 获取节点IP
     *
     * @return ip - 节点IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置节点IP
     *
     * @param ip 节点IP
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * 获取节点端口
     *
     * @return port - 节点端口
     */
    public Integer getPort() {
        return port;
    }

    /**
     * 设置节点端口
     *
     * @param port 节点端口
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * 获取最大任务数
     *
     * @return max_task_count - 最大任务数
     */
    public Integer getMaxTaskCount() {
        return maxTaskCount;
    }

    /**
     * 设置最大任务数
     *
     * @param maxTaskCount 最大任务数
     */
    public void setMaxTaskCount(Integer maxTaskCount) {
        this.maxTaskCount = maxTaskCount;
    }

    /**
     * 获取权重，0-100的整数
     *
     * @return weight - 权重，0-100的整数
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * 设置权重，0-100的整数
     *
     * @param weight 权重，0-100的整数
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * 获取优先级，0最高
     *
     * @return priority - 优先级，0最高
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 设置优先级，0最高
     *
     * @param priority 优先级，0最高
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * 获取给低优先级的额度，默认30
     *
     * @return temp_quota - 给低优先级的额度，默认30
     */
    public Integer getTempQuota() {
        return tempQuota;
    }

    /**
     * 设置给低优先级的额度，默认30
     *
     * @param tempQuota 给低优先级的额度，默认30
     */
    public void setTempQuota(Integer tempQuota) {
        this.tempQuota = tempQuota;
    }

    /**
     * 获取节点注释
     *
     * @return remark - 节点注释
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置节点注释
     *
     * @param remark 节点注释
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取0,注册失败；1，注册成功
     *
     * @return register - 0,注册失败；1，注册成功
     */
    public Integer getRegister() {
        return register;
    }

    /**
     * 设置0,注册失败；1，注册成功
     *
     * @param register 0,注册失败；1，注册成功
     */
    public void setRegister(Integer register) {
        this.register = register;
    }

    /**
     * 获取0，活跃；1，不活跃
     *
     * @return alive - 0，活跃；1，不活跃
     */
    public Integer getAlive() {
        return alive;
    }

    /**
     * 设置0，活跃；1，不活跃
     *
     * @param alive 0，活跃；1，不活跃
     */
    public void setAlive(Integer alive) {
        this.alive = alive;
    }

    /**
     * 获取租约到期时间
     *
     * @return expire_time - 租约到期时间
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * 设置租约到期时间
     *
     * @param expireTime 租约到期时间
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 获取0, 正常； 1，删除
     *
     * @return enable - 0, 正常； 1，删除
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * 设置0, 正常； 1，删除
     *
     * @param enable 0, 正常； 1，删除
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getRootPassword() {
        return rootPassword;
    }

    public void setRootPassword(String rootPassword) {
        this.rootPassword = rootPassword;
    }

    public Integer getSshPort() {
        return sshPort;
    }

    public void setSshPort(Integer sshPort) {
        this.sshPort = sshPort;
    }

    @Override
    public String toString() {
        return "OpsNode{" +
                "nodeId='" + nodeId + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", sshPort=" + sshPort +
                ", rootPassword='" + rootPassword + '\'' +
                ", maxTaskCount=" + maxTaskCount +
                ", weight=" + weight +
                ", priority=" + priority +
                ", tempQuota=" + tempQuota +
                ", remark='" + remark + '\'' +
                ", register=" + register +
                ", alive=" + alive +
                ", expireTime=" + expireTime +
                ", enable=" + enable +
                '}';
    }
}