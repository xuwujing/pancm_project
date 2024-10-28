package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.AssetBaselineDao;
import com.zans.portal.dao.AssetBranchMapper;
import com.zans.portal.dao.AssetMapMapper;
import com.zans.portal.model.AssetBaseline;
import com.zans.portal.model.AssetBranch;
import com.zans.portal.service.IAssetMapService;
import com.zans.portal.service.IConstantItemService;
import com.zans.portal.vo.AssetBaselineVO;
import com.zans.portal.vo.asset.map.AssetMapConvergeRespVO;
import com.zans.portal.vo.asset.map.AssetMapInitReqVO;
import com.zans.portal.vo.asset.map.ConvergeVO;
import com.zans.portal.vo.asset.req.AssetMapUpdateReqVO;
import com.zans.portal.vo.asset.req.AssetReqVO;
import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import com.zans.portal.vo.common.MapAssetVO;
import com.zans.portal.vo.common.MapCatalogueVO;
import com.zans.portal.vo.common.TreeSelect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.zans.portal.config.GlobalConstants.MAP_ZOOM_LEVEL;

@Service
@Slf4j
public class AssetMapServiceImpl implements IAssetMapService {
    @Autowired
    AssetMapMapper assetMapMapper;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    AssetBaselineDao assetBaselineDao;

    @Resource
    private AssetBranchMapper assetBranchMapper;

