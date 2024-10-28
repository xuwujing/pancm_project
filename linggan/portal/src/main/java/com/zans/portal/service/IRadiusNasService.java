package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.RadiusNas;
import com.zans.portal.vo.radius.RadiusNasRespVO;
import com.zans.portal.vo.radius.RadiusNasSearchVO;

public interface IRadiusNasService extends BaseService<RadiusNas> {

    RadiusNas getByNasIpAndSwIP(String nasIp, String swIp);

    PageResult<RadiusNasRespVO> getRadiusNasPage(RadiusNasSearchVO reqVO);

    RadiusNas getByNameOrNasIp(String name, String nasIp, Integer id);

}
