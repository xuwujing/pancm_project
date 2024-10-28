package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "asset_branch_asset_statistics")
public class AssetBranchAssetStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * asset_branch_statistics_id表id
     */
    @Column(name = "asset_branch_statistics_id")
    private Integer assetBranchStatisticsId;

    /**
     * ip，从t_asset中获得
     */
    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * 点位名称，从t_asset中获得
     */
    @Column(name = "point_name")
    private String pointName;

    /**
     * 0，停用;1,启用；从t_asset中获得enable_status
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

    /**
     * 是否在线;1:在线；2；不在线；从t_asset中获得alive
     */
    private Integer alive;

    /**
     * 上次在线时间；从t_asset中获得alive_last_time
     */
    @Column(name = "alive_last_time")
    private Date aliveLastTime;

    /**
     * 12位
     */
    private String pass;

    /**
     * 12位
     */
    private String username;

    /**
     * 设备类型，从t_asset_profile中获得cur_device_type
     */
    @Column(name = "device_type")
    private Integer deviceType;

    /**
     * 品牌，从t_asset_profile中获得cur_brand
     */
    @Column(name = "brand_name")
    private String brandName;

    /**
     * 设备型号，从t_asset_profile中获得cur_model_des
     */
    @Column(name = "model_des")
    private String modelDes;

    /**
     * 统计时间
     */
    @Column(name = "statistics_time")
    private Date statisticsTime;





    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}