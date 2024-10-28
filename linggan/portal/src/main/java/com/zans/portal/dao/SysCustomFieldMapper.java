package com.zans.portal.dao;

import com.zans.portal.model.SysCustomField;
import com.zans.portal.vo.custom.CustomOptionRespVO;
import com.zans.portal.vo.custom.CustomReqVO;
import com.zans.portal.vo.custom.CustomRespVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysCustomFieldMapper extends Mapper<SysCustomField> {

    List<CustomRespVO> findCustomFieldList(@Param("reqVo") CustomReqVO reqVO);

    List<CustomRespVO> findCustomDataList(@Param("id") String id, @Param("moduleName") String moduleName);

    List<CustomOptionRespVO> findOptionsByFieldId(@Param("id") Integer fieldId);

    Boolean updateCustomFieldInTable(@Param("id") String id, @Param("tableName") String tableName, @Param("column") String column, @Param("value") String value);

    Integer getMaxSort(@Param("moduleName") String moduleName);

}