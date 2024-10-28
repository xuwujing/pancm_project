package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.vo.devicepoint.DevicePointAddReqVO;
import com.zans.mms.vo.devicepoint.DevicePointDetailResVO;
import com.zans.mms.vo.devicepoint.DevicePointQueryVO;

/**
 * interface DevicePointservice
 *
 * @author
 */
public interface IDevicePointService extends BaseService<DevicePoint>{


    ApiResult getList(DevicePointQueryVO vo);

    DevicePointDetailResVO getViewById(Long id);

    Boolean existRelation(Long id);

    int deleteByUniqueId(Long id);

    Integer getIdByUniqueId(String pointId);

    ApiResult batchAddDevicePoint(String newFileName, String originName, UserSession userSession);

    void updateDevicePoint(DevicePoint devicePoint);


   int insertDevicePoint(DevicePointAddReqVO devicePointAddReqVO);

}
