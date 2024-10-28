package com.zans.mms.service.impl;


import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.mms.dao.BaseVehicleMapper;
import com.zans.mms.model.BaseVehicle;
import com.zans.mms.service.IBaseVehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  BaseVehicleServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("baseVehicleService")
public class BaseVehicleServiceImpl extends BaseServiceImpl<BaseVehicle> implements IBaseVehicleService {
		
		
	@Autowired
	private BaseVehicleMapper baseVehicleMapper;

	@Resource
	public void setBaseVehicleMapper(BaseVehicleMapper mapper) {
		super.setBaseMapper(mapper);
		this.baseVehicleMapper = mapper;
	}
}