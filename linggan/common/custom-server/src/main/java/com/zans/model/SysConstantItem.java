package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (SysConstantItem)实体类
 *
 * @author beixing
 * @since 2022-05-23 11:35:40
 */
@Data
@Table(name = "sys_constant_item")
public class SysConstantItem implements Serializable {
    private static final long serialVersionUID = -93478344917588033L;
    @Column(name = "id")
    private Object id;
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
    /**
     * 排序，小的在前
     */
    @Column(name = "ordinal")
    private Object ordinal;
    /**
     * 数值类型，int，string
     */
    @Column(name = "class_type")
    private String classType;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
