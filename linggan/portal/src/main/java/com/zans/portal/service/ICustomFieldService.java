package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.SysCustomField;
import com.zans.portal.vo.custom.CustomOptionRespVO;
import com.zans.portal.vo.custom.CustomReqVO;
import com.zans.portal.vo.custom.CustomRespVO;

import java.util.List;

public interface ICustomFieldService extends BaseService<SysCustomField> {

    PageResult<CustomRespVO> getCustomFieldPage(CustomReqVO reqVO);

    List<CustomRespVO> findCustomDataList(String id, String moduleName);

    Boolean updateCustomFieldInTable(String id, String tableName, String column, String value);

    Integer getMaxSort(String moduleName);

}
