package com.zans.portal.dao;

import com.zans.portal.model.SysCustomFieldOption;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysCustomFieldOptionMapper extends Mapper<SysCustomFieldOption> {

    List<SysCustomFieldOption> findByFieldId(@Param("fieldId") Integer fieldId);

}