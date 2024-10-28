package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.Asset;
import com.zans.mms.model.Speed;
import com.zans.mms.vo.asset.AssetExportQueryVO;
import com.zans.mms.vo.asset.AssetQueryVO;
import com.zans.mms.vo.asset.AssetViewResVO;
import com.zans.mms.vo.asset.ExcelAssetVO;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisInfoExReqVO;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * interface Assetservice
 *
 * @author
 */
public interface IAssetService extends BaseService<Asset>{


    ApiResult getList(AssetQueryVO vo);

    ApiResult getMonitorList(AssetQueryVO vo);

    ApiResult flagDiagnosis(AssetDiagnosisInfoExReqVO vo);

    AssetViewResVO getViewById(Long id);

    Integer getIdByUniqueId(String assetId);

    Boolean existRelation(String id);

    int deleteByUniqueId(String id);

    ApiResult chooseAssetList(AssetQueryVO vo);

    List<ExcelAssetVO> batchAddAsset(String newFileName, String originName, UserSession userSession, String type);

    Map<String, String> downloadTemplate(String type);

    /**
    * @Author beiming
    * @Description  根据条件查询获取assetList
    * @Date  4/13/21
    * @Param map
    * @return
    **/
    List<Asset> getListByCondition(HashMap<String, Object> map);


    ApiResult getDiagnosisView(String assetCode,String traceId);

    /**
     * 根据资产设备类型导出资产数据
     * @param type
     * @param vo
     * @param fileName
     * @return
     */
	String export(String type, AssetExportQueryVO vo, String fileName);


	String generatedFileName(String date,String type);


    /**
     * 导出人工标记数据
     *
     * @return
     */
    String exportFlag();

    String exportFlagByKind(HttpServletResponse response);

    /**
     * 资产数据同步
     */
	void synchronousData();

    void dealUploadData(Integer operation,Speed speed, List<ExcelAssetVO> assetVOS, String newFileName, UserSession userSession);

	void backupTable();

	void synchronousProjectName(Asset asset);

	/**
	 * 获取导入列表
	 * @param vo
	 * @return
	 */
	ApiResult getImportList(AssetQueryVO vo);

	void updateImportSelective(Asset asset);

	void synchronousImportProjectName(Asset asset);

	void importConfig(AssetQueryVO queryVO, UserSession userSession);
}
