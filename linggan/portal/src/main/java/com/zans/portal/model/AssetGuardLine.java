package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "asset_guard_line")
public class AssetGuardLine implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 警卫线路名称
     */
    private String name;

    /**
     * 开启快速扫描 0未开启,1开启
     */
    @Column(name = "enable_fast_scan")
    private Integer enableFastScan;

    /**
     * 排序
     */
    private Integer seq;

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
     * 0,未删除；1，已删除
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;

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
     * 获取警卫线路名称
     *
     * @return name - 警卫线路名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置警卫线路名称
     *
     * @param name 警卫线路名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取开启快速扫描 0未开启,1开启
     *
     * @return enable_fast_scan - 开启快速扫描 0未开启,1开启
     */
    public Integer getEnableFastScan() {
        return enableFastScan;
    }

    /**
     * 设置开启快速扫描 0未开启,1开启
     *
     * @param enableFastScan 开启快速扫描 0未开启,1开启
     */
    public void setEnableFastScan(Integer enableFastScan) {
        this.enableFastScan = enableFastScan;
    }

    /**
     * 获取排序
     *
     * @return seq - 排序
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置排序
     *
     * @param seq 排序
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
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
        sb.append(", name=").append(name);
        sb.append(", enableFastScan=").append(enableFastScan);
        sb.append(", seq=").append(seq);
        sb.append(", creatorId=").append(creatorId);
        sb.append(", updateId=").append(updateId);
        sb.append(", deleteStatus=").append(deleteStatus);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}