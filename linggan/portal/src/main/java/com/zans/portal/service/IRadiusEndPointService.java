package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.RadiusEndpoint;
import com.zans.portal.vo.arp.AssetRespVO;
import com.zans.portal.vo.arp.AssetSearchVO;
import com.zans.portal.vo.asset.req.AssetMapUpdateReqVO;
import com.zans.portal.vo.asset.req.AssetReqVO;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.chart.CountUnit;
import com.zans.portal.vo.radius.*;

import java.util.List;
import java.util.Map;


public interface IRadiusEndPointService extends BaseService<RadiusEndpoint> {

    PageResult<EndPointViewVO> getEndPointPage(EndPointReqVO reqVO);

    PageResult<EndPointViewVO> getQzEndPointPage(QzReqVO reqVO);



    RadiusEndpoint findByMacMin(String mac);

    QzRespVO findQzById(Integer id);

    ApiResult judge(Integer id, Integer policy, String authMark, UserSession userSession);

    ApiResult doDm(String mac, Integer policy);


    List<CircleUnit> getAssetTotal();

    List<CircleUnit> getAccessTotal();




    List<CountUnit> summary();

    List<CountUnit> device();



    List<CircleUnit> getCountOfRiskType(String dayStart);


    List<AssetMapRespVO> assetMapList(AssetReqVO reqVO);



    ApiResult updateAssetMap(AssetMapUpdateReqVO searchVO);




    QzViewRespVO findBaseByCurMac(String username);

    QzViewRespVO findCurByCurMac(String username);

    List<AssetRespVO> findAssetsForList( AssetSearchVO asset);

    List<AssetRespVO> getAssetNewListPage( AssetSearchVO asset);


    void radiusJudge(Integer policy, String radApiHost, String mac, Integer deleteStatus, String baseIp);

    ApiResult verify(QzRespVO respVO);

    /**
     * 异步批量审批
     * @param parseInt
     * @param policy
     * @param authMark
     * @param userSession
     * @return
     */
    ApiResult asynchronousJudge(Integer parseInt, Integer policy, String authMark, UserSession userSession);

    void checkAlive(List<String> list, PageResult<EndPointViewVO> pageResult) throws Exception;

    QzRespVO verifyLegal(Map<String, Object> map);

    /**
     * 单个同步审批
     * @param id
     * @param policy
     * @param authMark
     * @param userSession
     * @return
     */
    ApiResult syncJudge(Integer id, Integer policy, String authMark, UserSession userSession);

    /**
    * @Author beixing
    * @Description  批量审核
    * @Date  2022/1/20
    * @Param
    * @return
    **/
    ApiResult batchJudge(EndPointBatchReqVO reqVO);


    /**
    * @Author beixing
    * @Description  设备删除
    * @Date  2022/1/20
    * @Param
    * @return
    **/
    ApiResult delete(Integer id);
}
