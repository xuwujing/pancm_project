package com.zans.portal.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "sys_constant_dict")
public class SysConstantDict implements Serializable {
    /**
     * 常量类型主键
     */
    @Id
    @Column(name = "dict_key")
    private String dictKey;

    /**
     * 常量类型名称
     */
    @Column(name = "dict_name")
    private String dictName;

    /**
     * sys_module
     */
    @Column(name = "module_id")
    private Integer moduleId;

    /**
     * 关联的表名
     */
    @Column(name = "table_name")
    private String tableName;

    /**
     * 管理的列名
     */
    @Column(name = "column_name")
    private String columnName;

    private static final long serialVersionUID = 1L;

    /**
     * 获取常量类型主键
     *
     * @return dict_key - 常量类型主键
     */
    public String getDictKey() {
        return dictKey;
    }

    /**
     * 设置常量类型主键
     *
     * @param dictKey 常量类型主键
     */
    public void setDictKey(String dictKey) {
        this.dictKey = dictKey == null ? null : dictKey.trim();
    }

    /**
     * 获取常量类型名称
     *
     * @return dict_name - 常量类型名称
     */
    public String getDictName() {
        return dictName;
    }

    /**
     * 设置常量类型名称
     *
     * @param dictName 常量类型名称
     */
    public void setDictName(String dictName) {
        this.dictName = dictName == null ? null : dictName.trim();
    }

    /**
     * 获取sys_module
     *
     * @return module_id - sys_module
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * 设置sys_module
     *
     * @param moduleId sys_module
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * 获取关联的表名
     *
     * @return table_name - 关联的表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置关联的表名
     *
     * @param tableName 关联的表名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    /**
     * 获取管理的列名
     *
     * @return column_name - 管理的列名
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 设置管理的列名
     *
     * @param columnName 管理的列名
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", dictKey=").append(dictKey);
        sb.append(", dictName=").append(dictName);
        sb.append(", moduleId=").append(moduleId);
        sb.append(", tableName=").append(tableName);
        sb.append(", columnName=").append(columnName);
        sb.append("]");
        return sb.toString();
    }
}