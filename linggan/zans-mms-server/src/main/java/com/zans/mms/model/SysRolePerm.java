package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Data
@Table(name = "sys_role_perm")
public class SysRolePerm implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "jurisdiction_id")
    private String jurisdictionId;

    @Column(name = "`name`")
    private String name;

    @Column(name = "remark")
    private String remark;

    @Column(name = "perm_id")
    private Integer permId;

    @Column(name = "data_perm")
    private Integer dataPerm;

    @Column(name = "data_perm_desc")
    private String dataPermDesc;

    @Column(name = "create_time")
    private String createTime;


    private static final long serialVersionUID = 1L;

}