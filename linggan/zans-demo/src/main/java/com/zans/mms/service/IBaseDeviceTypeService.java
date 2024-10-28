package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.BaseDeviceType;

import java.util.List;
import java.util.Map;

/**
 * interface BaseDeviceTypeservice
 *
 * @author
 */
public interface IBaseDeviceTypeService extends BaseService<BaseDeviceType>{

    List<SelectVO> findDeviceTypeToSelect();

    Map<Object, String> findDeviceTypeToMap();

    String getTypeIdByName(String name);
}
