package com.zans.mms.dao;

import com.zans.mms.model.Asset;
import com.zans.mms.vo.asset.*;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisHisRespVO;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisInfoRespVO;
import com.zans.mms.vo.asset.subset.AssetExportVO;
import com.zans.mms.vo.asset.subset.AssetSubsetDetaiQueryReqVO;
import com.zans.mms.vo.asset.subset.AssetSubsetDetailAddByConditionReqVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface AssetMapper extends Mapper<Asset> {
    List<AssetResVO> getList(AssetQueryVO vo);

    List<AssetMonitorResVO> getMonitorList(AssetQueryVO vo);

    AssetViewResVO getViewById(Long id);

    List<Asset> getListByCondition(HashMap<String, Object> map);

    Integer getIdByUniqueId(String assetId);

    int deleteByUniqueId(String id);

    int getExistAssetCode(String assetCode);

    void insertAsset(ExcelAssetVO vo);

    void deleteByAssetCode(String assetCode);

    void updateByAssetCode(ExcelAssetVO vo);

    void updateByAssetCodeChange(ExcelAssetVO vo);

    List<Long> getAssetByCondition(AssetSubsetDetailAddByConditionReqVO reqVO);

    List<AssetResVO> chooseAssetList(AssetQueryVO vo);

    List<String> getIdByCode(@Param("assetCode") String assetCode,@Param("deviceType") String deviceType);

    /**
    * @Author beiming
    * @Description  资产地图
    * @Date  4/22/21
    * @Param
    * @return
    **/
    List<AssetForMapResVO> getListForMap();


    AssetDiagnosisInfoRespVO getDiagnosisView(String assetCode);

    AssetDiagnosisInfoRespVO getDiagnosisHisView(String assetCode,String traceId);

    AssetDiagnosisInfoRespVO getDiagnosisFlagView(String assetCode,String traceId);

    List<AssetDiagnosisHisRespVO> getThreeDaysDiagnosisHisList(String assetCode);

	List<AssetExportVO> getAssetExportData(AssetExportQueryVO vo);

	List<AssetExportVO> getExportData(AssetSubsetDetaiQueryReqVO assetSubsetDetaiQueryReqVO);

	Long chooseAssetListCount(AssetQueryVO vo);

	Long getIdByCodeAndPointId(@Param("assetCode") String assetCode,@Param("pointId") Long pointId);

	List<Map<Long, String>> getAsset(@Param("pointId") Long pointId);

	List<Long> getAssetIds(@Param("pointId") Long pointId,@Param("ticketId")Long ticketId);

	List<String> notExistProjectName();

	void updateProjectId();

	void setDefaultItemClassification();

	void synchronousItemClassification();

	void backupTable(@Param("backupTableName") String backupTableName);

	void synchronousData(@Param("backupTableName") String backupTableName);

	ExcelAssetVO getOneByCode(@Param("assetCode") String assetCode);

	List<ExcelAssetVO> getImportList(AssetQueryVO vo);

	void updateImportSelective(Asset asset);

	List<ExcelAssetVO> getByImportId(Long importId);

	void insertOne(ExcelAssetVO excelAssetVO);

	void updateExcelAssetVO(ExcelAssetVO excelAssetVO);
}
