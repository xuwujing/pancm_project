package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 自定义表查询项(SysQueryFieldConfig)实体类
 *
 * @author beixing
 * @since 2022-05-20 11:28:41
 */
@Data
@Table(name = "sys_query_field_config")
public class SysQueryFieldConfig implements Serializable {
    private static final long serialVersionUID = 488588057627610792L;
    /**
     * 主键
     */
    @Column(name = "id")
    private Integer id;
    /**
     * 模块名称
     */
    @Column(name = "module_name")
    private String moduleName;
    /**
     * 键
     */
    @Column(name = "field_key")
    private String fieldKey;
    /**
     * 名称
     */
    @Column(name = "field_name")
    private String fieldName;
    /**
     * 启用状态，1,使用;0,未使用;
     */
    @Column(name = "filed_enable")
    private Integer filedEnable;
    /**
     * 字段类型 0,单输入框;1,单下拉框
     */
    @Column(name = "field_type")
    private Integer fieldType;
    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;
    /**
     * 字段备注
     */
    @Column(name = "field_desc")
    private String fieldDesc;
    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updatedTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
