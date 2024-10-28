package com.zans.mms.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.BaseDeviceTypeMapper;
import com.zans.mms.model.BaseDeviceType;
import com.zans.mms.service.IBaseDeviceTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  BaseDeviceTypeServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("baseDeviceTypeService")
public class BaseDeviceTypeServiceImpl extends BaseServiceImpl<BaseDeviceType> implements IBaseDeviceTypeService {
		
		
	@Autowired
	private BaseDeviceTypeMapper baseDeviceTypeMapper;

	@Resource
	public void setBaseDeviceTypeMapper(BaseDeviceTypeMapper mapper) {
		super.setBaseMapper(mapper);
		this.baseDeviceTypeMapper = mapper;
	}

	@Override
	@Cacheable(cacheNames = "DEVICE_TYPE_LIST")
	public List<SelectVO> findDeviceTypeToSelect() {
		return baseDeviceTypeMapper.findDeviceTypeToSelect();
	}

	@Override
	@Cacheable(cacheNames = "DEVICE_TYPE_MAP")
	public Map<Object, String> findDeviceTypeToMap() {
		List<SelectVO> selectList = this.findDeviceTypeToSelect();
		return selectList.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
	}

	@Override
	public String getTypeIdByName(String name) {
		return baseDeviceTypeMapper.getTypeIdByName(name);
	}
}