package com.zans.mms.controller.demokit;


import com.zans.base.controller.BaseController;
import com.zans.base.vo.ApiResult;
import com.zans.mms.service.IAssetService;
import com.zans.mms.vo.asset.SwitcherMacInterfaceResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* @Title: NetworkController
* @Description: 交换机网络
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 6/16/21
*/
@RestController
@RequestMapping("demoKit/network")
@Api(tags = "交换机网络")
@Slf4j
public class NetworkController extends BaseController {
    @Autowired
    IAssetService assetService;

    @ApiOperation(value = "入网路径", notes = "入网路径")
    @RequestMapping(value = "/switchMacInterface", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult switchMacInterface() {
        List<SwitcherMacInterfaceResVO> list =  assetService.getSwitchMacInterface();
        return ApiResult.success(list);
    }

}
