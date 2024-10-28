package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "asset_guard_line_asset")
public class AssetGuardLineAsset implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * asset表id
     */
    @Column(name = "asset_id")
    private Integer assetId;

    /**
     * asset_guard_line表id
     */
    @Column(name = "guard_line_id")
    private Integer guardLineId;

    @Column(name = "offline_endpoint_id")
    private Integer offlineEndpointId;

    /**
     * ip，从asset中获得
     */
    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * 0,未删除；1，已删除
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;

    /**
     * 强制执行指令执行状态反馈,0未下发任何指令,1执行中,2执行成功
     */
    @Column(name = "command_execute_status")
    private Integer commandExecuteStatus;

    /**
     * 创建人
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private Integer updateId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取asset表id
     *
     * @return asset_id - asset表id
     */
    public Integer getAssetId() {
        return assetId;
    }

    /**
     * 设置asset表id
     *
     * @param assetId asset表id
     */
    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    /**
     * 获取asset_guard_line表id
     *
     * @return guard_line_id - asset_guard_line表id
     */
    public Integer getGuardLineId() {
        return guardLineId;
    }

    /**
     * 设置asset_guard_line表id
     *
     * @param guardLineId asset_guard_line表id
     */
    public void setGuardLineId(Integer guardLineId) {
        this.guardLineId = guardLineId;
    }

    /**
     * 获取ip，从asset中获得
     *
     * @return ip_addr - ip，从asset中获得
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * 设置ip，从asset中获得
     *
     * @param ipAddr ip，从asset中获得
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    /**
     * 获取0,未删除；1，已删除
     *
     * @return delete_status - 0,未删除；1，已删除
     */
    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * 设置0,未删除；1，已删除
     *
     * @param deleteStatus 0,未删除；1，已删除
     */
    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    /**
     * 获取强制执行指令执行状态反馈,0未下发任何指令,1执行中,2执行成功
     *
     * @return command_execute_status - 强制执行指令执行状态反馈,0未下发任何指令,1执行中,2执行成功
     */
    public Integer getCommandExecuteStatus() {
        return commandExecuteStatus;
    }

    /**
     * 设置强制执行指令执行状态反馈,0未下发任何指令,1执行中,2执行成功
     *
     * @param commandExecuteStatus 强制执行指令执行状态反馈,0未下发任何指令,1执行中,2执行成功
     */
    public void setCommandExecuteStatus(Integer commandExecuteStatus) {
        this.commandExecuteStatus = commandExecuteStatus;
    }

    /**
     * 获取创建人
     *
     * @return creator_id - 创建人
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建人
     *
     * @param creatorId 创建人
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 获取更新人
     *
     * @return update_id - 更新人
     */
    public Integer getUpdateId() {
        return updateId;
    }

    /**
     * 设置更新人
     *
     * @param updateId 更新人
     */
    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
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
        sb.append(", assetId=").append(assetId);
        sb.append(", guardLineId=").append(guardLineId);
        sb.append(", ipAddr=").append(ipAddr);
        sb.append(", deleteStatus=").append(deleteStatus);
        sb.append(", commandExecuteStatus=").append(commandExecuteStatus);
        sb.append(", creatorId=").append(creatorId);
        sb.append(", updateId=").append(updateId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    public Integer getOfflineEndpointId() {
        return offlineEndpointId;
    }

    public void setOfflineEndpointId(Integer offlineEndpointId) {
        this.offlineEndpointId = offlineEndpointId;
    }
}