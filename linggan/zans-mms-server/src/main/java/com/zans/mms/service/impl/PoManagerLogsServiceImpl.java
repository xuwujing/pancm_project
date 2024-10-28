package com.zans.mms.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.mms.dao.PoManagerLogsMapper;
import com.zans.mms.model.Asset;
import com.zans.mms.model.PoManagerLogs;
import com.zans.mms.service.IPoManagerLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/4
 */
@Service("poManagerLogsService")
@Slf4j
public class PoManagerLogsServiceImpl extends BaseServiceImpl<PoManagerLogs>  implements IPoManagerLogsService {

	@Resource
	PoManagerLogsMapper poManagerLogsMapper;

	@Override
	public void insertOne(PoManagerLogs poManagerLogs) {
		poManagerLogsMapper.insertSelective(poManagerLogs);
	}
}
