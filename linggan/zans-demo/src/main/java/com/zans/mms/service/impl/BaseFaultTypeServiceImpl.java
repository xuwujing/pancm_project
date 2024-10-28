package com.zans.mms.service.impl;


import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.mms.BaseFaultTypeMapper;
import com.zans.mms.model.BaseFaultType;
import com.zans.mms.service.IBaseFaultTypeService;
import com.zans.mms.vo.base.BaseFaultTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  BaseFaultypeServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("baseFaultTypeService")
public class BaseFaultTypeServiceImpl extends BaseServiceImpl<BaseFaultType> implements IBaseFaultTypeService {


	@Autowired
	private BaseFaultTypeMapper baseFaultTypeMapper;

	@Resource
	public void setBaseFaultTypeMapper(BaseFaultTypeMapper mapper) {
		super.setBaseMapper(mapper);
		this.baseFaultTypeMapper = mapper;
	}

	@Override
	public Map<String,List<SelectVO>> listFaultTypeView() {
		List<BaseFaultTypeVO> typeVOS = baseFaultTypeMapper.listFaultTypeView();
		Map<String, List<SelectVO>> faultMap = new HashMap();
		for (BaseFaultTypeVO vo : typeVOS) {
			String deviceType = vo.getDeviceType();
			List list = faultMap.get(deviceType);
			if (list == null) {
				list = new ArrayList();
				faultMap.put(deviceType, list);
			}
			SelectVO select = new SelectVO();
			select.setItemKey(vo.getFaultId());
			select.setItemValue(vo.getFaultName());
			list.add(select);
		}
		return faultMap;
	}

	@Override
	public List<SelectVO> faultList() {
		return baseFaultTypeMapper.faultList();
	}
}
