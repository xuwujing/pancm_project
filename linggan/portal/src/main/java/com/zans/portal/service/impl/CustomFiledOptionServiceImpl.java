package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.SysCustomFieldOptionMapper;
import com.zans.portal.model.SysCustomFieldOption;
import com.zans.portal.service.ICustomFieldOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yhj
 */
@Service
@Slf4j
public class CustomFiledOptionServiceImpl extends BaseServiceImpl<SysCustomFieldOption> implements ICustomFieldOptionService {

    SysCustomFieldOptionMapper customFieldOptionMapper;

    @Resource
    public void setCustomFieldOptionMapper(SysCustomFieldOptionMapper customFieldOptionMapper) {
        super.setBaseMapper(customFieldOptionMapper);
        this.customFieldOptionMapper = customFieldOptionMapper;
    }

    @Override
    public List<SysCustomFieldOption> findByFieldId(Integer fieldId) {
        return customFieldOptionMapper.findByFieldId(fieldId);
    }


}
