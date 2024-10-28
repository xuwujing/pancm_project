package com.zans.portal.dao;

import com.zans.base.vo.SelectVO;
import com.zans.portal.model.AssetBranch;
import com.zans.portal.vo.asset.branch.req.*;
import com.zans.portal.vo.asset.branch.resp.ChoiceDeviceRespVO;
import com.zans.portal.vo.asset.branch.resp.ExcelAssetRunStatisticsVO;
import com.zans.portal.vo.asset.branch.resp.RunningListRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AssetBranchMapper extends Mapper<AssetBranch> {
    Integer findByName(@Param("name") String name,@Param("id") Integer id);

    List<AssetBranchStatisticsVO> statisticsAssetBranch(@Param("assetBranchId") Integer assetBranchId);

    List<Integer> getIds();

    /**
     *  改用choiceDeviceAssetList
     * @param reqVO
     * @return
     */
    @Deprecated
    List<ChoiceDeviceRespVO> choiceDeviceList(@Param("reqVO")ChoiceDeviceReqVO reqVO);

    List<ChoiceDeviceRespVO> choiceDeviceAssetList(@Param("reqVO")ChoiceDeviceReqVO reqVO);

    List<RunningListRespVO> runningList(@Param("reqVO")RunningListReqVO reqVO);

    List<String> timeList();

    List<ExcelAssetRunStatisticsVO> runningListExport(@Param("reqVO")RunningListReqVO reqVO);

    /**
     * 改用 assetBranchAssetStatisticsService.runningDetailList
     * @param reqVO
     * @return
     */
    @Deprecated
    List<AssetBranchStatisticsVO> runningDetailList(@Param("reqVO")RunningDetailListReqVO reqVO);

    /**
     * 改用 assetBranchAssetStatisticsService.runningDetailListForExcel
     * @param reqVO
     * @return
     */
    @Deprecated
    List<ExcelAssetBranchStatisticsVO> runningDetailListForExcel(@Param("reqVO")RunningDetailListReqVO reqVO);


    List<AssetBranchStatisticsVO> assetBranchEndpointList(@Param("reqVO")AssetBranchReqVO reqVO);

    List<ExcelAssetBranchStatisticsVO> assetBranchEndpointListForExcel(@Param("reqVO")AssetBranchReqVO reqVO);

    List<SelectVO> findToSelect(@Param("name")String name);

    List<AssetBranch> findToTreeSelect(@Param("parentId") Integer parentId);

    List<AssetBranch> getAll(@Param("parentId") Integer parentId);

    Integer getNextSeq(Integer parentId);

    void deleteHistoryStatistics(@Param("statisticsTime") String statisticsTime);

    Integer countEndpointStatistics();



    List<Integer> getExistAssetIds(@Param("assetBranchId") Integer assetBranchId);

    List<String> getExistAssetIps(@Param("assetBranchId") Integer assetBranchId);

    List<AssetBranch> findByParentId(Integer parentId);

    AssetBranch findById(Integer id);


    List<AssetBranch> getAssetBranchByAreaId( @Param("baselineAreaId")Integer baselineAreaId);
}
