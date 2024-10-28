package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.Area;
import com.zans.mms.vo.area.AreaRespVO;
import com.zans.mms.vo.area.AreaSearchVO;
import com.zans.mms.vo.area.RegionVO;

import java.util.List;
import java.util.Map;

public interface IAreaService extends BaseService<Area> {

    List<SelectVO> findAreaToSelect();

    Map<Object, String> findAreaToMap();




}
