package com.zans.portal.controller;

import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.service.INetworkSwitcherInterfaceService;
import com.zans.portal.vo.switcher.SwitcherInterfaceReqVO;
import com.zans.portal.vo.switcher.SwitcherInterfaceRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.zans.portal.config.GlobalConstants.INIT_DATA_TABLE;

@Api(value = "/network/switcher/interface", tags = {"/network/switcher/interface ~ 网络交换机接口"})
@RestController
@RequestMapping("/network/switcher/interface")
@Validated
@Slf4j
public class NetworkSwitcherInterfaceController extends BasePortalController {

    @Autowired
    INetworkSwitcherInterfaceService interfaceService;
    @Autowired
    ILogOperationService logOperationService;

    @ApiOperation(value = "/list", notes = "交换机下行设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "SwitcherInterfaceReqVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list(@RequestBody SwitcherInterfaceReqVO req) {
        super.checkPageParams(req, " interface_index desc ");

        PageResult<SwitcherInterfaceRespVO> pageResult = interfaceService.getSwitcherInterfacePage(req);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }

}
