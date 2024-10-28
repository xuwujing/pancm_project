package com.zans.portal.dao;

import com.zans.base.vo.SelectVO;
import com.zans.portal.model.Area;
import com.zans.portal.vo.area.AreaRespVO;
import com.zans.portal.vo.area.AreaSearchVO;
import com.zans.portal.vo.area.RegionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface AreaMapper extends Mapper<Area> {

    List<SelectVO> findAreaToSelect();

    List<SelectVO> findRegionToSelect(@Param("city") Integer city);

    Area findAreaWithSwTypeById(@Param("id") Integer id);

    List<RegionVO> findRegion(@Param("city") Integer city);

    Integer findAreaByName(@Param("name") String name);

    Integer findAreaCountByName(@Param("name") String name, @Param("id") Integer id);

    List<AreaRespVO> findAreaList(@Param("reqVo") AreaSearchVO reqVO);

    List<RegionVO> findRegionByParentId(@Param("city") Integer city, @Param("parentId") Integer parentId);

    List<RegionVO> findAreaByRegion(@Param("region") Integer region);

    RegionVO findRegionById(@Param("regionId") Integer regionId);

    List<SelectVO> findAreaByParentId(@Param("parentId") Integer parentId);

    List<Area> getListCondition(Map<String, Object> map);


}
