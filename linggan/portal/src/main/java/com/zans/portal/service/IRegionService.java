package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.SysRegion;
import com.zans.portal.vo.common.TreeSelect;

import java.util.List;

/**
 * @author yhj
 *
 */
public interface IRegionService extends BaseService<SysRegion> {

    List<TreeSelect> getRegionTree(String regionName);

    int getByRegionName(String regionName, Integer regionId);

    String getRegionNameById(Integer id);


}
