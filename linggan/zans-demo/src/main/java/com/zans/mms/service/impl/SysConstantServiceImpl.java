package com.zans.mms.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.mms.dao.guard.SysConstantMapper;
import com.zans.mms.model.SysConstant;
import com.zans.mms.service.ISysConstantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.zans.base.config.GlobalConstants.ACCESS_ACCESS_MODE;
import static com.zans.base.config.GlobalConstants.RAD_API_HOST;


@Service
@Slf4j
public class SysConstantServiceImpl extends BaseServiceImpl<SysConstant> implements ISysConstantService {

    SysConstantMapper constantMapper;

    @Resource
    public void setConstantMapper(SysConstantMapper constantMapper) {
        super.setBaseMapper(constantMapper);
        this.constantMapper = constantMapper;
    }

    @Override
    public String getRadApi() {
        String host = null;
        SysConstant radData = this.findConstantByKey(RAD_API_HOST);
        if (radData != null) {
            host = radData.getConstantValue();
        }
        if (host == null || !host.startsWith("http")) {
            log.error("系统常量rad_api为空");
            return null;
        } else {
            return host;
        }
    }

    @Override
    public String getEdgeAccessMode() {
        String host = null;
        SysConstant radData = this.findConstantByKey(ACCESS_ACCESS_MODE);
        if (radData != null) {
            return radData.getConstantValue();
        } else {
            return "radius";
        }
    }


    @Override
    public SysConstant findConstantByKey(String key) {
        return constantMapper.findConstantByKey(key);
    }


}
