package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "sys_data_perm_define")
public class SysDataPermDefine implements Serializable {
    @Id
    private Long id;

    /**
     * sys_permission.perm_id
     */
    @Column(name = "perm_id")
    private Integer permId;

    /**
     * 数据权限名称
     */
    @Column(name = "data_name")
    private String dataName;

    /**
     * 数据权限数值
     */
    @Column(name = "data_value")
    private Integer dataValue;

    /**
     * 描述
     */
    private String remark;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 权限分组
     */
    @Column(name = "data_group")
    private Integer dataGroup;

    @Column(name = "sort")
    private Integer sort;


    private static final long serialVersionUID = 1L;


}
