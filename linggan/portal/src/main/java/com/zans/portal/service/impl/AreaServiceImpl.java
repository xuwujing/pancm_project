package com.zans.portal.service.impl;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.SelectVO;
import com.zans.portal.dao.AreaMapper;
import com.zans.portal.model.Area;
import com.zans.portal.service.IAreaService;
import com.zans.portal.vo.area.RegionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static com.zans.portal.config.GlobalConstants.REGION_LEVEL_ONE;
import static com.zans.portal.config.GlobalConstants.REGION_LEVEL_TWO;

@Service
@Slf4j
public class AreaServiceImpl extends BaseServiceImpl<Area> implements IAreaService {

    @Value("${api.area.city}")
    Integer city;

    AreaMapper areaMapper;

    @Resource
    public void setAreaMapper(AreaMapper areaMapper) {
        super.setBaseMapper(areaMapper);
        this.areaMapper = areaMapper;
    }

    @Override
    @Cacheable(cacheNames = "AREA_LIST")
    public List<SelectVO> findAreaToSelect() {
        return areaMapper.findAreaToSelect();
    }

    @Override
    @Cacheable(cacheNames = "AREA_MAP")
    public Map<Object, String> findAreaToMap() {
        List<SelectVO> selectList = this.findAreaToSelect();
        return selectList.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
    }

    @Override
    public String getAreaNameById(Integer id) {
        return findAreaToMap().get(id);
    }

    @Override
    @Cacheable(cacheNames = "REGION_LIST", key = "#level")
    public List<SelectVO> findRegionToSelect(int level) {
        List<RegionVO> regionList = areaMapper.findRegion(this.city);
        List<Integer> oneIdList = new ArrayList<>();
        List<Integer> twoIdList = new ArrayList<>();

        List<SelectVO> levelOne = new LinkedList<>();
        for (RegionVO vo : regionList) {
            if (vo.getParentId() == 0) {
                levelOne.add(vo.toSelectVO());
                oneIdList.add(vo.getRegionId());
            }
        }
        if (level == REGION_LEVEL_ONE) {
            return levelOne;
        }
        List<SelectVO> levelTwo = new LinkedList<>();
        for (RegionVO vo : regionList) {
            Integer pid = vo.getParentId();
            if (oneIdList.contains(pid)) {
                levelTwo.add(vo.toSelectVO());
                twoIdList.add(vo.getRegionId());
            }
        }
        if (level == REGION_LEVEL_TWO) {
            return levelTwo;
        }

        List<SelectVO> levelThree = new LinkedList<>();
        for (RegionVO vo : regionList) {
            Integer pid = vo.getParentId();
            if (twoIdList.contains(pid)) {
                levelThree.add(vo.toSelectVO());
            }
        }
        return levelThree;
    }

    @Override
    public List<RegionVO> findRegionByParentId(Integer parentId) {
        return areaMapper.findRegionByParentId(this.city, parentId);
    }

    @Override
    public RegionVO getRegionById(Integer regionId) {
        return areaMapper.findRegionById(regionId);
    }

}
