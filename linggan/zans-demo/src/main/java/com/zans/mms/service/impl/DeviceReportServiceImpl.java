package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.dao.mms.BaseVfsDao;
import com.zans.mms.dao.mms.DeviceReportDao;
import com.zans.mms.model.BaseVfs;
import com.zans.mms.model.DeviceReport;
import com.zans.mms.service.IDataPermService;
import com.zans.mms.service.IDeviceReportService;
import com.zans.mms.vo.devicereport.DeviceReportEditReqVO;
import com.zans.mms.vo.devicereport.DeviceReportSaveReqVO;
import com.zans.mms.vo.devicereport.DeviceReportSearchReqVO;
import com.zans.mms.vo.devicereport.DeviceReportSearchRespVO;
import com.zans.mms.vo.perm.DataPermVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.zans.mms.config.MMSConstants.MSG_FORMAT_GIS;


/**
 * 设备上报(DeviceReport)表服务实现类
 *
 * @author beixing
 * @since 2021-03-08 17:21:39
 */
@Service("deviceReportService")
public class DeviceReportServiceImpl implements IDeviceReportService {
    @Resource
    private DeviceReportDao deviceReportDao;

    @Resource
    private BaseVfsDao baseVfsDao;

    @Autowired
    private IDataPermService dataPermService;



    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DeviceReport queryById(Long id) {
        return this.deviceReportDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(DeviceReportSearchReqVO deviceReport, UserSession userSession) {
        DataPermVO dataPerm = dataPermService.getPcDeviceReportPerm(userSession);
        if(dataPerm == null){
            return ApiResult.error("该角色无权限或未配置！");
        }
        deviceReport.setDataPerm(dataPerm.getDataPerm());
        deviceReport.setCreator(userSession.getUserName());
        int pageNum = deviceReport.getPageNum();
        int pageSize = deviceReport.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<DeviceReportSearchRespVO> result = deviceReportDao.queryAll(deviceReport);
        for (DeviceReportSearchRespVO report : result) {
            List<BaseVfs> baseVfs = baseVfsDao.queryByAdjunctId(report.getAdjunctId());
            report.setAdjunctList(baseVfs);
        }
        return ApiResult.success(new PageResult(page.getTotal(), result, pageSize, pageNum));
    }


    @Override
    public ApiResult appList(DeviceReportSearchReqVO deviceReport, UserSession userSession) {
        DataPermVO dataPerm = dataPermService.getAppDeviceReportPerm(userSession);
        deviceReport.setDataPerm(dataPerm.getDataPerm());
        deviceReport.setCreator(userSession.getUserName());
        int pageNum = deviceReport.getPageNum();
        int pageSize = deviceReport.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<DeviceReportSearchRespVO> result = deviceReportDao.queryAppAll(deviceReport);
        for (DeviceReportSearchRespVO report : result) {
            List<BaseVfs> baseVfs = baseVfsDao.queryByAdjunctId(report.getAdjunctId());
            report.setAdjunctList(baseVfs);
        }
        return ApiResult.success(new PageResult(page.getTotal(), result, pageSize, pageNum));
    }


    /**
     * 新增数据
     *
     * @param deviceReport 实例对象
     * @return 实例对象
     */
    @Override
    public ApiResult save(DeviceReportSaveReqVO deviceReport) {
        deviceReport.setGis(String.format(MSG_FORMAT_GIS,deviceReport.getLongitude(),deviceReport.getLatitude()));
        deviceReportDao.insert(deviceReport);
        List<BaseVfs> baseVfs = baseVfsDao.queryByAdjunctId(deviceReport.getAdjunctId());
        DeviceReportSearchRespVO deviceReportSearchRespVO = new DeviceReportSearchRespVO();
        BeanUtils.copyProperties(deviceReport,deviceReportSearchRespVO);
        deviceReportSearchRespVO.setAdjunctList(baseVfs);
        return ApiResult.success(deviceReportSearchRespVO);
    }

    /**
     * 修改数据
     *
     * @param deviceReport 实例对象
     * @return 实例对象
     */
    @Override
    public ApiResult update(DeviceReportEditReqVO deviceReport) {
        if(deviceReport.getLongitude()!=null && deviceReport.getLatitude()!=null){
            deviceReport.setGis(String.format(MSG_FORMAT_GIS,deviceReport.getLongitude(),deviceReport.getLatitude()));
        }
        deviceReportDao.update(deviceReport);
        return ApiResult.success(deviceReport);
    }


    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.deviceReportDao.deleteById(id) > 0;
    }



}
