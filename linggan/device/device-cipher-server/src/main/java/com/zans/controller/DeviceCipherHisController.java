package com.zans.controller;

import com.zans.model.DeviceCipherHis;
import com.zans.service.IDeviceCipherHisService;
import com.zans.utils.HttpHelper;
import com.zans.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * (DeviceCipherHis)表控制层
 *
 * @author beixing
 * @since 2021-08-23 16:15:56
 */
@RestController
@RequestMapping("deviceCipherHis")
public class DeviceCipherHisController {
    /**
     * 服务对象
     */
    @Autowired
    private IDeviceCipherHisService deviceCipherHisService;

    @Autowired
    private HttpHelper httpHelper;

    /**
     * 新增一条数据
     *
     * @param deviceCipherHis 实体类
     * @return Response对象
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ApiResult<DeviceCipherHis> insert(@RequestBody DeviceCipherHis deviceCipherHis, HttpServletRequest httpRequest) {
        int result = deviceCipherHisService.insert(deviceCipherHis);
        if (result > 0) {
            return ApiResult.success();
        }
        return ApiResult.error("新增失败");
    }

    /**
     * 修改一条数据
     *
     * @param deviceCipherHis 实体类
     * @return Response对象
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ApiResult<DeviceCipherHis> update(@RequestBody DeviceCipherHis deviceCipherHis, HttpServletRequest httpRequest) {
        deviceCipherHisService.update(deviceCipherHis);
        return ApiResult.success();
    }

    /**
     * 删除一条数据
     *
     * @param deviceCipherHis 参数对象
     * @return Response对象
     */
    @RequestMapping(value = "del", method = RequestMethod.POST)
    public ApiResult<DeviceCipherHis> delete(@RequestBody DeviceCipherHis deviceCipherHis, HttpServletRequest httpRequest) {
        deviceCipherHisService.deleteById(deviceCipherHis.getId());
        return ApiResult.success();
    }


    /**
     * 分页查询
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ApiResult<DeviceCipherHis> list(@RequestBody DeviceCipherHis deviceCipherHis) {
        return deviceCipherHisService.list(deviceCipherHis);
    }

}
