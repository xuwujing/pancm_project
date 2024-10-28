package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_custom_field_option")
public class SysCustomFieldOption implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 选项键
     */
    @Column(name = "option_key")
    private String optionKey;

    /**
     * 显示名称
     */
    @Column(name = "option_name")
    private String optionName;

    /**
     * 是否默认选中  1:选中;0:不选中
     */
    private Integer checked;

    /**
     * 关联自定义字段表
     */
    @Column(name = "custom_field_id")
    private Integer customFieldId;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

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
     * 获取选项键
     *
     * @return option_key - 选项键
     */
    public String getOptionKey() {
        return optionKey;
    }

    /**
     * 设置选项键
     *
     * @param optionKey 选项键
     */
    public void setOptionKey(String optionKey) {
        this.optionKey = optionKey == null ? null : optionKey.trim();
    }

    /**
     * 获取显示名称
     *
     * @return option_name - 显示名称
     */
    public String getOptionName() {
        return optionName;
    }

    /**
     * 设置显示名称
     *
     * @param optionName 显示名称
     */
    public void setOptionName(String optionName) {
        this.optionName = optionName == null ? null : optionName.trim();
    }

    /**
     * 获取是否默认选中  1:选中;0:不选中
     *
     * @return checked - 是否默认选中  1:选中;0:不选中
     */
    public Integer getChecked() {
        return checked;
    }

    /**
     * 设置是否默认选中  1:选中;0:不选中
     *
     * @param checked 是否默认选中  1:选中;0:不选中
     */
    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Integer getCustomFieldId() {
        return customFieldId;
    }

    public void setCustomFieldId(Integer customFieldId) {
        this.customFieldId = customFieldId;
    }

    /**
     * 获取创建时间
     *
     * @return created_time - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取更新时间
     *
     * @return updated_time - 更新时间
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 设置更新时间
     *
     * @param updatedTime 更新时间
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", optionKey=").append(optionKey);
        sb.append(", optionName=").append(optionName);
        sb.append(", checked=").append(checked);
        sb.append(", customFieldId=").append(customFieldId);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append("]");
        return sb.toString();
    }
}