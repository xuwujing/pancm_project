package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.SysConstant;


public interface ISysConstantService extends BaseService<SysConstant> {

	SysConstant findConstantByKey(String key);

	String getRadApi();

	String getEdgeAccessMode();

}
