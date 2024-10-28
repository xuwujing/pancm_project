package com.zans.portal.dao;

import com.zans.portal.model.AssetGuardLineAsset;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineReqVO;
import com.zans.portal.vo.asset.guardline.resp.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AssetGuardLineAssetMapper extends Mapper<AssetGuardLineAsset> {

    AssetGuardLineAsset getByAssetIdAndLineId(@Param("assetId") Integer assetId, @Param("lineId") Integer lineId);

    List<AssetGuardLineListVO> findGuardLineAssetList(@Param("reqVO") AssetGuardLineReqVO reqVO);

    List<ExcelAssetGuardLineStatisticsVO> findGuardLineAssetExcelList(@Param("reqVO") AssetGuardLineReqVO reqVO);

    List<Integer> findExistAssetIds(@Param("guardLineId") Integer guardLineId);

    List<String> findExistAssetIps(@Param("guardLineId") Integer guardLineId);

    /**
     * 重点线路运行统计导出
     * @return
     */
    List<ExcelGuardLineRunningListRespVO> findRunningStatisExport();

    /**
     * 重点线路运行统计
     * @return
     */
    List<GuardLineRunningListRespVO> findRunningStatis();

    /**
     * 查找强制下线设备ID
     */
    List<GuardLineForceCommandObjectVO> findForceEndpointIds(@Param("idList") List<Integer> idList);
   List<GuardLineForceCommandObjectVO> findForceOnlineEndpointIds(@Param("idList") List<Integer> idList);

    /**
     *
     * @param idList
     * @return
     */
    List<GuardLineForceCommandObjectVO> findCommandExecResult(@Param("idList") List<Integer> idList);

    /**
     * 获取线路下所有设备
     * @param lineId
     * @return
     */
    List<AssetGuardLineAsset> getByLineId(@Param("lineId") Integer lineId);

    List<GuardLineForceCommandObjectVO> findForceEndpointIps(@Param("ipList")  List<String> ipList);

    List<GuardLineForceCommandObjectVO> findForceEndpointIps2(@Param("ipList") List<String> ipList);
}
