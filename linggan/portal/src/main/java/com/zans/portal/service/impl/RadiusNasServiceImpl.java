package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.RadiusNasMapper;
import com.zans.portal.model.RadiusNas;
import com.zans.portal.service.IRadiusNasService;
import com.zans.portal.vo.radius.RadiusNasRespVO;
import com.zans.portal.vo.radius.RadiusNasSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xv
 * @since 2020/4/17 12:02
 */
@Service
@Slf4j
public class RadiusNasServiceImpl extends BaseServiceImpl<RadiusNas> implements IRadiusNasService {

    RadiusNasMapper nasMapper;

    @Resource
    public void setNasMapper(RadiusNasMapper nasMapper) {
        super.setBaseMapper(nasMapper);
        this.nasMapper = nasMapper;
    }

    @Override
    public RadiusNas getByNasIpAndSwIP(String nasIp, String swIp) {
        return nasMapper.getByNasIpAndSwIP(nasIp, swIp);
    }

    @Override
    public PageResult<RadiusNasRespVO> getRadiusNasPage(RadiusNasSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<RadiusNasRespVO> list = nasMapper.findRadiusNasList(reqVO);
        return new PageResult<RadiusNasRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public RadiusNas getByNameOrNasIp(String name, String nasIp, Integer id) {
        return nasMapper.findByNameOrNasIp(name, nasIp, id);
    }

}
