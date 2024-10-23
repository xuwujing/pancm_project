package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 自定义列表(SysCustomFieldConfig)实体类
 *
 * @author beixing
 * @since 2022-05-07 15:47:31
 */
@Data
@Table(name = "sys_custom_field_config")
public class SysCustomFieldConfig implements Serializable {
    private static final long serialVersionUID = 902129160435048948L;
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
     * sql查询
     */
    @Column(name = "sql_text")
    private String sqlText;
    /**
     * 列表头
     */
    @Column(name = "sql_head")
    private String sqlHead;
    /**
     * 列表头中文
     */
    @Column(name = "sql_head_en")
    private String sqlHeadEn;

    @Column(name = "sql_sort")
    private String sqlSort;
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
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
