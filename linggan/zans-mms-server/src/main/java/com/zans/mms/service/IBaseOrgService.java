package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.BaseOrg;
import com.zans.mms.vo.baseorg.BaseOrgRepVO;
import com.zans.mms.vo.baseorg.BaseOrgReqVO;

import java.util.List;

/**
 * interface BaseOrgservice
 *
 * @author
 */
public interface IBaseOrgService extends BaseService<BaseOrg>{


     List<SelectVO> orgList();

     List<SelectVO> orgTypeList();

     List<SelectVO> queryBaseOrg();


     ApiResult queryList(BaseOrgReqVO baseOrgRepVO);

     BaseOrgRepVO queryByOrgId(String orgId);


	String getOrgIdByOrgName(String departmentName);

	/**
	 * 根据部门名称列表查询部门id列表
	 * @param deptNameList
	 * @return
	 */
	List<String> getIdByName(List<String> deptNameList);

	List<SelectVO> queryMapOrg();

	List<BaseOrg> queryBaseOrgInfo();

	String queryDutyInfo(String allocDepartmentNum);

	String queryContactInfo(String allocDepartmentNum);

	String getOrgType(String orgId);
}
