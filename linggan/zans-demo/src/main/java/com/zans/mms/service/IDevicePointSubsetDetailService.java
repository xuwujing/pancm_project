package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.DevicePointSubsetDetail;
import com.zans.mms.vo.asset.subset.AssetSubsetDetaiQueryReqVO;
import com.zans.mms.vo.devicepoint.subset.PointSubsetDetailAddByConditionReqVO;
import com.zans.mms.vo.devicepoint.subset.PointSubsetDetailAddReqVO;

/**
 * interface DevicePointSubsetDetailservice
 *
 * @author
 */
public interface IDevicePointSubsetDetailService extends BaseService<DevicePointSubsetDetail>{


    void groupsAddPoint(PointSubsetDetailAddReqVO reqVO, UserSession userSession);

    void removePoint(PointSubsetDetailAddReqVO reqVO);

    ApiResult groupList(AssetSubsetDetaiQueryReqVO reqVO);

    void groupsAddPointByCondition(PointSubsetDetailAddByConditionReqVO reqVO, UserSession userSession);

    ApiResult batchAddDevicePointSubset(String newFileName, String originName, UserSession userSession, String json );

    /**
     * 巡检子集数据导出方法
     * @return
     */
	String exportFile(String fileName);

	/**
	 * 巡检子集点位数据导出方法
	 * @param subsetId
	 * @param fileName
	 * @return
	 */
	String export(String subsetId, String fileName);
}
