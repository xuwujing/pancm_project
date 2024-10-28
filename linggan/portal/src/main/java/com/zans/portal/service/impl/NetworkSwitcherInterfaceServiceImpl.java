package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.dao.NetworkSwitcherInterfaceMapper;
import com.zans.portal.model.NetworkSwitcherInterface;
import com.zans.portal.service.INetworkSwitcherInterfaceService;
import com.zans.portal.vo.switcher.SwitcherInterfaceReqVO;
import com.zans.portal.vo.switcher.SwitcherInterfaceRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yhj
 */
@Service
@Slf4j
public class NetworkSwitcherInterfaceServiceImpl extends BaseServiceImpl<NetworkSwitcherInterface> implements INetworkSwitcherInterfaceService {

    NetworkSwitcherInterfaceMapper interfaceMapper;

    @Resource
    public void setInterfaceMapper(NetworkSwitcherInterfaceMapper interfaceMapper) {
        super.setBaseMapper(interfaceMapper);
        this.interfaceMapper = interfaceMapper;
    }

    @Override
    public PageResult<SwitcherInterfaceRespVO> getSwitcherInterfacePage(SwitcherInterfaceReqVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<SwitcherInterfaceRespVO> list = interfaceMapper.getSwitcherInterfacePage(reqVO);
        return new PageResult<SwitcherInterfaceRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public List<SelectVO> getInterfaceBySwId(String swId) {
        return interfaceMapper.getInterfaceBySwId(swId);
    }

}
