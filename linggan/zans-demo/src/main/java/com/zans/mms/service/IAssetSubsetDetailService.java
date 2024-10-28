package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.AssetSubsetDetail;
import com.zans.mms.vo.asset.subset.AssetSubsetDetaiQueryReqVO;
import com.zans.mms.vo.asset.subset.AssetSubsetDetailAddByConditionReqVO;
import com.zans.mms.vo.asset.subset.AssetSubsetDetailAddReqVO;
import com.zans.mms.vo.asset.subset.AssetSubsetPingReqVO;

/**
 * interface AssetSubsetDetailservice
 *
 * @author
 */
public interface IAssetSubsetDetailService extends BaseService<AssetSubsetDetail>{


    ApiResult groupList(AssetSubsetDetaiQueryReqVO reqVO);

    int groupsAddAsset(AssetSubsetDetailAddReqVO reqVO, UserSession userSession);

    int removeAsset(AssetSubsetDetailAddReqVO reqVO);

    void groupsAddAssetByCondition(AssetSubsetDetailAddByConditionReqVO reqVO, UserSession userSession);

	String exportFile(String fileName);

	/**
	* @Author beiming
	* @Description  一键ping统计
	* @Date  4/19/21
	* @Param
	* @return
	**/
    ApiResult pingAndStats(AssetSubsetPingReqVO reqVO, UserSession userSession);

	/**
	 * 资产数据导出
	 * @param subsetId 子集id
	 * @param fileName 文件名称
	 * @return
	 */
	String export(String subsetId, String fileName);
}
