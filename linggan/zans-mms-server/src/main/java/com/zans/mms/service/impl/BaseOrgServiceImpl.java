package com.zans.mms.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.BaseOrgMapper;
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

	/**
	 * 通过部门名称查询部门id
	 * @param departmentName 部门名称
	 * @return
	 */
	@Override
	public String getOrgIdByOrgName(String departmentName) {
		return baseOrgMapper.getOrgIdByOrgName(departmentName);
	}


	/**
	 * 根据部门名称查询部门id列表
	 * @param deptNameList 部门名称列表
	 * @return
	 */
	@Override
	public List<String> getIdByName(List<String> deptNameList) {
		return baseOrgMapper.getIdByName(deptNameList);
	}

	@Override
	public List<SelectVO> queryMapOrg() {
		return baseOrgMapper.queryMapOrg();
	}

	@Override
	public List<BaseOrg> queryBaseOrgInfo() {
		return baseOrgMapper.queryBaseOrgInfo();
	}

	@Override
	public String queryDutyInfo(String allocDepartmentNum) {
		return baseOrgMapper.queryDutyInfo(allocDepartmentNum);
	}

	@Override
	public String queryContactInfo(String allocDepartmentNum) {
		return  baseOrgMapper.queryContactInfo(allocDepartmentNum);
	}

	@Override
	public String getOrgType(String orgId) {
		return baseOrgMapper.getOrgType(orgId);
	}
}
