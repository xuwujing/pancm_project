package com.zans.mms.dao;

import com.zans.mms.model.AssetSubsetDetail;
import com.zans.mms.vo.asset.AssetForMapResVO;
import com.zans.mms.vo.asset.AssetQueryVO;
import com.zans.mms.vo.asset.AssetResVO;
import com.zans.mms.vo.asset.subset.AssetExportVO;
import com.zans.mms.vo.asset.subset.AssetSubsetDetaiQueryReqVO;
import com.zans.mms.vo.asset.subset.AssetSubsetDetailExportVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AssetSubsetDetailMapper extends Mapper<AssetSubsetDetail> {
    List<AssetResVO> groupList(AssetSubsetDetaiQueryReqVO reqVO);

    int removeAsset(@Param("subsetId") Long subsetId, @Param("assetId") Long assetId);

    List<Long> getAssetIdBySubsetId(Long subsetId);

    int deleteDetailBySubsetId(Long subsetId);

    int getByAssetId(String assetId);

    void deleteDetailByAssetId(String id);


    String selectBySubsetIdAndAssetId(@Param("subsetId") Long subsetId,@Param("assetId") Long assetId);

	List<AssetSubsetDetailExportVO> selectExportData();
    /**
     * @Author beiming
     * @Description  子集资产地图
     * @Date  4/22/21
     * @Param
     * @return
     **/

    List<AssetForMapResVO> getListForMap(@Param("subsetId") Long subsetId);

	List<AssetExportVO> getExportData(String subsetId);



	/**
	 * 根据子集id清空一个子集
	 * @param subsetId
	 */
	void clearById(Long subsetId);

	String getSubsetNameById(String subsetId);
}