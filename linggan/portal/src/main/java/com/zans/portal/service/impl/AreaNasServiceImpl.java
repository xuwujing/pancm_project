package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.AreaNasMapper;
import com.zans.portal.model.AreaNas;
import com.zans.portal.service.IAreaNasService;
import com.zans.portal.vo.area.AreaNasVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xv
 * @since 2020/4/17 12:02
 */
@Service
@Slf4j
public class AreaNasServiceImpl extends BaseServiceImpl<AreaNas> implements IAreaNasService {

    AreaNasMapper nasMapper;

    @Resource
    public void setNasMapper(AreaNasMapper nasMapper) {
        super.setBaseMapper(nasMapper);
        this.nasMapper = nasMapper;
    }

    @Override
    public AreaNasVO findAreaNasByMac(String mac) {
        return nasMapper.findAreaNasByMac(mac);
    }
}
