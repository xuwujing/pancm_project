package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.AssetSubset;
import com.zans.mms.vo.asset.subset.AssetSubsetLineChartReqVO;
import com.zans.mms.vo.asset.subset.AssetSubsetQueryVO;
import com.zans.mms.vo.devicepoint.DevicePointQueryVO;

import java.util.List;

/**
 * interface AssetSubsetservice
 *
 * @author
 */
public interface IAssetSubsetService extends BaseService<AssetSubset>{


    ApiResult getList(AssetSubsetQueryVO vo);

    Integer getIdByUniqueId(String subsetId);

    Boolean existRelation(Long id);

    int deleteByUniqueId(Long id);

    int findByName(String subsetName, String subsetId);

    ApiResult batchAddAssetSubset(String newFileName, String originName, UserSession userSession, String json);

	List<String> getSubsetNameList(AssetSubsetLineChartReqVO assetSubsetLineChartReqVO);

	ApiResult getLineChartData(AssetSubsetLineChartReqVO assetSubsetLineChartReqVO);

	ApiResult getAssetSubsetChartData(AssetSubsetLineChartReqVO assetSubsetLineChartReqVO);

	/**
	 * 资产子集实时在线率展示接口 显示最后一条在线率数据
	 * @return
	 */
	ApiResult getAssetSubsetOnlineData();


	/**
	* @Author beiming
	* @Description  资产地图
	* @Date  4/22/21
	* @Param
	* @return
	**/
	@Deprecated // 资产地图不存在，改为点位地图
    ApiResult getMapList(DevicePointQueryVO vo);

    /**
    * @Author beiming
    * @Description  获取资产子集所有数据
    * @Date  4/22/21
    * @Param
    * @return
    **/
	ApiResult getSelectList();


}