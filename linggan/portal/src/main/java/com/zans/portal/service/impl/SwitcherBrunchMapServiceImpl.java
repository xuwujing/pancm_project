package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.SysSwitcherBranchMapper;
import com.zans.portal.model.SysSwitcherBranch;
import com.zans.portal.service.*;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.map.ConvergeVO;
import com.zans.portal.vo.switcher.SwitchBranchConvergeMapResVO;
import com.zans.portal.vo.switcher.SwitchBranchMapInitVO;
import com.zans.portal.vo.switcher.SwitchBranchResVO;
import com.zans.portal.vo.switcher.SwitchBranchSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SwitcherBrunchMapServiceImpl extends BaseServiceImpl<SysSwitcherBranch> implements ISwitcherBrunchMapService {


    SysSwitcherBranchMapper switcherBranchMapper;
    @Autowired
    IAreaService areaService;
    @Autowired
    IRegionService regionService;
    @Autowired
    ISwitcherService switcherService;

    @Autowired
    IConstantItemService constantItemService;

    @Resource
    public void setSysSwitcherBranchMapper(SysSwitcherBranchMapper switcherBranchMapper) {
        super.setBaseMapper(switcherBranchMapper);
        this.switcherBranchMapper = switcherBranchMapper;
    }


    @Override
    public SwitchBranchConvergeMapResVO mapList(SwitchBranchMapInitVO reqVO) {
        SwitchBranchConvergeMapResVO result = new SwitchBranchConvergeMapResVO();
        List<SwitchBranchResVO> pointList = new ArrayList<>();
        List<ConvergeVO>   convergeList = new ArrayList<>();

//        //缩放级别 10 14 17
        Map<Object, String> zoomLevelMap = constantItemService.findItemsMapByDict("zoom_level");//MAP_ZOOM_LEVEL
        String s = zoomLevelMap.getOrDefault(reqVO.getZoomLevel(),"0");
//        //汇聚阈值
        String threshold = zoomLevelMap.getOrDefault(0,"30");
//        //地图切片为几等分
        BigDecimal shardingNumber = new BigDecimal(s);
//
//        //如果是切分0等分 则直接查
        if (shardingNumber.compareTo(BigDecimal.ZERO)==0){
            List<SwitchBranchResVO> mapList = switcherBranchMapper.mapList(reqVO);
            pointList.addAll(mapList);
            result.setPointList(pointList);
            result.setConvergeList(convergeList);
            return result;
        }
//        //latitudeFactor(纬度因子)=右上角纬度-左下角纬度
        BigDecimal latitudeFactor = (reqVO.getRightLatitude().subtract(reqVO.getLeftLatitude())).divide(shardingNumber, 14, RoundingMode.HALF_UP);
//        //longitudeFactor(经度因子)=右上角经度-左下角经度
        BigDecimal longitudeFactor = (reqVO.getRightLongitude().subtract(reqVO.getLeftLongitude())).divide(shardingNumber, 14, RoundingMode.HALF_UP);

        SwitchBranchMapInitVO shardingVO = null;
        for (int m = 1; m <= shardingNumber.intValue(); m++) {
            for (int n = 1; n <= shardingNumber.intValue(); n++) {
                shardingVO = new SwitchBranchMapInitVO();
                // shardingLeftLat = latitudeFactor*(m-1) + leftLatitude
                shardingVO.setLeftLatitude(reqVO.getLeftLatitude().add(latitudeFactor.multiply(new BigDecimal(m-1))));
                // shardingLeftLon = longitudeFactor*(n-1) + leftLongitude
                shardingVO.setLeftLongitude(reqVO.getLeftLongitude().add(longitudeFactor.multiply(new BigDecimal(n-1))));

                //切片右上角只需用左下角的经纬度加上对应因子
                shardingVO.setRightLatitude(shardingVO.getLeftLatitude().add(latitudeFactor));
                shardingVO.setRightLongitude(shardingVO.getLeftLongitude().add(longitudeFactor));
                shardingVO.setProjectIds(reqVO.getProjectIds());
                List<SwitchBranchResVO> mapList = switcherBranchMapper.mapList(shardingVO);
                if (mapList !=null && mapList.size()>0) {
                    ConvergeVO convergeVO = null;
                    //如果切片内 （存在超过汇聚阈值并且地图级别小于17） 或者地图缩放级别为13以下如13 12 11 。。。 则汇聚
                    if ((mapList.size()>Integer.valueOf(threshold) && reqVO.getZoomLevel().intValue()<17)
                            || reqVO.getZoomLevel().intValue()<14 ) {
                        convergeVO = new ConvergeVO();
                        convergeVO.setConvergeNumber(mapList.size());
                        convergeVO.setLatitude(mapList.get(0).getLat().toString());
                        convergeVO.setLongitude(mapList.get(0).getLon().toString());

                        convergeVO.setRightLongitude(shardingVO.getRightLongitude().toString());
                        convergeVO.setRightLatitude(shardingVO.getRightLatitude().toString());
                        convergeVO.setLeftLongitude(shardingVO.getLeftLongitude().toString());
                        convergeVO.setLeftLatitude(shardingVO.getLeftLatitude().toString());
                        convergeList.add(convergeVO);
                    } else {
                        //如果没有或者一个点
                        pointList.addAll(mapList);
                    }
                }
            }
        }
        result.setPointList(pointList);
        result.setConvergeList(convergeList);
        return result;
    }

    @Override
    public PageResult<AssetMapRespVO> assetSwitchMapListPage(SwitchBranchSearchVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        List<AssetMapRespVO> list =  switcherBranchMapper.assetSwitchMapListPage(reqVO);
        return new PageResult<AssetMapRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());

    }


}
