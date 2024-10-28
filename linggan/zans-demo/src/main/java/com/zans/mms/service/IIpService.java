package com.zans.mms.service;



import com.zans.base.service.BaseService;
import com.zans.mms.model.IpAll;
import com.zans.mms.vo.radius.QzViewRespVO;

import java.util.List;

public interface IIpService extends BaseService<IpAll> {

	QzViewRespVO findByIp(String ip);
}
