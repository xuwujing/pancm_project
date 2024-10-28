package com.zans.portal.dao;

import com.zans.portal.model.Asset;
import com.zans.portal.vo.AliveHeartbeatVO;
import com.zans.portal.vo.arp.AssetRespVO;
import com.zans.portal.vo.arp.AssetSearchVO;
import com.zans.portal.vo.arp.ExcelAssetVO;
import com.zans.portal.vo.arp.NetworkSwitchVO;
import com.zans.portal.vo.asset.AssetVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchDisposeReqVO;
import com.zans.portal.vo.asset.req.AssetEditCoordinatesReqVO;
import com.zans.portal.vo.asset.resp.AssetStatisRespVO;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.chart.CompareUnit;
import com.zans.portal.vo.chart.CountUnit;
import com.zans.portal.vo.common.ChartStatisVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface AssetMapper extends Mapper<Asset> {
    int disposeAsset(@Param("reqVO") AssetBranchDisposeReqVO reqVO);

    /**
     * 根据IP地址查询数据
     *
     * @param ipAddr
     * @return
     */
    AssetVO findByIpAddr(@Param("ipAddr") String ipAddr);

    /**
     * 资产列表查看详情
     *
     * @param id
     * @return
     */
    AssetRespVO findAssetDynamicDetail(@Param("id") Integer id);

    AssetRespVO findAssetDynamicDetailByIp(@Param("ip") String ip);

    Asset uniqueByCondition(HashMap<String, Object> map);


    /**
     * 获取资产设备类型维度在线率、资产总数等统计数据
     * @return
     */
    List<AssetStatisRespVO> findAssetDeviceStatis();


    List<AssetStatisRespVO> findAssetDeviceStatis2();

    /**
     * 获取资产交换机设备类型维度在线率、资产总数等统计数据
     * @return
     */
    List<AssetStatisRespVO> findAssetDeviceStatisBySwitch();


    CountUnit findAuthByDate(@Param("date") String date);

    CountUnit findRefuseByDate(@Param("date") String date);


    List<ExcelAssetVO> findAssetsForExport2(@Param("asset") AssetSearchVO asset);

    NetworkSwitchVO findNetWorkSwitchByMacTopo(String mac);

    List<NetworkSwitchVO> findNetWorkSwitchByMac(String mac);

    NetworkSwitchVO findSwitcherTopo(String swIp);

	int editAssetCoordinates(AssetEditCoordinatesReqVO reqVO);

	int updateAliveByIpAddr(AssetVO assetVO);

	AliveHeartbeatVO findAliveHeartBeat(AssetVO assetVO);


    List<CircleUnit> getBrand();

    /**
    * @Author beixing
    * @Description  品牌在线情况
    * @Date  2021/12/14
    * @Param
    * @return
    **/
    List<CompareUnit> findAssetByBrand();

     /**
     * @Author beixing
     * @Description  设备品牌统计
     * @Date  2021/12/16
     * @Param
     * @return
     **/
    List<ChartStatisVO> countGroupByBrand();


    List<ChartStatisVO> countGroupBySwType();

    Map<String,String> queryDeviceType();


    List<CompareUnit> findAssetByBrandByOther();

    AssetRespVO findAssetNewDynamicDetailByIp(String ipAddr);
}
