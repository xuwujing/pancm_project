package com.zans.portal.dao;

import com.zans.portal.model.SysCustomData;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface SysCustomDataMapper extends Mapper<SysCustomData> {

    Integer findCountByFieldId(@Param("fieldId") Integer fieldId);

}