package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.Asset;
import com.zans.mms.vo.asset.AssetQueryVO;
import com.zans.mms.vo.asset.AssetViewResVO;
import com.zans.mms.vo.asset.SwitcherMacInterfaceResVO;
import com.zans.mms.vo.radius.QzViewRespVO;

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

    AssetViewResVO getViewById(Long id);

    Integer getIdByUniqueId(String assetId);

    Boolean existRelation(String id);

    int deleteByUniqueId(String id);

    ApiResult chooseAssetList(AssetQueryVO vo);

    ApiResult batchAddAsset(String newFileName, String originName, UserSession userSession,String type);

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
     * @Author pancm
     * @Description 根据IP查询
     * @Date  2020/10/29
     * @Param [ip]
     * @return com.zans.portal.vo.radius.QzViewRespVO
     **/
    QzViewRespVO findByIp(String ip);

    /**
    * @Author beiming
    * @Description
    * @Date  6/16/21
    * @Param
    * @return
    **/
    List<SwitcherMacInterfaceResVO> getSwitchMacInterface();


    SwitcherMacInterfaceResVO getSwitchMacInterfaceByPort(String portId);
    SwitcherMacInterfaceResVO getSwitchMacInterfaceByPortDown(String portId);

    void resetStatusByMac(String mac);
}
