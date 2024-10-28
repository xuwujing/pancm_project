package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author beixing
 * @Title: 基线表(AssetBaseline)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:09:41
 */
@Data
@Table(name = "asset")
public class AssetBaseline implements Serializable {
    private static final long serialVersionUID = -31886062694539070L;
    @Id
    @Column(name = "id")
    private Long id;
    /**
     * endpoint_profile放行时候的cur_ip_addr
     */
    @Column(name = "ip_addr")
    private String ipAddr;
    /**
     * 终端mac地址
     */
    @Column(name = "mac")
    private String mac;
    /**
     * endpoint_profile放行时候的cur_model_des
     */
    @Column(name = "model_des")
    private String modelDes;
    /**
     * 品牌
     */
    @Column(name = "model_brand")
    private String brandName;
    /**
     * endpoint_profile放行时候的cur_device_type
     */
    @Column(name = "device_type")
    private Integer deviceType;

    @Transient
    private String deviceTypeName;
    /**
     * endpoint_profile放行时候的cur_server_os
     */
    @Column(name = "server_os")
    private String serverOs;

    @Column(name = "open_port")
    private String openPort;
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
    private String longitude;
    /**
     * 纬度
     */
    @Column(name = "latitude")
    private String latitude;
    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;

    @Column(name = "mask")
    private String mask;

    @Column(name = "gateway")
    private String gateway;
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
    @Column(name = "maintain_contact")
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
     * 0:ip、mac未绑定，mac为空;1:已绑定
     */
    @Column(name = "bind_status")
    private Integer bindStatus;
    /**
     * 审批意见
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 创建人
     */
    @Column(name = "creator")
    private String creator;
    /**
     * 更新人
     */
    @Column(name = "reviser")
    private String reviser;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;

    /**
     * 是否哑终端 0：活跃终端；1：哑终端
     */
    @Column(name = "mute")
    private Integer mute;

    /**
     * 维护状态(1正常,2迁改停用,3审核停用)
     */
    @Column(name = "maintain_status")
    private Integer maintainStatus;

    @Transient
    private String maintainStatusName;

    @Column(name = "area_name_level1")
    private String areaNameLevel1;
    @Column(name = "area_name_level2")
    private String areaNameLevel2;
    @Column(name = "area_name_level3")
    private String areaNameLevel3;

    /**
     * 地区编号
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * t_ip_alloc.id
     */
    @Column(name = "alloc_id")
    private Integer allocId;

    @Column(name = "asset_source")
    private Integer assetSource;

    @Column(name = "asset_manage")
    private Integer assetManage;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
