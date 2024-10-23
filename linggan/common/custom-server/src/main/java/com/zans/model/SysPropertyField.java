package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 字段属性表(SysPropertyField)实体类
 *
 * @author beixing
 * @since 2022-04-19 15:10:27
 */
@Data
@Table(name = "sys_property_field")
public class SysPropertyField implements Serializable {
    private static final long serialVersionUID = -32045800630680693L;
    /**
     * 主键
     */
    @Column(name = "id")
    private Integer id;
    /**
     * 名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 值
     */
    @Column(name = "value")
    private String value;
    /**
     * 启用状态，1,使用;0,未使用;
     */
    @Column(name = "filed_enable")
    private Integer filedEnable;
    /**
     * 字段状态
     */
    @Column(name = "filed_status")
    private Integer filedStatus;
    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;
    @Column(name = "created_time")
    private String createdTime;
    @Column(name = "updated_time")
    private String updatedTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
