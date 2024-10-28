package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.NetworkSwitcherMac;
import com.zans.portal.vo.switcher.SwitcherMacReqVO;
import com.zans.portal.vo.switcher.SwitcherMacRespVO;

/**
 * @author yhj
 *
 */
public interface INetworkSwitcherMacService extends BaseService<NetworkSwitcherMac> {

    PageResult<SwitcherMacRespVO> getSwitcherMacPage(SwitcherMacReqVO reqVO);

}
