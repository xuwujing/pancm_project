package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.Asset;
import com.zans.portal.model.AssetProfile;
import com.zans.portal.vo.arp.*;
import com.zans.portal.vo.asset.AssetVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchDisposeReqVO;
import com.zans.portal.vo.asset.req.AssetAddReqVO;
import com.zans.portal.vo.asset.req.AssetEditCoordinatesReqVO;
import com.zans.portal.vo.asset.req.AssetEditReqVO;
import com.zans.portal.vo.asset.resp.AssetDetailRespVO;
import com.zans.portal.vo.asset.resp.AssetStatisRespVO;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.radius.QzViewRespVO;

import java.util.HashMap;
import java.util.List;

public interface IAssetService extends BaseService<Asset> {

    /**
     * 处置资产
     *
     * @param reqVO
     * @param userSession
     * @return
     */
    int disposeAsset(AssetBranchDisposeReqVO reqVO, UserSession userSession);

    /**
     * 删除资产(逻辑删除)
     *
     * @param id      资产ID
     * @param ipAddr
     * @return        null表示没有找到对应资产
     */
    AssetProfile deleteAsset(Integer id, String ipAddr, UserSession userSession);

    /**
     * 根据资产ID查询资产详情
     *
     * @param id
     * @param ipAddr
     * @return
     */
    AssetDetailRespVO getAssetDetail(Integer id, String ipAddr);

    /**
     * 编辑资产信息
     *
     * @param reqVO
     * @return     0表示更新失败
     */
    int editAsset(AssetEditReqVO reqVO,UserSession userSession);

    /**
     * 单个添加资产
     *
     * @param reqVO
     * @return
     */
    int addAsset(AssetAddReqVO reqVO,UserSession userSession);

    int addOrUpdate(AssetAddReqVO reqVO,UserSession userSession);

    /**
     * 批量导入资产信息
     *
     * @param newFileName
     * @param originName
     * @param userSession
     * @return
     */
    ApiResult batchAddAsset(String newFileName, String originName,Integer assetBranchId,Integer assetGuardLineId, UserSession userSession);

    /**
     * 获取资产列表查看详情
     *
     * @param id
     * @param ipAddr
     * @return
     */
    AssetRespVO getAssetDynamicDetail(Integer id, String ipAddr);



    ApiResult getDeny(ArpViewVO res);

    AssetVO findByIpAddr(String ip);

    /**
     * @Author pancm
     * @Description 根据IP查询
     * @Date  2020/10/29
     * @Param [ip]
     * @return com.zans.portal.vo.radius.QzViewRespVO
     **/
    QzViewRespVO findByIp(String ip);

    Asset uniqueByCondition(HashMap<String, Object> map);



    /**
     * 获取资产设备类型维度在线率、资产总数等统计数据
     * @return
     */
    List<AssetStatisRespVO> getAssetDeviceStatis();



    List<String[]> findRadiusMacByDate();

    PageResult<AssetRespVO> getAssetPage(AssetSearchVO asset);

    PageResult<AssetRespVO> getAssetNewListPage(AssetSearchVO asset);

    List<ExcelAssetVO> getExportAssets(AssetSearchVO req);



    ArpSwitchVO getByMacAdress(String macAddr);

    List<NetworkSwitchVO> getNetWorkSwitchByMacTopo(String macAddr);

	int editAssetCoordinates(AssetEditCoordinatesReqVO reqVO);

    List<CircleUnit>  getBrand();

    AssetRespVO getAssetNewDynamicDetail(Integer id, String ipAddr);
}
