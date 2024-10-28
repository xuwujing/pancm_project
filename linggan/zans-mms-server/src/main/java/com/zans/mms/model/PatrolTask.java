package com.zans.mms.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "patrol_task")
public class PatrolTask implements Serializable {
    /**
     * 自增id
     */
    @Id
    @GeneratedValue(generator = "JDBC")// tk 插入返回主键  并在sevicec层调用
    private Long id;

    /**
     * 巡检计划ID
     */
    @Column(name = "patrol_scheme_id")
    private Long patrolSchemeId;

    /**
     * 负责单位id
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 巡检任务名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 巡检任务描述
     */
    private String description;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 点位数
     */
    @Column(name = "point_count")
    private Integer pointCount;

    /**
     * 巡检完成个数
     */
    @Column(name = "finished_count")
    private Integer finishedCount;

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
     * 获取巡检计划ID
     *
     * @return patrol_scheme_id - 巡检计划ID
     */
    public Long getPatrolSchemeId() {
        return patrolSchemeId;
    }

    /**
     * 设置巡检计划ID
     *
     * @param patrolSchemeId 巡检计划ID
     */
    public void setPatrolSchemeId(Long patrolSchemeId) {
        this.patrolSchemeId = patrolSchemeId;
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
     * 获取巡检任务名称
     *
     * @return task_name - 巡检任务名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置巡检任务名称
     *
     * @param taskName 巡检任务名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    /**
     * 获取巡检任务描述
     *
     * @return description - 巡检任务描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置巡检任务描述
     *
     * @param description 巡检任务描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取点位数
     *
     * @return point_count - 点位数
     */
    public Integer getPointCount() {
        return pointCount;
    }

    /**
     * 设置点位数
     *
     * @param pointCount 点位数
     */
    public void setPointCount(Integer pointCount) {
        this.pointCount = pointCount;
    }

    /**
     * 获取巡检完成个数
     *
     * @return finished_count - 巡检完成个数
     */
    public Integer getFinishedCount() {
        return finishedCount;
    }

    /**
     * 设置巡检完成个数
     *
     * @param finishedCount 巡检完成个数
     */
    public void setFinishedCount(Integer finishedCount) {
        this.finishedCount = finishedCount;
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
        sb.append(", patrolSchemeId=").append(patrolSchemeId);
        sb.append(", orgId=").append(orgId);
        sb.append(", taskName=").append(taskName);
        sb.append(", description=").append(description);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", pointCount=").append(pointCount);
        sb.append(", finishedCount=").append(finishedCount);
        sb.append(", creator=").append(creator);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}