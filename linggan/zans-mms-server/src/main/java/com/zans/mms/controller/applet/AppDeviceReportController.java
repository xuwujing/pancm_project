package com.zans.mms.controller.applet;

import com.zans.base.annotion.Record;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.DeviceReport;
import com.zans.mms.service.IDeviceReportService;
import com.zans.mms.vo.devicereport.DeviceReportSaveReqVO;
import com.zans.mms.vo.devicereport.DeviceReportSearchReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.zans.mms.config.MMSConstants.*;


/**
 * 设备上报(DeviceReport)表控制层
 *
 * @author beixing
 * @since 2021-03-08 17:21:40
 */
@Api(tags = "设备上报(APP)")
@RestController
@RequestMapping("app/deviceReport")
public class AppDeviceReportController {
    /**
     * 服务对象
     */
    @Autowired
    private IDeviceReportService deviceReportService;

    @Autowired
    private HttpHelper httpHelper;

    /**
     * 新增一条数据
     *
     * @param deviceReport 实体类
     * @return Response对象
     */
    @ApiOperation(value = "设备上报新增", notes = "设备上报新增")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Record(module = MODULE_DEVICE_REPORT,operation = LOG_OPERATION_SAVE,opPlatform = LOG_APP)
    public ApiResult<DeviceReport> insert(@RequestBody DeviceReportSaveReqVO deviceReport, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        deviceReport.setCreator(userSession.getUserName());
        return deviceReportService.save(deviceReport);
    }



    /**
     * 分页查询
     */
    @ApiOperation(value = "设备上报查询", notes = "设备上报查询")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ApiResult<DeviceReport> list(@RequestBody DeviceReportSearchReqVO deviceReport, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        return deviceReportService.appList(deviceReport, userSession);
    }



}
