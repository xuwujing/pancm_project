package com.zans.mms.controller.applet;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.service.IDevicePointCheckLogService;
import com.zans.mms.service.IDevicePointService;
import com.zans.mms.vo.devicepoint.DevicePointAddReqVO;
import com.zans.mms.vo.devicepoint.check.DevicePointCheckReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.zans.mms.config.MMSConstants.*;

/**
 * @author
 * @version V1.0
 **/
@RestController
@RequestMapping("app/devicePoint")
@Api(tags = "app点位管理")
@Validated
@Slf4j
public class AppDevicePointController extends BaseController {

    @Autowired
    IDevicePointService devicePointService;

    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    IDevicePointCheckLogService devicePointCheckLogService;



    /**
     * 新增一条数据
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "新增", notes = "新增")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Record(module = MODULE_POINT,operation = LOG_OPERATION_SAVE,opPlatform = LOG_APP)
    public ApiResult<DevicePoint> insert(@RequestBody DevicePointAddReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        DevicePoint devicePoint = new DevicePoint();
        BeanUtils.copyProperties(reqVO,devicePoint);
        devicePoint.setCreator(userSession.getUserName());
        int result = devicePointService.saveSelective(devicePoint);
        if (result > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",devicePoint.getId());
            return ApiResult.success(jsonObject);
        }
        return ApiResult.error("新增失败");
    }

    /**
     * 校正点位
     *
     * @param reqVO 实体类
     * @return Response对象
     */
    @ApiOperation(value = "校正点位")
    @RequestMapping(value = "checkPoint", method = RequestMethod.POST)
    @Record(module = MODULE_POINT,operation = LOG_OPERATION_SAVE,opPlatform = LOG_APP)
    public ApiResult<DevicePoint> checkPoint(@RequestBody DevicePointCheckReqVO reqVO, HttpServletRequest httpRequest) {
        UserSession userSession = httpHelper.getUser(httpRequest);
        reqVO.setCreator(userSession.getUserName());
        boolean b = devicePointCheckLogService.checkPoint(reqVO);
        if (b) {
            return ApiResult.success().message("校正点位成功");
        }
        return ApiResult.error("校正点位失败");
    }



}
