package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "base_org")
public class BaseOrg implements Serializable {


    /**
     * 机构代码
     */
    @Id
    @Column(name = "org_id")
    private String orgId;

    /**
     * 单位名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 单位全称
     */
    @Column(name = "org_full_name")
    private String orgFullName;

    /**
     * 联系人
     */
    @Column(name = "org_contact")
    private String orgContact;

    /**
     * 联系电话
     */
    @Column(name = "org_phone")
    private String orgPhone;

    /**
     * 01 业主单位
     */
    @Column(name = "org_type")
    private String orgType;

    /**
     * 备注
     */
    private String remark;

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
