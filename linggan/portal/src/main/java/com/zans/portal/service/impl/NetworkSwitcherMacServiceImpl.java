package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.NetworkSwitcherMacMapper;
import com.zans.portal.model.NetworkSwitcherMac;
import com.zans.portal.service.INetworkSwitcherMacService;
import com.zans.portal.vo.switcher.SwitcherMacReqVO;
import com.zans.portal.vo.switcher.SwitcherMacRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yhj
 */
@Service
@Slf4j
public class NetworkSwitcherMacServiceImpl extends BaseServiceImpl<NetworkSwitcherMac> implements INetworkSwitcherMacService {

    NetworkSwitcherMacMapper switcherMacMapper;

    @Resource
    public void setSwitcherMacMapper(NetworkSwitcherMacMapper switcherMacMapper) {
        super.setBaseMapper(switcherMacMapper);
        this.switcherMacMapper = switcherMacMapper;
    }

    @Override
    public PageResult<SwitcherMacRespVO> getSwitcherMacPage(SwitcherMacReqVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<SwitcherMacRespVO> list = switcherMacMapper.findSwitcherMacList(reqVO);
        return new PageResult<SwitcherMacRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }


}
