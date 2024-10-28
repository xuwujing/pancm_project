package com.zans.portal.controller;

import com.google.common.collect.ImmutableMap;
import com.zans.base.vo.ApiResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home/monitor")
@Slf4j
public class HomeMonitorController extends BasePortalController {



    /**
     * 重点线路
     * @return
     */
    @ApiOperation(value = "",notes = "重点线路")
    @RequestMapping(value = "/emphasisPath", method = {RequestMethod.GET})
    public ApiResult emphasisPath(){
        List list = new ArrayList();
        list.add(ImmutableMap.of("pointName","天河机场","deviceTotal",105,"onlineCount",105,"onlineRate",100.0,"offStreamUnit",0));
        list.add(ImmutableMap.of("pointName","马家湖特大桥","deviceTotal",12,"onlineCount",12,"onlineRate",100.0,"offStreamUnit",0));
        list.add(ImmutableMap.of("pointName","黄花涝立交桥","deviceTotal",158,"onlineCount",155,"onlineRate",98.1,"offStreamUnit",3));
        list.add(ImmutableMap.of("pointName","丰荷山互通","deviceTotal",321,"onlineCount",320,"onlineRate",99.6,"offStreamUnit",1));
        list.add(ImmutableMap.of("pointName","机场高速汽车充电站","deviceTotal",258,"onlineCount",256,"onlineRate",99.2,"offStreamUnit",2));
        list.add(ImmutableMap.of("pointName","武汉市消防救援队","deviceTotal",276,"onlineCount",275,"onlineRate",99.6,"offStreamUnit",1));
        list.add(ImmutableMap.of("pointName","唐家墩","deviceTotal",177,"onlineCount",177,"onlineRate",100.0,"offStreamUnit",0));
        list.add(ImmutableMap.of("pointName","竹叶山立交桥","deviceTotal",31,"onlineCount",29,"onlineRate",93.5,"offStreamUnit",2));
        list.add(ImmutableMap.of("pointName","黄埔大街立交桥","deviceTotal",212,"onlineCount",210,"onlineRate",99.0,"offStreamUnit",2));
        list.add(ImmutableMap.of("pointName","四美塘公园","deviceTotal",98,"onlineCount",97,"onlineRate",98.9,"offStreamUnit",1));
        list.add(ImmutableMap.of("pointName","东湖宾馆","deviceTotal",189,"onlineCount",189,"onlineRate",100.0,"offStreamUnit",0));
        return ApiResult.success(list);
    }


}
