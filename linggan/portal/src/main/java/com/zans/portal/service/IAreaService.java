package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.Area;
import com.zans.portal.vo.area.RegionVO;

import java.util.List;
import java.util.Map;

public interface IAreaService extends BaseService<Area> {

    List<SelectVO> findRegionToSelect(int level);

    String getAreaNameById(Integer id);

    List<SelectVO> findAreaToSelect();

    Map<Object, String> findAreaToMap();

    List<RegionVO> findRegionByParentId(Integer parentId);

    RegionVO getRegionById(Integer regionId);
}
