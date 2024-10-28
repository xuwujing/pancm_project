package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.mms.model.RadiusAcct;
import com.zans.mms.vo.radius.AcctReqVO;
import com.zans.mms.vo.radius.AcctRespVO;

/**
 * @author yhj
 *
 */
public interface IRadiusAcctService extends BaseService<RadiusAcct> {

	AcctRespVO findLatestAcctByMac(String mac);

}