    @Override
    public AssetMapConvergeRespVO assetMap(AssetMapInitReqVO reqVO) {
        AssetMapConvergeRespVO result = new AssetMapConvergeRespVO();
        List<AssetMapRespVO> pointList = new ArrayList<>();
        List<ConvergeVO> convergeList = new ArrayList<>();

        //缩放级别 10 14 17
        Map<Object, String> zoomLevelMap = constantItemService.findItemsMapByDict(MAP_ZOOM_LEVEL);
        String s = zoomLevelMap.getOrDefault(reqVO.getZoomLevel(), "0");
        //汇聚阈值
        String threshold = zoomLevelMap.getOrDefault(0, "30");
        //地图切片为几等分
        BigDecimal shardingNumber = new BigDecimal(s);

        //如果是切分0等分 则直接查
        if (shardingNumber.compareTo(BigDecimal.ZERO) == 0) {
            List<AssetMapRespVO> mapList = assetMapMapper.assetMapList(reqVO);
            pointList.addAll(mapList);
            result.setPointList(pointList);
            result.setConvergeList(convergeList);
            return result;
        }

        //latitudeFactor(纬度因子)=右上角纬度-左下角纬度
        BigDecimal latitudeFactor = (reqVO.getRightLatitude().subtract(reqVO.getLeftLatitude())).divide(shardingNumber, 14, RoundingMode.HALF_UP);
        //longitudeFactor(经度因子)=右上角经度-左下角经度
        BigDecimal longitudeFactor = (reqVO.getRightLongitude().subtract(reqVO.getLeftLongitude())).divide(shardingNumber, 14, RoundingMode.HALF_UP);


        AssetMapInitReqVO shardingVO = null;
        for (int m = 1; m <= shardingNumber.intValue(); m++) {
            for (int n = 1; n <= shardingNumber.intValue(); n++) {
                shardingVO = new AssetMapInitReqVO();
                // shardingLeftLat = latitudeFactor*(m-1) + leftLatitude
                shardingVO.setLeftLatitude(reqVO.getLeftLatitude().add(latitudeFactor.multiply(new BigDecimal(m - 1))));
                // shardingLeftLon = longitudeFactor*(n-1) + leftLongitude
                shardingVO.setLeftLongitude(reqVO.getLeftLongitude().add(longitudeFactor.multiply(new BigDecimal(n - 1))));

                //切片右上角只需用左下角的经纬度加上对应因子
                shardingVO.setRightLatitude(shardingVO.getLeftLatitude().add(latitudeFactor));
                shardingVO.setRightLongitude(shardingVO.getLeftLongitude().add(longitudeFactor));

                shardingVO.setBrandName(reqVO.getBrandName());
                shardingVO.setBuildTypes(reqVO.getBuildTypes());
                List<AssetMapRespVO> mapList = assetMapMapper.assetMapList(shardingVO);
                if (mapList != null && mapList.size() > 0) {
                    ConvergeVO convergeVO = null;
                    //如果切片内 （存在超过汇聚阈值并且地图级别小于17） 或者地图缩放级别为13以下如13 12 11 。。。 则汇聚
                    if ((mapList.size() > Integer.valueOf(threshold) && reqVO.getZoomLevel().intValue() < 19)
                            || reqVO.getZoomLevel().intValue() < 14) {
                        convergeVO = new ConvergeVO();
                        convergeVO.setConvergeNumber(mapList.size());
                        convergeVO.setLatitude(mapList.get(0).getLatitude());
                        convergeVO.setLongitude(mapList.get(0).getLongitude());

                        convergeVO.setRightLongitude(shardingVO.getRightLongitude().toString());
                        convergeVO.setRightLatitude(shardingVO.getRightLatitude().toString());
                        convergeVO.setLeftLongitude(shardingVO.getLeftLongitude().toString());
                        convergeVO.setLeftLatitude(shardingVO.getLeftLatitude().toString());
                        //设置在线 、离线数量
                        convergeVO.resetOnlineOfflineNumber(mapList);
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
    public List<MapCatalogueVO> assetMapForTreeList(MapAssetVO reqVO) {
        return assetMapMapper.assetMapForTreeList(reqVO);
    }

    @Override
    public List<MapCatalogueVO> assetMapForXGJGTreeList(MapAssetVO reqVO) {
        return assetMapMapper.assetMapForXGJGTreeList(reqVO);
    }

    @Override
    public ApiResult updateAssetMap(AssetMapUpdateReqVO searchVO) {
        AssetBaseline assetBaseline = new AssetBaseline();
        assetBaseline.setId(Long.valueOf(searchVO.getId()));
        assetBaseline.setLatitude(searchVO.getLatitude());
        assetBaseline.setLongitude(searchVO.getLongitude());
        assetBaselineDao.update(assetBaseline);
        return ApiResult.success();
    }

    @Override
    public List<TreeSelect> getMapTreeAndData(Integer pareId) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        List<AssetBranch> branchList = assetBranchMapper.findToTreeSelect(pareId);
        if(StringHelper.isEmpty(branchList)){
            AssetBranch  branch = assetBranchMapper.findById(pareId);
            if(branch == null){
                log.warn("parentId：{}，没有该分组!",pareId);
                return treeSelectList;
            }
            AssetBaselineVO baselineVO = new AssetBaselineVO();
            baselineVO.setAssetBranchId(branch.getId());
            List<AssetBaselineVO> baselineVOList = assetBaselineDao.assetMapForTreeList(baselineVO);
            for (AssetBaselineVO assetBaselineVO : baselineVOList) {
                TreeSelect node2 = TreeSelect.builder().id(assetBaselineVO.getId())
                        .label(assetBaselineVO.getPointName()).seq(assetBaselineVO.getSeq()).isShow(false)
                        .children(new LinkedList<>())
                        .data(assetBaselineVO)
                        .build();
                treeSelectList.add(node2);
            }
            return treeSelectList;
        }


        Map<Integer, TreeSelect> treeMap = new HashMap<>(branchList.size());

        for (AssetBranch assetBranch : branchList) {
            AssetBaselineVO baselineVO = new AssetBaselineVO();
            TreeSelect node = TreeSelect.builder().id(assetBranch.getId())
                    .label(assetBranch.getName()).seq(assetBranch.getSeq()).isShow(false)
                    .level(assetBranch.getLevel())
                    .children(new LinkedList<>())
//                    .data(baselineVOList)
                    .build();
            if (node != null && node.getChildren() == null) {
                node.setChildren(new LinkedList<>());
            }
            treeMap.put(assetBranch.getId(), node);
            Integer parentId = assetBranch.getParentId();
            if (parentId == 0) {
                treeSelectList.add(node);
                continue;
            }

            TreeSelect parent = treeMap.get(parentId);
            if (parent == null ) {
                treeSelectList.add(node);
                continue;
            }

            if (assetBranch.getLevel() == 3) {
                List<AssetBaselineVO> baselineVOList = assetBaselineDao.assetMapForTreeList(baselineVO);
                List<TreeSelect> treeSelectList2 = new ArrayList<>();
                for (AssetBaselineVO assetBaselineVO : baselineVOList) {
                    TreeSelect node2 = TreeSelect.builder().id(assetBaselineVO.getId())
                            .label(assetBaselineVO.getPointName()).seq(assetBaselineVO.getSeq()).isShow(false)
                            .children(new LinkedList<>())
                            .data(assetBaselineVO)
                            .build();
                    treeSelectList2.add(node2);
                }
                node.setChildren(treeSelectList2);
            }
            parent.addChild(node);

        }

        return treeSelectList;
    }

    @Override
    public List<TreeSelect> getMapTree(Integer parId) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        List<AssetBranch> branchList = assetBranchMapper.findToTreeSelect(parId);
        Map<Integer, TreeSelect> treeMap = new HashMap<>(branchList.size());
        for (AssetBranch assetBranch : branchList) {
            AssetBaselineVO baselineVO = new AssetBaselineVO();
            baselineVO.setAssetBranchId(assetBranch.getId());
            //添加子节点以及属性
            TreeSelect node = TreeSelect.builder().id(assetBranch.getId())
                    .label(assetBranch.getName()).seq(assetBranch.getSeq()).isShow(false)
                    .level(assetBranch.getLevel())
                    .children(new LinkedList<>())
                    .build();
            //如果节点不为空，但是子节点为空，那么设置默认空数组
            if (node != null && node.getChildren() == null) {
                node.setChildren(new LinkedList<>());
            }
            treeMap.put(assetBranch.getId(), node);
            Integer parentId = assetBranch.getParentId();
            //如果是第一级，这添加下级节点
            if (parentId == 0) {
                treeSelectList.add(node);
                continue;
            }

            TreeSelect parent = treeMap.get(parentId);
            if (parent == null) {
                treeSelectList.add(node);
                continue;
            }
            parent.addChild(node);
        }

        return treeSelectList;
    }

    @Override
    public List<AssetBaselineVO> assetMapForTreeList(Integer assetBranchId) {
        AssetBaselineVO assetBaselineVO = new AssetBaselineVO();
        assetBaselineVO.setAssetBranchId(assetBranchId);
        return assetBaselineDao.assetMapForTreeList(assetBaselineVO);
    }


    @Override
    public PageResult<AssetMapRespVO> assetMapListPage(AssetReqVO reqVO) {
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());
        List<AssetMapRespVO> list = assetMapMapper.assetMapList2(reqVO);
        return new PageResult<AssetMapRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }


}
