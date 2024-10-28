package com.zans.portal.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "asset_branch_endpoint_statistics")
public class AssetBranchEndpointStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 12位
     */
    @Column(name = "pass")
    private String pass;

    /**
     * asset_branch_statistics表id
     */
    @Column(name = "asset_branch_statistics_id")
    private Integer assetBranchStatisticsId;

    /**
     * 是否在线;1:在线；2；不在线
     */
    @Column(name = "alive")
    private Integer alive;

    /**
     * 统计时间
     */
    @Column(name = "statistics_time")
    private Date statisticsTime;


    /**
     *  `area_id` int(10) DEFAULT NULL COMMENT '区域编号，从endpoint_profile中获得',
     * 	`point_name` varchar(255) DEFAULT NULL COMMENT '点位名称，从t_ip_all中获得',
     * 	`device_type` int(10) DEFAULT '0' COMMENT '设备类型，从endpoint_profile中获得',
     * 	`brand_name` varchar(100) DEFAULT NULL COMMENT '品牌，从endpoint_profile中获得',
     * 	`model_des` varchar(50) DEFAULT NULL COMMENT '设备型号，从endpoint_profile中获得',
     */

    @Column(name = "point_name")
    private String pointName;

    @Column(name = "device_type")
    private Integer deviceType;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "model_des")
    private String modelDes;

    @Column(name = "ip_addr")
    private String ipAddr;

    @Column(name = "enable_status")
    private Integer enableStatus;
}