package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.guard.DeviceTypeMapper;
import com.zans.mms.model.DeviceType;
import com.zans.mms.service.IDeviceTypeService;
import com.zans.mms.vo.device.DeviceTypeResVO;
import com.zans.mms.vo.device.DeviceTypeSearchVO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zans.base.config.BaseConstants.SEPARATOR_SPACE;

/**
 * @author xv
 */
@Service
public class DeviceTypeServiceImpl extends BaseServiceImpl<DeviceType> implements IDeviceTypeService {

    DeviceTypeMapper deviceTypeMapper;

    @Resource
    public void setDeviceTypeMapper(DeviceTypeMapper deviceTypeMapper) {
        super.setBaseMapper(deviceTypeMapper);
        this.deviceTypeMapper = deviceTypeMapper;
    }

    @Override
    @Cacheable(cacheNames = "DEVICE_TYPE_LIST")
    public List<SelectVO> findDeviceTypeToSelect() {
        return deviceTypeMapper.findDeviceTypeToSelect();
    }

    @Override
    @Cacheable(cacheNames = "DEVICE_TYPE_MAP")
    public Map<Object, String> findDeviceTypeToMap() {
        List<SelectVO> selectList = this.findDeviceTypeToSelect();
        return selectList.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
    }


}
