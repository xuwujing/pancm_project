package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.DeviceType;
import com.zans.base.vo.SelectVO;
import com.zans.portal.vo.common.TreeSelect;
import com.zans.portal.vo.device.DeviceTypeResVO;
import com.zans.portal.vo.device.DeviceTypeSearchVO;

import java.util.List;
import java.util.Map;

public interface IDeviceTypeService extends BaseService<DeviceType> {

    List<SelectVO> findDeviceTypeToSelect();
    List<SelectVO> findMmsDeviceTypeToSelect();

    Map<Object, String> findDeviceTypeToMap();

    String getNameByType(String type);

    /**
     * 返回模板路径不为空的的设备类型，用于填表
     * @return
     */
    List<SelectVO> findDeviceTypeHasTemplateToSelect();

    String getSupportDeviceType();

    Integer findTypeByName(String name);

    PageResult<DeviceTypeResVO> getDeviceTypePage(DeviceTypeSearchVO reqVO);

    List<TreeSelect> deviceTypeTreeList();
}
