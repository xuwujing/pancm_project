package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_custom_data")
public class SysCustomData implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 关联主表的rowid
     */
    private String rowid;

    /**
     * 关联custom_field_id
     */
    @Column(name = "field_id")
    private Integer fieldId;

    /**
     * 列键
     */
    @Column(name = "field_key")
    private String fieldKey;

    /**
     * 列值
     */
    @Column(name = "field_value")
    private String fieldValue;

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
     * 获取关联主表的rowid
     *
     * @return rowid - 关联主表的rowid
     */
    public String getRowid() {
        return rowid;
    }

    /**
     * 设置关联主表的rowid
     *
     * @param rowid 关联主表的rowid
     */
    public void setRowid(String rowid) {
        this.rowid = rowid == null ? null : rowid.trim();
    }

    /**
     * 获取关联custom_field_id
     *
     * @return field_id - 关联custom_field_id
     */
    public Integer getFieldId() {
        return fieldId;
    }

    /**
     * 设置关联custom_field_id
     *
     * @param fieldId 关联custom_field_id
     */
    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    /**
     * 获取列键
     *
     * @return field_key - 列键
     */
    public String getFieldKey() {
        return fieldKey;
    }

    /**
     * 设置列键
     *
     * @param fieldKey 列键
     */
    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey == null ? null : fieldKey.trim();
    }

    /**
     * 获取列值
     *
     * @return field_value - 列值
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     * 设置列值
     *
     * @param fieldValue 列值
     */
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue == null ? null : fieldValue.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", rowid=").append(rowid);
        sb.append(", fieldId=").append(fieldId);
        sb.append(", fieldKey=").append(fieldKey);
        sb.append(", fieldValue=").append(fieldValue);
        sb.append("]");
        return sb.toString();
    }
}