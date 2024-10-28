package com.zans.mms.service;

import java.util.List;

import com.zans.base.service.BaseService;
import com.zans.mms.model.SysSwitcherVlanConfig;
import com.zans.mms.vo.switcher.SwitcherVlanConfigRespVO;


public interface ISysSwitcherVlanConfigService extends BaseService<SysSwitcherVlanConfig> {

	/**
	 * 根据ip查找所对应的交换机配置的VLAN信息
	 * @param ip
	 * @return
	 */
	List<SwitcherVlanConfigRespVO> getIpMatchVlanList(String ip);
}
