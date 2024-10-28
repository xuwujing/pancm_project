package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.mms.model.DevicePointCheckLog;
import com.zans.mms.vo.devicepoint.check.DevicePointCheckReqVO;

/**
* @Title: IDevicePointCheckLogService
* @Description: 点位表校正日志Service
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/12/21
*/
public interface IDevicePointCheckLogService extends BaseService<DevicePointCheckLog>{



    /**
    * @Author beiming
    * @Description  校正点位
    * @Date  4/12/21
    * @Param DevicePointCheckReqVO
    * @return boolean
    **/
    boolean checkPoint(DevicePointCheckReqVO reqVO);
}
