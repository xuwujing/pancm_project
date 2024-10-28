package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.NetworkSwitcherInterface;
import com.zans.portal.vo.switcher.SwitcherInterfaceReqVO;
import com.zans.portal.vo.switcher.SwitcherInterfaceRespVO;

import java.util.List;

/**
 * @author yhj
 *
 */
public interface INetworkSwitcherInterfaceService extends BaseService<NetworkSwitcherInterface> {

    List<SelectVO> getInterfaceBySwId(String swId);

    PageResult<SwitcherInterfaceRespVO> getSwitcherInterfacePage(SwitcherInterfaceReqVO reqVO);

}
