package com.zans.mms.service.impl;


import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.dao.mms.AssetSubsetStatsMapper;
import com.zans.mms.model.AssetSubsetStats;
import com.zans.mms.service.IAssetSubsetStatsService;
import com.zans.mms.vo.asset.stats.AssetOnlineRateResVO;
import com.zans.mms.vo.asset.stats.AssetSubsetStatsResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  AssetSubsetStatsServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("assetSubsetStatsService")
public class AssetSubsetStatsServiceImpl extends BaseServiceImpl<AssetSubsetStats> implements IAssetSubsetStatsService {


	@Autowired
	private AssetSubsetStatsMapper assetSubsetStatsMapper;

    @Resource
    public void setAssetSubsetStatsMapper(AssetSubsetStatsMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.assetSubsetStatsMapper = baseMapper;
    }

    @Override
    public AssetSubsetStats getLastRecordBySubsetId(Long subsetId) {
        return assetSubsetStatsMapper.getLastRecordBySubsetId(subsetId);
    }

    @Override
    public ApiResult statsTwoWeek(Integer weekDay) {
        List<AssetSubsetStatsResVO>  list = assetSubsetStatsMapper.getTwoWeekData(weekDay);
        List<AssetOnlineRateResVO> result = new ArrayList<>();
        //根据子集名称分组
        Map<String, List<AssetSubsetStatsResVO>> collectMap =
                list.stream().collect(Collectors.groupingBy(AssetSubsetStatsResVO::getSubsetName));
        AssetOnlineRateResVO rVO ;
        for (Map.Entry<String, List<AssetSubsetStatsResVO>> entry : collectMap.entrySet()) {
            Map<String,AssetSubsetStatsResVO>  dataMap =  new HashMap<>();
            rVO = new AssetOnlineRateResVO();

            List<AssetSubsetStatsResVO> groupBySubsetList = entry.getValue();

            //根据子集名称、统计时间分组
            Map<String, List<AssetSubsetStatsResVO>> listMap = groupBySubsetList.stream().collect(Collectors.groupingBy(AssetSubsetStatsResVO::getStatsTime));
            for (Map.Entry<String, List<AssetSubsetStatsResVO>> e : listMap.entrySet()){
                List<AssetSubsetStatsResVO> groupBySubsetStatsTimeList = e.getValue();
                //在线率从高到低排序
                Collections.sort(groupBySubsetStatsTimeList, (o1, o2) -> {
                    BigDecimal onlineRate1 = new BigDecimal(o1.getOnlineRate());
                    BigDecimal onlineRate2 = new BigDecimal(o2.getOnlineRate());
                    return onlineRate1.compareTo(onlineRate2);
                });
                //在线率从高到低排序  取第一条最大的
                AssetSubsetStatsResVO statsResVO = groupBySubsetStatsTimeList.get(0);
                String onlineRate = statsResVO.getOnlineRate();
                //在线率 *100 +百分号
                String  onlineRateStr  = new BigDecimal(onlineRate).multiply(MMSConstants.ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"%";
                statsResVO.setOnlineRate(onlineRateStr);

                rVO.setAreaName(statsResVO.getAreaName());
                rVO.setSubsetName(statsResVO.getSubsetName());
                dataMap.put(statsResVO.getWeekDay().toString(),statsResVO);
            }
            rVO.setOnlineData(dataMap);
            result.add(rVO);

        }

        //按提取的辖区名称排名
        Collections.sort(result, (o1, o2) -> o1.getAreaName().compareTo(o2.getAreaName()));
        return ApiResult.success(result);
    }
}
