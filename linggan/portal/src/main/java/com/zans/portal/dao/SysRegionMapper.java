package com.zans.portal.dao;

import com.zans.portal.model.SysRegion;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysRegionMapper extends Mapper<SysRegion> {

    List<SysRegion> findRegionByName(@Param("regionName") String regionName);

    int findByRegionName(@Param("regionName") String regionName,@Param("regionId") Integer regionId);

    String getRegionNameById(@Param("id")Integer id);

}