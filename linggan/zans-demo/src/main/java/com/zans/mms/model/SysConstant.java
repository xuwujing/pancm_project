package com.zans.mms.model;

import javax.persistence.*;
import java.io.Serializable;

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
     * 是否有效，默认1有效0无效
     */
    private Integer status;

    /**
     * 备注
     */
    private String comment;

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
     * 获取是否有效，默认1有效0无效
     *
     * @return status - 是否有效，默认1有效0无效
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置是否有效，默认1有效0无效
     *
     * @param status 是否有效，默认1有效0无效
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 获取备注
     *
     * @return comment - 备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置备注
     *
     * @param comment 备注
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
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
        sb.append(", constantValue=").append(constantValue);
        sb.append("]");
        return sb.toString();
    }
}