package com.zans.mms.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "t_ip_all")
public class IpAll implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "point_name")
    private String pointName;

    @Column(name = "ip_addr")
    private String ipAddr;

    @Column(name = "mask")
    private String mask;

    private String gateway;

    private String vlan;

    private String contractor;

    @Column(name = "contractor_person")
    private String contractorPerson;

    @Column(name = "contractor_phone")
    private String contractorPhone;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_status")
    private Integer projectStatus;

    @Column(name = "maintain_company")
    private String maintainCompany;

    @Column(name = "maintain_person")
    private String maintainPerson;

    @Column(name = "maintain_phone")
    private String maintainPhone;

    @Column(name = "the_date")
    private Date theDate;

    @Column(name = "device_model_type")
    private String deviceModelType;

    @Column(name = "device_model_brand")
    private String deviceModelBrand;

    @Column(name = "device_model_des")
    private String deviceModelDes;

    @Column(name = "device_type_id")
    private Integer deviceTypeId;

    /**
     * 地区编号
     */
    @Column(name = "area_id")
    private Integer areaId;

    @Column(name = "sw_ip")
    private String swIp;

    /**
     * 认证MAC地址
     */
    @Column(name = "auth_mac")
    private String authMac;

    /**
     * 认证状态 -1：未认证；1：认证申请；2：认证通过;3:认证否决;4：认证驳回；
     */
    @Column(name = "auth_status")
    private Integer authStatus;

    /**
     * 认证审核意见
     */
    @Column(name = "auth_mark")
    private String authMark;

    /**
     * 发起认证时间
     */
    @Column(name = "auth_time")
    private String authTime;

    /**
     * 认证通过时间
     */
    @Column(name = "auth_permit_time")
    private String authPermitTime;

    /**
     * 发起认证请求的人
     */
    @Column(name = "auth_person")
    private String authPerson;

    /**
     * 审核人
     */
    @Column(name = "auth_permit_person")
    private String authPermitPerson;

    /**
     * t_ip_alloc.id
     */
    @Column(name = "alloc_id")
    private Integer allocId;





    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "longitudeB")
    private String longitudeB;

    @Column(name = "latitudeB")
    private String latitudeB;
}
