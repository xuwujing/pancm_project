package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.AreaNas;
import com.zans.portal.model.RadiusServer;
import com.zans.portal.vo.area.AreaNasVO;
import com.zans.portal.vo.radius.ServerRespVO;
import com.zans.portal.vo.radius.ServerSearchVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author yanghanjin
 * @since 2020/6/6 12:00
 */
public interface IRadiusServerService extends BaseService<RadiusServer> {

    PageResult<ServerRespVO> getRadiusServerPage(ServerSearchVO reqVO);


}
