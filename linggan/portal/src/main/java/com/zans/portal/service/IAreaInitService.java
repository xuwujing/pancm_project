package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.TAreaInit;
import com.zans.portal.vo.area.AreaInitResVO;
import com.zans.portal.vo.area.AreaInitSearchVO;

/**
 * @author yhj
 *
 */
public interface IAreaInitService extends BaseService<TAreaInit> {

    PageResult<AreaInitResVO> getAreaInitPage(AreaInitSearchVO reqVO);

    TAreaInit getByAreaIdAndDeviceTypeId(Integer areaId, Integer deviceTypeId, Integer id);

    Integer getCountByAreaId(Integer areaId);

    Integer getCountByDeviceTypeId(Integer deviceTypeId);

}
