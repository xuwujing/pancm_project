package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.SysCustomDataMapper;
import com.zans.portal.model.SysCustomData;
import com.zans.portal.service.ICustomDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yhj
 */
@Service
@Slf4j
public class CustomDataServiceImpl extends BaseServiceImpl<SysCustomData> implements ICustomDataService {

    SysCustomDataMapper customDataMapper;

    @Resource
    public void setCustomFiledMapper(SysCustomDataMapper customDataMapper) {
        super.setBaseMapper(customDataMapper);
        this.customDataMapper = customDataMapper;
    }


    @Override
    public Integer findCountByFieldId(Integer fieldId) {
        return customDataMapper.findCountByFieldId(fieldId);
    }


}
