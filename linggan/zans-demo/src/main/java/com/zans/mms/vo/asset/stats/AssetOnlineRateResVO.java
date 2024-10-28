package com.zans.mms.vo.asset.stats;

import lombok.Data;

import java.util.Map;
/**
* @Title: AssetOnlineRateResVO
* @Description: 在线率详情
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/23/21
*/
@Data
public class AssetOnlineRateResVO {
    private String areaName;

    private String subsetName;

    /**
     * key: weekDay
     * value AssetSubsetStatsResVO
     */
    private Map<String, AssetSubsetStatsResVO> onlineData;
}
