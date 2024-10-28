package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.DeviceType;
import com.zans.mms.vo.device.DeviceTypeResVO;
import com.zans.mms.vo.device.DeviceTypeSearchVO;

import java.util.List;
import java.util.Map;

public interface IDeviceTypeService extends BaseService<DeviceType> {

	List<SelectVO> findDeviceTypeToSelect();

	Map<Object, String> findDeviceTypeToMap();

}
