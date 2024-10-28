package com.zans.portal.dao;

import com.zans.portal.model.TAreaInit;
import com.zans.portal.vo.area.AreaInitResVO;
import com.zans.portal.vo.area.AreaInitSearchVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TAreaInitMapper extends Mapper<TAreaInit> {

    List<AreaInitResVO> findAreaInitList(@Param("reqVo") AreaInitSearchVO reqVO);

    TAreaInit getByAreaIdAndDeviceTypeId(@Param("areaId") Integer areaId, @Param("deviceTypeId") Integer deviceTypeId, @Param("id") Integer id);

    Integer getCountByAreaId(@Param("areaId") Integer areaId);

    Integer getCountByDeviceTypeId(@Param("deviceTypeId") Integer deviceTypeId);

}