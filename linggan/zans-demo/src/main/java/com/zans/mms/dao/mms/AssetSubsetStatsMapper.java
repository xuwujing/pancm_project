package com.zans.mms.dao.mms;

import com.zans.mms.model.AssetSubsetStats;
import com.zans.mms.vo.asset.stats.AssetSubsetStatsResVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AssetSubsetStatsMapper extends Mapper<AssetSubsetStats> {
    AssetSubsetStats getLastRecordBySubsetId(@Param("subsetId") Long subsetId);

    /**
     * @Author beiming
     * @Description  统计两周在线率
     * @Date  4/22/21
     * @Param
     * @return
     *
     * @param weekDay*/
    List<AssetSubsetStatsResVO> getTwoWeekData(@Param("weekDay")Integer weekDay);
}
