package com.zans.mms.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "asset")
public class Asset implements Serializable {
    /**
     * 自增id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    /**
     * 资产编号
     */
    @Column(name = "asset_code")
    private String assetCode;

    /**
     * 设备方位
     */
    @Column(name = "device_direction")
    private String deviceDirection;

    /**
     * 点位ID
     */
    @Column(name = "point_id")
    private Long pointId;

    /**
     * 设备类型id
     */
    @Column(name = "device_type")
    private String deviceType;

    /**
     * 设备类型子类 非字典，直接录入文字
     */
    @Column(name = "device_sub_type")
    private String deviceSubType;

    /**
     * ip地址
     */
    @Column(name = "network_ip")
    private String networkIp;

    /**
     * mac地址 12位，去空格
     */
    @Column(name = "network_mac")
    private String networkMac;

    /**
     * 子网掩码
     */
    @Column(name = "network_mask")
    private String networkMask;

    /**
     * 网关
     */
    @Column(name = "network_geteway")
    private String networkGeteway;

    /**
     * 设备序列号
     */
    @Column(name = "device_sn")
    private String deviceSn;

    /**
     * 设备型号
     */
    @Column(name = "device_model_des")
    private String deviceModelDes;

    /**
     * 设备品牌
     */
    @Column(name = "device_model_brand")
    private String deviceModelBrand;

    /**
     * 软件版本
     */
    @Column(name = "device_software_version")
    private String deviceSoftwareVersion;

    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 建设年份
     */
    @Column(name = "build_year")
    private String buildYear;

    /**
     * 建设单位
     */
    @Column(name = "build_company")
    private String buildCompany;

    /**
     * 建设单位联系人
     */
    @Column(name = "build_contact")
    private String buildContact;

    /**
     * 建设单位联系电话
     */
    @Column(name = "build_phone")
    private String buildPhone;

    /**
     * 运维单位名称
     */
    @Column(name = "maintain_company")
    private String maintainCompany;

    /**
     * 运维单位名称
     */
    @Column(name = "maintain_contact")
    private String maintainContact;

    /**
     * 运维单位名称
     */
    @Column(name = "maintain_phone")
    private String maintainPhone;

    /**
     * 检测方式 视频、雷达、线圈
     */
    @Column(name = "detect_mode")
    private String detectMode;

    /**
     * 设备维护状态
     */
    @Column(name = "maintain_status")
    private String maintainStatus;

    /**
     * 设备账号 联网设备有该属性
     */
    @Column(name = "device_account")
    private String deviceAccount;

    /**
     * 设备账号 设备密码
     */
    @Column(name = "device_pwd")
    private String devicePwd;

    /**
     * 在线 离线
     */
    @Column(name = "online_status")
    private String onlineStatus;

    /**
     * 断光 断电
     */
    @Column(name = "offline_status")
    private String offlineStatus;

    /**
     * 相机安装时间
     */
    @Column(name = "install_time")
    private String installTime;

    /**
     * 车道数
     */
    @Column(name = "lane_number")
    private Integer laneNumber;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建用户
     */
    private String creator;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

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

    /**
     * 组织id
     */
    @Column(name = "org_id")
    private String orgId;

    private static final long serialVersionUID = 1L;

}