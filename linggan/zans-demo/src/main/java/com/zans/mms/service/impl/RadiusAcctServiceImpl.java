package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.PageResult;

import com.zans.mms.dao.guard.RadiusAcctMapper;
import com.zans.mms.model.RadiusAcct;
import com.zans.mms.service.IRadiusAcctService;
import com.zans.mms.vo.radius.AcctRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yhj
 */
@Service
@Slf4j
public class RadiusAcctServiceImpl extends BaseServiceImpl<RadiusAcct> implements IRadiusAcctService {

    RadiusAcctMapper mapper;

    @Resource
    public void setMapper(RadiusAcctMapper mapper) {
        super.setBaseMapper(mapper);
        this.mapper = mapper;
    }

    @Override
    public AcctRespVO findLatestAcctByMac(String mac) {
        String macMin = StringHelper.convertMacToMin(mac);
        return mapper.findLatestAcctByMac(macMin);
    }

}
