package com.zans.mms.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Table(name = "sys_permission")
public class SysPermission implements Serializable {
    /**
     * 首页
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "perm_id")
    private Integer permId;

    /**
     * 首页
     */
    @Column(name = "module")
    private Integer module;

    /**
     * 首页
     */
    @Column(name = "perm_name")
    private String permName;


    /**
     * 前端路径，允许为空
     */
    @Column(name = "route")
    private String route;

    /**
     * 接口路径，允许为空
     */
    @Column(name = "api")
    private String api;

    @Column(name = "perm_desc")
    private String permDesc;

    @Column(name = "seq")
    private Integer seq;

    private Integer enable;

    @Column(name = "sort")
    private Integer sort;

    /**
     * 0,无数据权限;1,有数据权限
     */
    @Column(name = "data_perm_status")
    private Integer dataPermStatus;

    private static final long serialVersionUID = 1L;


}
