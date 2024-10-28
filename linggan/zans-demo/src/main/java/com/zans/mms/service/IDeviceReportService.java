package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.DeviceReport;
import com.zans.mms.vo.devicereport.DeviceReportEditReqVO;
import com.zans.mms.vo.devicereport.DeviceReportSaveReqVO;
import com.zans.mms.vo.devicereport.DeviceReportSearchReqVO;

/**
 * 设备上报(DeviceReport)表服务接口
 *
 * @author beixing
 * @since 2021-03-08 17:21:39
 */
public interface IDeviceReportService {



    ApiResult appList(DeviceReportSearchReqVO deviceReport, UserSession userSession);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceReport queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceReport 实例对象
     * @param userSession
     * @return 对象列表
     */
    ApiResult list(DeviceReportSearchReqVO deviceReport, UserSession userSession);


    /**
     * 新增数据
     *
     * @param deviceReport 实例对象
     * @return 实例对象
     */
    ApiResult save(DeviceReportSaveReqVO deviceReport);

    /**
     * 修改数据
     *
     * @param deviceReport 实例对象
     * @return 实例对象
     */
    ApiResult update(DeviceReportEditReqVO deviceReport);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
