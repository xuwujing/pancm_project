package com.zans.portal.service.impl;



import com.zans.portal.dao.AliveHeartbeatMapper;
import com.zans.portal.model.AliveHeartbeat;
import com.zans.portal.service.IAliveHeartbeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.zans.base.service.impl.BaseServiceImpl;

import javax.annotation.Resource;
/**
 *  AliveHeartbeatServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("aliveHeartbeatService")
public class AliveHeartbeatServiceImpl extends BaseServiceImpl<AliveHeartbeat> implements IAliveHeartbeatService {
		
		
	@Resource
	private AliveHeartbeatMapper aliveHeartbeatMapper;


}