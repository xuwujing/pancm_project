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



}
