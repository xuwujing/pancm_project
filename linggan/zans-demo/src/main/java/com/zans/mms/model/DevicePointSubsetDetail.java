package com.zans.mms.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "device_point_subset_detail")
public class DevicePointSubsetDetail implements Serializable {
    /**
     * 自增id
     */
    @Id
    private Long id;

    /**
     * 点位子集ID
     */
    @Column(name = "subset_id")
    private Long subsetId;

    /**
     * 点位ID
     */
    @Column(name = "point_id")
    private Long pointId;

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
     * 获取点位子集ID
     *
     * @return subset_id - 点位子集ID
     */
    public Long getSubsetId() {
        return subsetId;
    }

    /**
     * 设置点位子集ID
     *
     * @param subsetId 点位子集ID
     */
    public void setSubsetId(Long subsetId) {
        this.subsetId = subsetId;
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
        sb.append(", subsetId=").append(subsetId);
        sb.append(", pointId=").append(pointId);
        sb.append(", creator=").append(creator);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}
