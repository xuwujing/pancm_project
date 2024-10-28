package com.zans.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "fort_agent_server")
public class FortAgentServer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "server_ip")
    private String serverIp;

    /**
     * agent 路径
     */
    private String path;

    /**
     * agent 应用名称
     */
    private String name;

    /**
     * 是否启用 0否 1是
     */
    private Integer status;

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
     * @return server_ip
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * @param serverIp
     */
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp == null ? null : serverIp.trim();
    }

    /**
     * 获取agent 路径
     *
     * @return path - agent 路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置agent 路径
     *
     * @param path agent 路径
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * 获取agent 应用名称
     *
     * @return name - agent 应用名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置agent 应用名称
     *
     * @param name agent 应用名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取是否启用 0否 1是
     *
     * @return status - 是否启用 0否 1是
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置是否启用 0否 1是
     *
     * @param status 是否启用 0否 1是
     */
    public void setStatus(Integer status) {
        this.status = status;
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
        sb.append(", serverIp=").append(serverIp);
        sb.append(", path=").append(path);
        sb.append(", name=").append(name);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}