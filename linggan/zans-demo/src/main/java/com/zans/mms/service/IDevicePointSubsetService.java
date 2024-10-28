package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.DevicePointSubset;
import com.zans.mms.vo.devicepoint.subset.DevicePointSubsetQueryVO;

import java.util.List;

/**
 * interface DevicePointSubsetservice
 *
 * @author
 */
public interface IDevicePointSubsetService extends BaseService<DevicePointSubset>{


    ApiResult getList(DevicePointSubsetQueryVO vo);

    Boolean existRelation(Long subsetId);

    int deleteById(Long subsetId);

    List<SelectVO> getSelectList();

}
