package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.guard.AreaMapper;
import com.zans.mms.model.Area;
import com.zans.mms.service.IAreaService;
import com.zans.mms.vo.area.AreaRespVO;
import com.zans.mms.vo.area.AreaSearchVO;
import com.zans.mms.vo.area.RegionVO;
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

@Service
@Slf4j
public class AreaServiceImpl extends BaseServiceImpl<Area> implements IAreaService {

    public static int REGION_LEVEL_ONE = 1;
    public static int REGION_LEVEL_TWO = 2;


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


}
