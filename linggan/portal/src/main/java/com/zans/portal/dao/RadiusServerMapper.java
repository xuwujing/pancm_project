package com.zans.portal.dao;

import com.zans.portal.model.RadiusServer;
import com.zans.portal.vo.radius.ServerRespVO;
import com.zans.portal.vo.radius.ServerSearchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RadiusServerMapper extends Mapper<RadiusServer> {

    List<ServerRespVO> getRadiusServerPage(@Param("reqVo") ServerSearchVO reqVo);

}
