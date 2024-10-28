package com.zans.mms.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "patrol_scheme")
public class PatrolScheme implements Serializable {
    /**
     * 自增id
     */
    @Id
    @GeneratedValue(generator = "JDBC")// tk 插入返回主键
    private Long id;

    /**
     * 巡检计划名称
     */
    @Column(name = "scheme_name")
    private String schemeName;

    /**
     * 巡检计划名称描述
     */
    private String description;

    /**
     * 巡检周期(天)
     */
    @Column(name = "patrol_period")
    private Integer patrolPeriod;

    /**
     * 负责单位id
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 点位子集id
     */
    @Column(name = "subset_id")
    private Long subsetId;

    /**
     * 停用,启用
     */
    @Column(name = "enable_status")
    private String enableStatus;

    /**
     * 创建用户
     */
    private String creator;

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
     * 获取自增id
     *
     * @return id - 自增id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增id
     *
     * @param id 自增id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取巡检计划名称
     *
     * @return scheme_name - 巡检计划名称
     */
    public String getSchemeName() {
        return schemeName;
    }

    /**
     * 设置巡检计划名称
     *
     * @param schemeName 巡检计划名称
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName == null ? null : schemeName.trim();
    }

    /**
     * 获取巡检计划名称描述
     *
     * @return description - 巡检计划名称描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置巡检计划名称描述
     *
     * @param description 巡检计划名称描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取巡检周期(天)
     *
     * @return patrol_period - 巡检周期(天)
     */
    public Integer getPatrolPeriod() {
        return patrolPeriod;
    }

    /**
     * 设置巡检周期(天)
     *
     * @param patrolPeriod 巡检周期(天)
     */
    public void setPatrolPeriod(Integer patrolPeriod) {
        this.patrolPeriod = patrolPeriod;
    }

    /**
     * 获取负责单位id
     *
     * @return org_id - 负责单位id
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * 设置负责单位id
     *
     * @param orgId 负责单位id
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    /**
     * 获取点位子集id
     *
     * @return subset_id - 点位子集id
     */
    public Long getSubsetId() {
        return subsetId;
    }

    /**
     * 设置点位子集id
     *
     * @param subsetId 点位子集id
     */
    public void setSubsetId(Long subsetId) {
        this.subsetId = subsetId;
    }

    /**
     * 获取停用,启用
     *
     * @return enable_status - 停用,启用
     */
    public String getEnableStatus() {
        return enableStatus;
    }

    /**
     * 设置停用,启用
     *
     * @param enableStatus 停用,启用
     */
    public void setEnableStatus(String enableStatus) {
        this.enableStatus = enableStatus == null ? null : enableStatus.trim();
    }

    /**
     * 获取创建用户
     *
     * @return creator - 创建用户
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建用户
     *
     * @param creator 创建用户
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
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
        sb.append(", schemeName=").append(schemeName);
        sb.append(", description=").append(description);
        sb.append(", patrolPeriod=").append(patrolPeriod);
        sb.append(", orgId=").append(orgId);
        sb.append(", subsetId=").append(subsetId);
        sb.append(", enableStatus=").append(enableStatus);
        sb.append(", creator=").append(creator);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}