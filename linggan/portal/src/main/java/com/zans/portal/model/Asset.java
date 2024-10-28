package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "asset")
public class Asset implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 点位名称
     */
    @Column(name = "point_name")
    private String pointName;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * ip地址
     */
    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * 资产部门
     */
    @Column(name = "department_id")
    private Integer departmentId;
    /**
     * 资产部门
     */
    @Column(name = "map_catalogue_id")
    private Integer mapCatalogueId;

    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 联系人单位
     */
    private String contractor;

    /**
     * 项目状态：0建设中，1质保，2过保，3维保
     */
    @Column(name = "project_status")
    private Integer projectStatus;

    /**
     * 联系人
     */
    @Column(name = "contractor_person")
    private String contractorPerson;

    /**
     * 联系电话
     */
    @Column(name = "contractor_phone")
    private String contractorPhone;

    /**
     * 维护单位
     */
    @Column(name = "maintain_company")
    private String maintainCompany;

    /**
     * 维护联系人
     */
    @Column(name = "maintain_person")
    private String maintainPerson;

    /**
     * 维护联系电话
     */
    @Column(name = "maintain_phone")
    private String maintainPhone;

    /**
     * 维护状态(1正常,2迁改停用,3审核停用)
     */
    @Column(name = "maintain_status")
    private String maintainStatus;

    /**
     * 设备品牌
     */
    @Column(name = "device_model_brand")
    private String deviceModelBrand;

    /**
     * 设备类类型
     */
    @Column(name = "device_type")
    private String deviceType;

    /**
     * 设备型号
     */
    @Column(name = "device_model_des")
    private String deviceModelDes;

    /**
     * 是否哑终端 0：活跃终端；1：哑终端
     */
    private Integer mute;

    /**
     * 数据录入来源(0arp扫描,1vpn导入,2其他)
     */
    private Integer source;

    /**
     * 是否在线;1:在线；2；不在线
     */
    ///将alive alive_last_time移至alive_heartbeat表
//    private Integer alive;

    /**
     * 上次在线时间
     */
//    @Column(name = "alive_last_time")
//    private Date aliveLastTime;

    /**
     * 0,正常；1，删除
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;

    /**
     * 0停用；1启用
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 录入人(创建人)
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 创建人
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private Integer updateId;

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


    @Override
    public String toString() {
      return JSONObject.toJSONString(this);
    }
}
