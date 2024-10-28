package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.mms.model.AssetSubsetStats;

/**
 * interface AssetSubsetStatsservice
 *
 * @author
 */
public interface IAssetSubsetStatsService extends BaseService<AssetSubsetStats>{


    AssetSubsetStats getLastRecordBySubsetId(Long subsetId);

    /**
    * @Author beiming
    * @Description  统计两周在线率
    * @Date  4/22/21
    * @Param
    * @return
    *
     * @param weekDay*/
    ApiResult statsTwoWeek(Integer weekDay);
}