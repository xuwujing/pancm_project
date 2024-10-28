package com.zans.mms.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "device_point")
public class DevicePoint implements Serializable {
    /**
     * 自增id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "point_type")
    private Integer pointType;

    /**
     * 点位编号
     */
    @Column(name = "point_code")
    private String pointCode;

    /**
     * 点位名称
     */
    @Column(name = "point_name")
    private String pointName;

    /**
     * 路口类；十字路口 丁字路口 人行通道
     */
    @Column(name = "road_type")
    private String roadType;

    /**
     * 辖区ID
     */
    @Column(name = "area_id")
    private String areaId;

    /**
     * 设备类型id
     */
    @Column(name = "device_type")
    private String deviceType;

    /**
     * 取电方式，已装电表 未装电表
     */
    @Column(name = "power_way")
    private String powerWay;

    /**
     * 电表位置 xxx方便的电表箱
     */
    @Column(name = "power_place")
    private String powerPlace;

    /**
     * 通讯接入方式 VPN 、裸纤
     */
    @Column(name = "network_linkway")
    private String networkLinkway;

    /**
     * GCJ02 经度
     */
    private BigDecimal longitude;

    /**
     * GCJ02 维度
     */
    private BigDecimal latitude;

    /**
     * 地图来源 1:百度；2:高德;3:腾讯;
     */
    @Column(name = "map_source")
    private String mapSource;

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

    /**
     * 所属单位
     */
    private String affiliatedUnits;

    private static final long serialVersionUID = 1L;


}
