package com.zans.controller;

import com.zans.model.DeviceCipherApprove;
import com.zans.service.IDeviceCipherApproveService;
import com.zans.utils.HttpHelper;
import com.zans.vo.ApiResult;
import com.zans.vo.DeviceCipherApproveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * (DeviceCipherApprove)表控制层
 *
 * @author beixing
 * @since 2021-08-23 16:23:48
 */
@RestController
@RequestMapping("deviceCipherApprove")
public class DeviceCipherApproveController {
    /**
     * 服务对象
     */
    @Autowired
    private IDeviceCipherApproveService deviceCipherApproveService;

    @Autowired
    private HttpHelper httpHelper;

    /**
     * 新增一条数据
     *
     * @param deviceCipherApprove 实体类
     * @return Response对象
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ApiResult<DeviceCipherApprove> insert(@RequestBody DeviceCipherApprove deviceCipherApprove, HttpServletRequest httpRequest) {
        int result = deviceCipherApproveService.insert(deviceCipherApprove);
        if (result > 0) {
            return ApiResult.success();
        }
        return ApiResult.error("新增失败");
    }

    /**
     * 修改一条数据
     *
     * @param deviceCipherApprove 实体类
     * @return Response对象
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ApiResult<DeviceCipherApprove> update(@RequestBody DeviceCipherApprove deviceCipherApprove, HttpServletRequest httpRequest) {
        deviceCipherApproveService.update(deviceCipherApprove);
        return ApiResult.success();
    }

    /**
     * 删除一条数据
     *
     * @param deviceCipherApprove 参数对象
     * @return Response对象
     */
    @RequestMapping(value = "del", method = RequestMethod.POST)
    public ApiResult<DeviceCipherApprove> delete(@RequestBody DeviceCipherApprove deviceCipherApprove, HttpServletRequest httpRequest) {
        deviceCipherApproveService.deleteById(deviceCipherApprove.getId());
        return ApiResult.success();
    }

    /**
     * 审批列表  待审批/审批历史
     * @param deviceCipherApproveVO
     * @return
     */
    @RequestMapping(value = "approval/list",method = RequestMethod.POST)
    public ApiResult approvalList(@RequestBody DeviceCipherApproveVO deviceCipherApproveVO){
        return deviceCipherApproveService.approvalList(deviceCipherApproveVO);
    }

}
