package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 自定义表字段(SysCustomField)实体类
 *
 * @author beixing
 * @since 2021-12-02 12:04:03
 */
@Data
@Table(name = "sys_custom_field")
public class SysCustomField implements Serializable {
    private static final long serialVersionUID = 468468476857266581L;
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
     * 类型,1,列表 ;2 ,查询项
     */
    @Column(name = "field_type")
    private Integer fieldType;
    /**
     * 列键
     */
    @Column(name = "field_key")
    private String fieldKey;
    /**
     * 列名称
     */
    @Column(name = "field_name")
    private String fieldName;
    /**
     * 启用状态，1,使用;0,未使用;
     */
    @Column(name = "filed_enable")
    private Integer filedEnable;
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
