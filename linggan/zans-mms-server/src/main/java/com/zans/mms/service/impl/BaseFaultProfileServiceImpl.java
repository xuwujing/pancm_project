package com.zans.mms.service.impl;


import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.mms.dao.BaseFaultProfileMapper;
import com.zans.mms.model.BaseFaultProfile;
import com.zans.mms.service.IBaseFaultProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  BaseFaultProfileServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("baseFaultProfileService")
public class BaseFaultProfileServiceImpl extends BaseServiceImpl<BaseFaultProfile> implements IBaseFaultProfileService {
		
		
	@Autowired
	private BaseFaultProfileMapper baseFaultProfileMapper;

	@Resource
	public void setBaseFaultProfileMapper(BaseFaultProfileMapper mapper) {
		super.setBaseMapper(mapper);
		this.baseFaultProfileMapper = mapper;
	}

}