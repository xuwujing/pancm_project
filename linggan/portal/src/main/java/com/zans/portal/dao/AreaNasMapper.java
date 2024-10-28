package com.zans.portal.dao;

import com.zans.portal.model.AreaNas;
import com.zans.portal.vo.area.AreaNasVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AreaNasMapper extends Mapper<AreaNas> {

    AreaNasVO findAreaNasByMac(@Param("mac") String mac);
}