package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.SysCustomData;
import com.zans.portal.model.SysCustomFieldOption;

import java.util.List;

public interface ICustomDataService extends BaseService<SysCustomData> {

    Integer findCountByFieldId(Integer fieldId);

}
