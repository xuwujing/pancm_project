package com.zans.mms.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.exception.RollbackException;
import com.zans.base.office.excel.*;
import com.zans.base.office.validator.ValidateHelper;
import com.zans.base.office.validator.ValidateResult;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;

import com.zans.mms.dao.guard.IpAllMapper;
import com.zans.mms.model.IpAll;
import com.zans.mms.service.IIpService;
import com.zans.mms.vo.radius.QzViewRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_ERROR;


@Service
@Slf4j
public class IpServiceImpl extends BaseServiceImpl<IpAll> implements IIpService {

    IpAllMapper ipMapper;





    @Resource
    public void setIpMapper(IpAllMapper ipMapper) {
        super.setBaseMapper(ipMapper);
        this.ipMapper = ipMapper;
    }

    @Override
    public QzViewRespVO findByIp(String ip) {
        return ipMapper.findByIp(ip);
    }




}
