package com.zans.mms.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.mms.BaseOrgMapper;
import com.zans.mms.model.BaseOrg;
import com.zans.mms.service.IBaseOrgService;
import com.zans.mms.vo.baseorg.BaseOrgRepVO;
import com.zans.mms.vo.baseorg.BaseOrgReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *  BaseOrgServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("baseOrgService")
public class BaseOrgServiceImpl extends BaseServiceImpl<BaseOrg> implements IBaseOrgService {


	@Autowired
	private BaseOrgMapper baseOrgMapper;

    @Resource
    public void setBaseOrgMapper(BaseOrgMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.baseOrgMapper = baseMapper;
    }

	@Override
	public List<SelectVO> orgList() {
		return baseOrgMapper.orgList();
	}

	@Override
	public List<SelectVO> orgTypeList() {
		return baseOrgMapper.orgTypeList();
	}

	@Override
	public List<SelectVO> queryBaseOrg() {
		return baseOrgMapper.queryBaseOrg();
	}

	@Override
	public ApiResult queryList(BaseOrgReqVO baseOrgRepVO) {
		int pageNum = baseOrgRepVO.getPageNum();
		int pageSize = baseOrgRepVO.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<BaseOrgRepVO> result = baseOrgMapper.queryAll(baseOrgRepVO);
		return ApiResult.success(new PageResult<>(page.getTotal(), result, pageSize, pageNum));
	}














































	@Override
	public BaseOrgRepVO queryByOrgId(String orgId) {
		return baseOrgMapper.queryByOrgId(orgId);
	}
}
