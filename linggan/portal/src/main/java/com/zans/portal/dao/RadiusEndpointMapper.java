package com.zans.portal.dao;

import com.zans.portal.model.RadiusEndpoint;
import com.zans.portal.model.RadiusEndpointLog;
import com.zans.portal.vo.arp.AssetRespVO;
import com.zans.portal.vo.arp.AssetSearchVO;
import com.zans.portal.vo.asset.req.AssetReqVO;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.chart.CountUnit;
import com.zans.portal.vo.chart.TwoDimensionGroupUnit;
import com.zans.portal.vo.radius.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface RadiusEndpointMapper extends Mapper<RadiusEndpoint> {

    List<EndPointViewVO> findEndPointList(@Param("reqVo") EndPointReqVO reqVO);

    List<EndPointViewVO> findBlockEndPointList(@Param("reqVo") EndPointReqVO reqVO);

    /**
     * @Author pancm
     * @Description 新增一个通过pass查询设备信息，告警中心使用
     * @Date  2020/9/9
     * @Param [reqVO]
     * @return com.zans.portal.vo.radius.EndPointViewVO
     **/
    EndPointViewVO findEndPointByPass(@Param("pass") String pass);

    /**
     * @Author pancm
     * @Description 新增一个通过pass查询id，告警中心使用
     * @Date  2020/9/9
     * @Param [reqVO]
     * @return com.zans.portal.vo.radius.EndPointViewVO
     **/
    EndPointViewVO findEndPointIdByPass(@Param("pass") String pass);

    /**
     * @Author pancm
     * @Description 获取资产总数相关
     * @Date  2020/10/20
     * @Param []
     * @return java.util.List<com.zans.portal.vo.chart.CircleUnit>
     **/
    List<CircleUnit> getAssetTotal();


    /**
     * @Author pancm
     * @Description 获取准入总数相关
     * @Date  2020/10/20
     * @Param []
     * @return java.util.List<com.zans.portal.vo.chart.CircleUnit>
     **/
    List<CircleUnit> getAccessTotal();


    /**
     * @Author pancm
     * @Description 获取告警总数相关
     * @Date  2020/10/20
     * @Param []
     * @return java.util.List<com.zans.portal.vo.chart.CircleUnit>
     **/
    List<CircleUnit> getAlertTotal();

    /**
     * @Author pancm
     * @Description
     * @Date  2020/10/20 资产全景需要
     * @Param []
     * @return java.util.List<com.zans.portal.vo.chart.CircleUnit>
     **/
    List<CircleUnit> getAssetPanoramicTotal();


    /**
     * 查看隔离区的终端设备
     * @param reqVO
     * @return
     */
    List<EndPointViewVO> findQzEndPointList(@Param("reqVo") QzReqVO reqVO);

    RadiusEndpoint findEndpointByMacMin(String mac);

    QzRespVO findQzById(Integer id);

    EndPointViewVO findById(@Param("id") Integer id, @Param("arpId") Integer arpId);

    EndPointViewVO findBlockById(@Param("id") Integer id, @Param("arpId") Integer arpId);


    List<CountUnit> summary();

    List<CountUnit> findByDevice();


    List<CountUnit> getGroupByBrand();

    List<CircleUnit> networkin();

    List<CircleUnit> getCountOfRiskType(@Param("dayStart")  String dayStart);


    List<AssetMapRespVO> assetMapList(AssetReqVO reqVO);



    List<TwoDimensionGroupUnit> assetDepartmentOnlineRate();

    List<AssetMapRespVO> assetNumBuild(AssetReqVO reqVO);

    List<TwoDimensionGroupUnit> assetDepartmentAuthRate();

    QzViewRespVO findBaseByCurMac(@Param("mac")  String mac);

    QzViewRespVO findCurByCurMac(@Param("mac")  String mac);

    QzViewRespVO findCurByIp(@Param("ipAddr")  String ipAddr);

    List<AssetRespVO> findAssetsForList(@Param("asset") AssetSearchVO asset);

    int updateEnableByUsername(@Param("username") String username, @Param("remark") String remark,@Param("enableStatus") Integer enableStatus);

    List<CircleUnit> assetOnlineRate();

    Map<String,Long> findOnlinAccessStatis();


    void saveRadiusEndpointLog(RadiusEndpointLog radiusEndpointLog);


     List<RadiusEndpointLog> findRadiusEndpointLog(@Param("username") String username);

    void updateOtherScanStatusByMac(@Param("otherScanStatus") int otherScanStatus,  @Param("ipAddr") String ipAddr);

    String getMacByIp(String hostIp);

    List<AssetRespVO> getAssetNewListPage(@Param("asset") AssetSearchVO asset);
}
