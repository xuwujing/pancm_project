package com.zans.portal.dao;

import com.zans.portal.model.RadiusNas;
import com.zans.portal.vo.radius.RadiusNasRespVO;
import com.zans.portal.vo.radius.RadiusNasSearchVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RadiusNasMapper extends Mapper<RadiusNas> {

    RadiusNas getByNasIpAndSwIP(@Param("nasIp") String nasIp, @Param("swIp") String swIp);


    List<RadiusNasRespVO> findRadiusNasList(@Param("reqVo") RadiusNasSearchVO reqVO);

    RadiusNas findByNameOrNasIp(@Param("name") String name,@Param("nasIp") String nasIp,@Param("id") Integer id);

}
