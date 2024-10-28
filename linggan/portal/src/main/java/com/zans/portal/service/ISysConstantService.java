package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.SysConstant;
import com.zans.portal.vo.constant.ConstantReqVO;
import com.zans.portal.vo.constant.ConstantSearchVO;

public interface ISysConstantService extends BaseService<SysConstant> {

    SelectVO findSelectVOByKey(String key);

    SysConstant findConstantByKey(String key);

    String getRadApi();

    String getAlertApi();

    String getEdgeAccessMode();

    PageResult<ConstantReqVO> getConstantPage(ConstantSearchVO reqVO);

	String getJudgeApi();
}
