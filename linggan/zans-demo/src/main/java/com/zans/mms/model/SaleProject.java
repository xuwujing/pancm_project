package com.zans.mms.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "sale_project")
public class SaleProject implements Serializable {
    /**
     * 自增id
     */
    @Id
    private Long id;

    /**
     * 销售项目编号
     */
    @Column(name = "project_id")
    private String projectId;

    /**
     * 销售项目名称
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 创建用户
     */
    private String creator;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}