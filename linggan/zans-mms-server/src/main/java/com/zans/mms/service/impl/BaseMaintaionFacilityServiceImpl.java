package com.zans.mms.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.BaseMaintaionFacilityMapper;
import com.zans.mms.model.BaseMaintaionFacility;
import com.zans.mms.service.IBaseMaintaionFacilityService;
import com.zans.mms.vo.basemaintaionfacility.BaseMaintaionFacilityTicketReqVO;
import com.zans.mms.vo.basemaintaionfacility.BaseMaintaionFacilityQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *  BaseMaintaionFacilityServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("baseMaintaionFacilityService")
public class BaseMaintaionFacilityServiceImpl extends BaseServiceImpl<BaseMaintaionFacility> implements IBaseMaintaionFacilityService {


	@Autowired
	private BaseMaintaionFacilityMapper baseMaintaionFacilityMapper;

	@Resource
	public void setBaseMaintaionFacilityMapper(BaseMaintaionFacilityMapper mapper) {
		super.setBaseMapper(mapper);
		this.baseMaintaionFacilityMapper = mapper;
	}

	@Override
	public ApiResult getList(BaseMaintaionFacilityQueryVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);

		List<BaseMaintaionFacility> result = baseMaintaionFacilityMapper.getList(vo);

		return ApiResult.success(new PageResult<BaseMaintaionFacility>(page.getTotal(), result, pageNum, pageSize));

	}

	@Override
	public ApiResult getTicketList(BaseMaintaionFacilityTicketReqVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);

		List<BaseMaintaionFacility> result = baseMaintaionFacilityMapper.getTicketList(vo);

		return ApiResult.success(new PageResult<BaseMaintaionFacility>(page.getTotal(), result, pageNum, pageSize));
	}
}
