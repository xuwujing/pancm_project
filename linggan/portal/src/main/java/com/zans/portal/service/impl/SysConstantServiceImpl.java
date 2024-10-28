package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.dao.SysConstantMapper;
import com.zans.portal.model.SysConstant;
import com.zans.portal.service.ISysConstantService;
import com.zans.portal.vo.constant.ConstantReqVO;
import com.zans.portal.vo.constant.ConstantSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.zans.portal.config.GlobalConstants.*;


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
    public SelectVO findSelectVOByKey(String key) {
        return constantMapper.findSelectVOByKey(key);
    }

    @Override
    public SysConstant findConstantByKey(String key) {
        return constantMapper.findConstantByKey(key);
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
    public String getAlertApi() {
        String host = null;
        SysConstant radData = this.findConstantByKey(ALERT_API_HOST);
        if (radData != null) {
            host = radData.getConstantValue();
        }
        if (host == null || !host.startsWith("http")) {
            log.error("系统常量alert_api为空");
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
    public PageResult<ConstantReqVO> getConstantPage(ConstantSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<ConstantReqVO> list = constantMapper.findConstantList(reqVO);
        return new PageResult<ConstantReqVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public String getJudgeApi() {
        String host = null;
        SysConstant radData = this.findConstantByKey(JUDGE_API_HOST);
        if (radData != null) {
            host = radData.getConstantValue();
        }
        if (host == null || !host.startsWith("http")) {
            log.error("系统常量alert_api为空");
            return null;
        } else {
            return host;
        }
    }
}
