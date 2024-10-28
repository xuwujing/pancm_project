package com.zans.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_constant")
public class SysConstant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 键
     */
    @Column(name = "constant_key")
    private String constantKey;

    /**
     * 是否启用，默认1启用0不启用
     */
    private Integer status;

    /**
     * 备注（显示名称）
     */
    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 常量的值
     */
    @Column(name = "constant_value")
    private String constantValue;

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
     * 获取键
     *
     * @return constant_key - 键
     */
    public String getConstantKey() {
        return constantKey;
    }

    /**
     * 设置键
     *
     * @param constantKey 键
     */
    public void setConstantKey(String constantKey) {
        this.constantKey = constantKey == null ? null : constantKey.trim();
    }

    /**
     * 获取是否启用，默认1启用0不启用
     *
     * @return status - 是否启用，默认1启用0不启用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置是否启用，默认1启用0不启用
     *
     * @param status 是否启用，默认1启用0不启用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取备注（显示名称）
     *
     * @return comment - 备注（显示名称）
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置备注（显示名称）
     *
     * @param comment 备注（显示名称）
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
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

    /**
     * 获取常量的值
     *
     * @return constant_value - 常量的值
     */
    public String getConstantValue() {
        return constantValue;
    }

    /**
     * 设置常量的值
     *
     * @param constantValue 常量的值
     */
    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue == null ? null : constantValue.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", constantKey=").append(constantKey);
        sb.append(", status=").append(status);
        sb.append(", comment=").append(comment);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", constantValue=").append(constantValue);
        sb.append("]");
        return sb.toString();
    }
}