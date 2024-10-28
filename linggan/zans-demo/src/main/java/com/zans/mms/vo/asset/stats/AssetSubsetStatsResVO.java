package com.zans.mms.vo.asset.stats;

import lombok.Data;

import java.io.Serializable;

/**
* @Title: AssetSubsetStatsResVO
* @Description: 在线率统计报表VO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/22/21
*/
@Data
public class AssetSubsetStatsResVO implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    private String subsetName;

    /**
     * asset_subset表id
     */
    private Integer assetSubsetId;

    /**
     * 设备总数,子集详情中数量
     */
    private Integer subsetTotal;

    /**
     * 在线设备数
     */

    private Integer onlineNumber;



    /**
     * 停用设备数
     */
    private Integer disableNumber;

    /**
     * 正常设备数
     */
    private Integer normalNumber;

    /**
     * 在线率
     */
    private String onlineRate;

    /**
     * 统计时间
     */
    private String statsTime;

    /**
     * 星期几
     */
    private Integer weekDay;

    /**
     * 区域名称
     */
    private String areaName;


    private static final long serialVersionUID = 1L;


}
