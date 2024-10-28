package com.zans.mms.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "po_base_dict")
public class PoBaseDict implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 常量的类型
     */
    @Column(name = "dict_key")
    private String dictKey;

    /**
     * 常量的取值，表中字段的取值
     */
    @Column(name = "item_key")
    private String itemKey;

    /**
     * 常量value，显示用
     */
    @Column(name = "item_value")
    private String itemValue;

    @Column(name = "class_type")
    private String classType;


    @Column(name = "belong")
    private String belong;

    @Column(name="remark")
    private String remark;

    /**
     * 排序，小的在前
     */
    private Byte ordinal;

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

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    /**
     * 获取常量的取值，表中字段的取值
     *
     * @return item_key - 常量的取值，表中字段的取值
     */
    public String getItemKey() {
        return itemKey;
    }

    /**
     * 设置常量的取值，表中字段的取值
     *
     * @param itemKey 常量的取值，表中字段的取值
     */
    public void setItemKey(String itemKey) {
        this.itemKey = itemKey == null ? null : itemKey.trim();
    }

    /**
     * 获取常量value，显示用
     *
     * @return item_value - 常量value，显示用
     */
    public String getItemValue() {
        return itemValue;
    }

    /**
     * 设置常量value，显示用
     *
     * @param itemValue 常量value，显示用
     */
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue == null ? null : itemValue.trim();
    }

    /**
     * 获取排序，小的在前
     *
     * @return ordinal - 排序，小的在前
     */
    public Byte getOrdinal() {
        return ordinal;
    }

    /**
     * 设置排序，小的在前
     *
     * @param ordinal 排序，小的在前
     */
    public void setOrdinal(Byte ordinal) {
        this.ordinal = ordinal;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    @Override
    public String toString() {
        return "SysConstantItem{" +
                "id=" + id +
                ", dictKey='" + dictKey + '\'' +
                ", itemKey='" + itemKey + '\'' +
                ", itemValue='" + itemValue + '\'' +
                ", classType='" + classType + '\'' +
                ", ordinal=" + ordinal +
                '}';
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
