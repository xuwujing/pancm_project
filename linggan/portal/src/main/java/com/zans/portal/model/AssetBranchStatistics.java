package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "asset_branch_statistics")
@Data
public class AssetBranchStatistics implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    /**
     * device_branch表id
     */
    @Column(name = "asset_branch_id")
    private Integer assetBranchId;

    /**
     * 设备总数
     */
    @Column(name = "branch_total")
    private Integer branchTotal;

    /**
     * 在线设备数
     */
    @Column(name = "online_number")
    private Integer onlineNumber;

    /**
     * 离线设备数
     */
    @Column(name = "offline_number")
    private Integer offlineNumber;

    /**
     * 停用设备数
     */
    @Column(name = "disable_number")
    private Integer disableNumber;

    /**
     * 在线率
     */
    @Column(name = "online_rate")
    private String  onlineRate;

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