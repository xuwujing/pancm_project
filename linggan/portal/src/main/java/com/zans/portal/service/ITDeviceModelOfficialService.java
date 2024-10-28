package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.TDeviceModelOfficial;
import com.zans.portal.vo.device.DeviceResponseVO;
import com.zans.portal.vo.device.DeviceSearchVO;
import com.zans.portal.vo.device.ExcelUnknownDeviceVO;

import java.util.List;

public interface ITDeviceModelOfficialService extends BaseService<TDeviceModelOfficial> {

    PageResult<DeviceResponseVO> getOfficialPage(DeviceSearchVO reqVO);

    /**
     * 设备模型存在但是设备类型未知或为摄像头问题
     * @return
     */
    List<ExcelUnknownDeviceVO> getUnknownDevicePage();

    /**
     * 批量修正未知设备
     * @param filePath
     * @param fileName
     * @param userSession
     * @return
     */
    ApiResult batchAddDevice(String filePath, String fileName, UserSession userSession);

    int findOffcialCountByCode(String modelCode, Integer id);

}
