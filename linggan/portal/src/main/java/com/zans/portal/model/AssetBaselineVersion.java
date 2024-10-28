package com.zans.portal.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: 基线变更记录表(AssetBaselineVersion)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:10:24
 */
@Data
@Table(name = "asset_baseline_version")
public class AssetBaselineVersion implements Serializable {
    private static final long serialVersionUID = -45721563609843140L;
    @Column(name = "id")
    private Long id;
    /**
     * asset_baseline.id
     */
    @Column(name = "asset_baseline_id")
    private Long assetBaselineId;
    /**
     * 终端mac地址
     */
    @Column(name = "username")
    private String username;
    /**
     * base_ip_mac的ip_addr
     */
    @Column(name = "ip_addr")
    private String ipAddr;
    /**
     * endpoint_profile放行时候的cur_model_des
     */
    @Column(name = "model_des")
    private String modelDes;
    /**
     * 品牌
     */
    @Column(name = "brand_name")
    private String brandName;
    /**
     * endpoint_profile放行时候的cur_device_type
     */
    @Column(name = "device_type")
    private Integer deviceType;
    /**
     * endpoint_profile放行时候的cur_server_os
     */
    @Column(name = "server_os")
    private String serverOs;

    @Column(name = "openPort")
    private String open_port;

    /**
     * nas_ip
     */
    @Column(name = "nas_ip")
    private String nasIp;
    /**
     * nas_port_id
     */
    @Column(name = "nas_port_id")
    private String nasPortId;
    /**
     * 虚拟局域网网络
     */
    @Column(name = "vlan")
    private String vlan;

    /**
     * 点位名称
     */
    @Column(name = "point_name")
    private String pointName;
    /**
     * 经度
     */
    @Column(name = "longitude")
    private Double longitude;
    /**
     * 纬度
     */
    @Column(name = "latitude")
    private Double latitude;
    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;
    /**
     * 承建单位
     */
    @Column(name = "contractor")
    private String contractor;
    /**
     * 项目状态：0建设中，1质保，2过保，3维保
     */
    @Column(name = "project_status")
    private Integer projectStatus;
    /**
     * 承建单位联系人
     */
    @Column(name = "contractor_person")
    private String contractorPerson;
    /**
     * 承建单位联系电话
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
     * map_catalogue.id
     */
    @Column(name = "map_catalogue_id")
    private Integer mapCatalogueId;
    /**
     * 变更意见
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 本次创建人
     */
    @Column(name = "creator")
    private String creator;
    /**
     * 历史记录创建人
     */
    @Column(name = "version_creator")
    private String versionCreator;
    /**
     * 历史记录创建基线时间
     */
    @Column(name = "version_create_time")
    private String versionCreateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
