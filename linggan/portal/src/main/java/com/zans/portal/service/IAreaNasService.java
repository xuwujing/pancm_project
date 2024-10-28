package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.AreaNas;
import com.zans.portal.vo.area.AreaNasVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author xv
 * @since 2020/4/17 12:00
 */
public interface IAreaNasService extends BaseService<AreaNas> {

    AreaNasVO findAreaNasByMac(@Param("mac") String mac);
}
