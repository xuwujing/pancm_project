package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.SysCustomFieldOption;

import java.util.List;

public interface ICustomFieldOptionService extends BaseService<SysCustomFieldOption> {

    List<SysCustomFieldOption> findByFieldId(Integer fieldId);

}
