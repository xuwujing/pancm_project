package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.RadiusEndpoint;
import com.zans.mms.vo.arp.AssetRespVO;
import com.zans.mms.vo.arp.AssetSearchVO;
import com.zans.mms.vo.radius.EndPointReqVO;
import com.zans.mms.vo.radius.EndPointViewVO;
import com.zans.mms.vo.radius.QzRespVO;
import com.zans.mms.vo.radius.QzViewRespVO;

import java.util.List;

/**
 * @author yhj
 */
public interface IRadiusEndPointService extends BaseService<RadiusEndpoint> {

	PageResult<EndPointViewVO> getEndPointPage(EndPointReqVO reqVO);


	ApiResult judge(Integer id, Integer policy, String authMark, UserSession userSession);

	List<AssetRespVO> findAssetsForList(AssetSearchVO asset);

	QzRespVO findQzById(Integer id);

	QzViewRespVO findCurByCurMac(String username);

	QzViewRespVO findBaseByCurMac(String username);

	ApiResult getPolicy(String username);

	ApiResult deleteAcctList(Integer id);

    void initData();
}
