package com.zans.mms.dao;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.AssetSubset;
import com.zans.mms.vo.asset.subset.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface AssetSubsetMapper extends Mapper<AssetSubset> {
    List<AssetSubsetResVO> getList(AssetSubsetQueryVO vo);

    Integer getIdByUniqueId(String subsetId);

    int deleteByUniqueId(Long id);

    int findByName(@Param("subsetName") String subsetName, @Param("subsetId") String subsetId);


    String getIdBySubsetName(String subsetName);

    String getIdBySubsetNameAndDeviceType(Map<String, Object> map);

	List<String> getSubsetNameList(AssetSubsetLineChartReqVO assetSubsetLineChartReqVO);

	List<AssetSubsetLineChartVO> getLineChartData(AssetSubsetLineChartReqVO assetSubsetLineChartReqVO);

	List<AssetSubsetOnlineRateVO> getAssetSubsetOnlineData();


	List<AssetSubsetOnlineRateVO> getOnlineRateList(Long subsetId);

	String getSubsetNameById(Long subsetId);

	List<AssetSubsetOnlineRateVO> getOnlineRateListBySubsetIdAndDate(AssetSubsetLineChartReqVO assetSubsetLineChartReqVO);

	List<AssetSubsetOnlineRateVO> getHistoryOnlineRateListBySubsetId(AssetSubsetLineChartReqVO assetSubsetLineChartReqVO);

     List<SelectVO> getSelectList();
}
