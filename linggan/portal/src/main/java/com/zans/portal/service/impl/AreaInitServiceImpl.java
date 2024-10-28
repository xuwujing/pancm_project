package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.TAreaInitMapper;
import com.zans.portal.model.TAreaInit;
import com.zans.portal.service.IAreaInitService;
import com.zans.portal.vo.area.AreaInitResVO;
import com.zans.portal.vo.area.AreaInitSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yhj
 */
@Service
@Slf4j
public class AreaInitServiceImpl extends BaseServiceImpl<TAreaInit> implements IAreaInitService {

    TAreaInitMapper areaInitMapper;

    @Resource
    public void setAreaInitMapper(TAreaInitMapper areaInitMapper) {
        super.setBaseMapper(areaInitMapper);
        this.areaInitMapper = areaInitMapper;
    }

    @Override
    public PageResult<AreaInitResVO> getAreaInitPage(AreaInitSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<AreaInitResVO> list = areaInitMapper.findAreaInitList(reqVO);
        return new PageResult<AreaInitResVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public TAreaInit getByAreaIdAndDeviceTypeId(Integer areaId, Integer deviceTypeId, Integer id) {
        return areaInitMapper.getByAreaIdAndDeviceTypeId(areaId, deviceTypeId, id);
    }

    @Override
    public Integer getCountByAreaId(Integer areaId) {
        return areaInitMapper.getCountByAreaId(areaId);
    }

    @Override
    public Integer getCountByDeviceTypeId(Integer deviceTypeId) {
        return areaInitMapper.getCountByDeviceTypeId(deviceTypeId);
    }


}
