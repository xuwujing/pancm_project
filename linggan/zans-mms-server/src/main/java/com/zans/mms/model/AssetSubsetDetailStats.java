package com.zans.mms.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "asset_subset_detail_stats")
public class AssetSubsetDetailStats implements Serializable {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * asset_subset表id
     */
    @Column(name = "asset_subset_id")
    private Integer assetSubsetId;

    /**
     * asset_branch_statistics_id表id
     */
    @Column(name = "asset_subset_stats_id")
    private Integer assetSubsetStatsId;

    /**
     * ip，从t_asset中获得
     */
    @Column(name = "network_ip")
    private String networkIp;

    /**
     * 设备维护状态
     */
    @Column(name = "maintain_status")
    private String maintainStatus;

    /**
     * 在线 离线
     */
    @Column(name = "online_status")
    private String onlineStatus;

    /**
     * 上次在线时间
     */
    @Column(name = "last_scan_time")
    private Date lastScanTime;

    /**
     * 点位ID
     */
    @Column(name = "point_id")
    private Long pointId;

    /**
     * 统计时间
     */
    @Column(name = "stats_time")
    private Date statsTime;

    /**
     * ops_job_execution.id
     */
    @Column(name = "exec_id")
    private Long execId;

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
     * 获取asset_subset表id
     *
     * @return asset_subset_id - asset_subset表id
     */
    public Integer getAssetSubsetId() {
        return assetSubsetId;
    }

    /**
     * 设置asset_subset表id
     *
     * @param assetSubsetId asset_subset表id
     */
    public void setAssetSubsetId(Integer assetSubsetId) {
        this.assetSubsetId = assetSubsetId;
    }

    /**
     * 获取asset_branch_statistics_id表id
     *
     * @return asset_subset_stats_id - asset_branch_statistics_id表id
     */
    public Integer getAssetSubsetStatsId() {
        return assetSubsetStatsId;
    }

    /**
     * 设置asset_branch_statistics_id表id
     *
     * @param assetSubsetStatsId asset_branch_statistics_id表id
     */
    public void setAssetSubsetStatsId(Integer assetSubsetStatsId) {
        this.assetSubsetStatsId = assetSubsetStatsId;
    }

    /**
     * 获取ip，从t_asset中获得
     *
     * @return network_ip - ip，从t_asset中获得
     */
    public String getNetworkIp() {
        return networkIp;
    }

    /**
     * 设置ip，从t_asset中获得
     *
     * @param networkIp ip，从t_asset中获得
     */
    public void setNetworkIp(String networkIp) {
        this.networkIp = networkIp == null ? null : networkIp.trim();
    }

    /**
     * 获取设备维护状态
     *
     * @return maintain_status - 设备维护状态
     */
    public String getMaintainStatus() {
        return maintainStatus;
    }

    /**
     * 设置设备维护状态
     *
     * @param maintainStatus 设备维护状态
     */
    public void setMaintainStatus(String maintainStatus) {
        this.maintainStatus = maintainStatus == null ? null : maintainStatus.trim();
    }

    /**
     * 获取在线 离线
     *
     * @return online_status - 在线 离线
     */
    public String getOnlineStatus() {
        return onlineStatus;
    }

    /**
     * 设置在线 离线
     *
     * @param onlineStatus 在线 离线
     */
    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus == null ? null : onlineStatus.trim();
    }

    /**
     * 获取上次在线时间
     *
     * @return last_scan_time - 上次在线时间
     */
    public Date getLastScanTime() {
        return lastScanTime;
    }

    /**
     * 设置上次在线时间
     *
     * @param lastScanTime 上次在线时间
     */
    public void setLastScanTime(Date lastScanTime) {
        this.lastScanTime = lastScanTime;
    }

    /**
     * 获取点位ID
     *
     * @return point_id - 点位ID
     */
    public Long getPointId() {
        return pointId;
    }

    /**
     * 设置点位ID
     *
     * @param pointId 点位ID
     */
    public void setPointId(Long pointId) {
        this.pointId = pointId;
    }

    /**
     * 获取统计时间
     *
     * @return stats_time - 统计时间
     */
    public Date getStatsTime() {
        return statsTime;
    }

    /**
     * 设置统计时间
     *
     * @param statsTime 统计时间
     */
    public void setStatsTime(Date statsTime) {
        this.statsTime = statsTime;
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
        sb.append(", assetSubsetId=").append(assetSubsetId);
        sb.append(", assetSubsetStatsId=").append(assetSubsetStatsId);
        sb.append(", networkIp=").append(networkIp);
        sb.append(", maintainStatus=").append(maintainStatus);
        sb.append(", onlineStatus=").append(onlineStatus);
        sb.append(", lastScanTime=").append(lastScanTime);
        sb.append(", pointId=").append(pointId);
        sb.append(", statsTime=").append(statsTime);
        sb.append(", execId=").append(execId);
        sb.append("]");
        return sb.toString();
    }
}
