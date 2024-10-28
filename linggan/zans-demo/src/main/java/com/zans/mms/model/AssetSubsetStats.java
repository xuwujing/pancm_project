package com.zans.mms.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "asset_subset_stats")
public class AssetSubsetStats implements Serializable {
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
     * 设备总数,子集详情中数量
     */
    @Column(name = "subset_total")
    private Integer subsetTotal;

    /**
     * 在线设备数
     */
    @Column(name = "online_number")
    private Integer onlineNumber;

    /**
     * fault设备数
     */
    @Column(name = "fault_number")
    private Integer faultNumber;

    /**
     * 停用设备数
     */
    @Column(name = "disable_number")
    private Integer disableNumber;

    /**
     * 正常设备数
     */
    @Column(name = "normal_number")
    private Integer normalNumber;

    /**
     * 在线率
     */
    @Column(name = "online_rate")
    private BigDecimal onlineRate;

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
     * 获取设备总数,子集详情中数量
     *
     * @return subset_total - 设备总数,子集详情中数量
     */
    public Integer getSubsetTotal() {
        return subsetTotal;
    }

    /**
     * 设置设备总数,子集详情中数量
     *
     * @param subsetTotal 设备总数,子集详情中数量
     */
    public void setSubsetTotal(Integer subsetTotal) {
        this.subsetTotal = subsetTotal;
    }

    /**
     * 获取在线设备数
     *
     * @return online_number - 在线设备数
     */
    public Integer getOnlineNumber() {
        return onlineNumber;
    }

    /**
     * 设置在线设备数
     *
     * @param onlineNumber 在线设备数
     */
    public void setOnlineNumber(Integer onlineNumber) {
        this.onlineNumber = onlineNumber;
    }

    /**
     * 获取fault设备数
     *
     * @return fault_number - fault设备数
     */
    public Integer getFaultNumber() {
        return faultNumber;
    }

    /**
     * 设置fault设备数
     *
     * @param faultNumber fault设备数
     */
    public void setFaultNumber(Integer faultNumber) {
        this.faultNumber = faultNumber;
    }

    /**
     * 获取停用设备数
     *
     * @return disable_number - 停用设备数
     */
    public Integer getDisableNumber() {
        return disableNumber;
    }

    /**
     * 设置停用设备数
     *
     * @param disableNumber 停用设备数
     */
    public void setDisableNumber(Integer disableNumber) {
        this.disableNumber = disableNumber;
    }

    /**
     * 获取正常设备数
     *
     * @return normal_number - 正常设备数
     */
    public Integer getNormalNumber() {
        return normalNumber;
    }

    /**
     * 设置正常设备数
     *
     * @param normalNumber 正常设备数
     */
    public void setNormalNumber(Integer normalNumber) {
        this.normalNumber = normalNumber;
    }

    /**
     * 获取在线率
     *
     * @return online_rate - 在线率
     */
    public BigDecimal getOnlineRate() {
        return onlineRate;
    }

    /**
     * 设置在线率
     *
     * @param onlineRate 在线率
     */
    public void setOnlineRate(BigDecimal onlineRate) {
        this.onlineRate = onlineRate;
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
        sb.append(", subsetTotal=").append(subsetTotal);
        sb.append(", onlineNumber=").append(onlineNumber);
        sb.append(", faultNumber=").append(faultNumber);
        sb.append(", disableNumber=").append(disableNumber);
        sb.append(", normalNumber=").append(normalNumber);
        sb.append(", onlineRate=").append(onlineRate);
        sb.append(", statsTime=").append(statsTime);
        sb.append(", execId=").append(execId);
        sb.append("]");
        return sb.toString();
    }
}
