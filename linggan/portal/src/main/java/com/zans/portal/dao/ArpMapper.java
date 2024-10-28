package com.zans.portal.dao;

import com.zans.base.vo.SelectVO;
import com.zans.portal.model.Arp;
import com.zans.portal.vo.arp.*;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.chart.CompareUnit;
import com.zans.portal.vo.chart.CountUnit;
import com.zans.portal.vo.chart.TwoDimensionGroupUnit;
import com.zans.portal.vo.radius.QzViewRespVO;
import com.zans.portal.vo.stats.ArpHourStatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ArpMapper extends Mapper<Arp> {

    List<ExcelAssetVO> findAssetsForExport2(@Param("asset") AssetSearchVO asset);

    AssetRespVO getAsset(@Param("id") Integer id);


    List<AssetRespVO> findChangeList(@Param("asset") ChangeSearchVO asset, @Param("ipStatus") Integer ipStatus);



    List<CountUnit> getGroupByBrand();

    List<CountUnit> getGroupByDeviceType();

    Integer getTotalCount();

    Integer getAliveCount();

    List<ArpHourStatVO> getArpHourStats(@Param("limit") int limit);

    List<CircleUnit> getCountOfRiskType(@Param("dayStart") String dayStart);


    Integer getCountByBrand(@Param("brand") String brand);



    List<CountUnit> getGroupByBrandAndSearch(@Param("req") AssetSearchVO req);

    List<CountUnit> getGroupByDeviceTypeAndSearch(@Param("req") AssetSearchVO req);

    Integer getByDeviceTypeId(@Param("deviceTypeId") Integer deviceTypeId);



    ArpSwitchVO getByMacAdress(@Param("macAdress") String macAdress);

    List<NetworkSwitchVO> findNetWorkSwitchByMac(@Param("mac") String mac);

    NetworkSwitchVO findNetWorkSwitchByMacTopo(@Param("mac") String mac);

    NetworkSwitchVO findSwitcherTopo(@Param("swIp") String swIp);

    QzViewRespVO findByCurMac(@Param("mac") String mac);

    List<CountUnit> summary();

    List<CountUnit> findByDevice();


    CountUnit findAuthByDate(@Param("date") String date);

    CountUnit findRefuseByDate(@Param("date") String date);

    List<CircleUnit> getCountOfArpReport();


}
