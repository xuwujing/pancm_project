package com.zans.portal.service;

import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceDisposeVO;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceResVO;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceSearchVO;

import java.util.Date;

public interface IAlertOfflineDeviceService {

    PageResult<OfflineDeviceResVO> getDevicePage(OfflineDeviceSearchVO reqVO);

    Integer countOfflineDeviceByTime( Date aliveLastTime,boolean isLessThan);

    @Deprecated
    int dispose(OfflineDeviceDisposeVO disposeVO, UserSession userSession);

}
