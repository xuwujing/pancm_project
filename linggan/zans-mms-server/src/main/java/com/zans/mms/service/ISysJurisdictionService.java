package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.SysJurisdiction;
import com.zans.mms.vo.jurisdiction.SysJurisdictionReqVO;

import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:系统权限配置逻辑层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/18
 */
public interface ISysJurisdictionService {

	ApiResult list(SysJurisdictionReqVO vo);

	ApiResult add(SysJurisdiction sysJurisdiction);

	ApiResult update(SysJurisdiction sysJurisdiction);

	List<SelectVO> selectList();

	ApiResult cloneSysJurisdiction(SysJurisdictionReqVO vo);
}
