package com.zans.portal.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.zans.base.util.FileHelper;
import com.zans.base.vo.ApiResult;
import com.zans.portal.service.*;
import com.zans.portal.util.GetProperties;
import com.zans.portal.vo.radius.EndPointReqVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController extends BasePortalController {



    @Autowired
    IIpService ipService;

    @Autowired
    IRadiusEndPointService radiusEndPointService;

    @Autowired
    IAlertRuleService ruleService;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IAssetBranchAssetService assetBranchAssetService;


    @Autowired
    IAssetService assetService;

    @Autowired
    AlertOfflineDeviceController alertOfflineDeviceController;


    @Autowired
    private  RadiusEndPointController radiusEndPointController;

    @Autowired
    ILogOperationService logOperationService;

    @Value("${spring.profiles.active}")
    String active;

    @Autowired
    private IHomeGlobalConfigService homeGlobalConfigService;



    private final static String  VERSION_NAME = "version.txt";

    @ApiOperation(value = "/version", notes = "版本信息")
    @RequestMapping(value = "/version", method = {RequestMethod.GET})
    public JSONObject version(HttpServletRequest request){
        Map<String, String> map = GetProperties.getAppSettings();
        JSONObject result = new JSONObject();
        result.put("git_branch", map.get("git_branch"));
        result.put("build_time", map.get("build_time"));
        result.put("git_commit", map.get("git_commit"));
        result.put("app_name", map.get("build_app"));
        result.put("profile", active);
        result.put("version", FileHelper.readResourcesFile(VERSION_NAME));
        return result;
    }

    /**
     * @Author pancm
     * @Description  获取资产总数
     * @Date  2020/10/20
     * @Param [request]
     * @return com.zans.base.vo.ApiResult
     **/
    @RequestMapping(value = "/getAssetTotal", method = RequestMethod.GET)
    public ApiResult getAssetTotal() {
        return ApiResult.success(radiusEndPointService.getAssetTotal());
    }

    /**
     * @Author pancm
     * @Description  获取准入总数
     * @Date  2020/10/20
     * @Param [request]
     * @return com.zans.base.vo.ApiResult
     **/
    @RequestMapping(value = "/getAccessTotal", method = RequestMethod.GET)
    public ApiResult getAccessTotal() {
        return ApiResult.success(radiusEndPointService.getAccessTotal());
    }










    /**
    * @Author beixing
    * @Description  查看未读的告警数
     * 注：目前只提供给孝感使用
    * @Date  2021/5/10
    * @Param
    * @return
    **/
    @RequestMapping(value = "/getAlertUnReadTotal", method = RequestMethod.GET)
    public ApiResult getAlertUnReadTotal() {
        return ruleService.getAlertUnReadTotal();
    }


    /*  -------------------风险感知相关接口 end----------------------             */



    /**
     * 资产类型统计数据
     * @return
     */
    @RequestMapping(value = "/assetDeviceStatis", method = {RequestMethod.GET})
    public ApiResult assetDeviceStatis(){
        return ApiResult.success(assetService.getAssetDeviceStatis());
    }



    @ApiOperation(value = "/radius/endpoint/list", notes = "radius 在线、离线、检疫列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "EndPointReqVO", paramType = "body")
    })
    @RequestMapping(value = "/radius/endpoint/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult endpointList(@RequestBody EndPointReqVO req) {
       return radiusEndPointController.list(req);
    }



    @ApiOperation(value = "/asset", notes = "资产准入情况报表")
    @RequestMapping(value = "/asset", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult findRadiusMacByDate(HttpServletRequest request) {
        return ApiResult.success(assetService.findRadiusMacByDate());
    }

    @GetMapping("/webSocket/heartbeat")
    public ApiResult heartbeat(HttpServletRequest request){
        return logOperationService.heartbeat(super.getUserSession(request).getUserId());
    }

    @RequestMapping(value = "/maintainStatis", method = {RequestMethod.GET})
    public ApiResult maintainStatis(){
        return ApiResult.success(ImmutableMap.of("sendTotal",8,"maintainTotal",6,"maintainRate",75.00,"unSend",2));
    }


    /**
     * 设备分组在线率
     * @return
     */
    @RequestMapping(value = "/assetBranchStatis", method = {RequestMethod.GET})
    public ApiResult assetBranchStatis(){
        return ApiResult.success(assetBranchAssetService.getAssetBranchStatis());
    }

    /**
     * 获取品牌信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getBrand", method = RequestMethod.GET)
    public ApiResult getBrand(HttpServletRequest request) {
        return ApiResult.success(assetService.getBrand());
    }



    @ApiOperation(value = "/globalConfig", notes = "全局配置")
    @RequestMapping(value = "/globalConfig", method = {RequestMethod.GET})
    public ApiResult globalConfig(@RequestParam(value = "id",defaultValue = "1") Integer id,HttpServletRequest request){
        return homeGlobalConfigService.globalConfig(id);
    }

    @ApiOperation(value = "/getIndex", notes = "指标配置")
    @RequestMapping(value = "/getIndex", method = {RequestMethod.GET})
    public ApiResult getIndex(@RequestParam(value = "id",defaultValue = "1") Integer id, HttpServletRequest request){
        return homeGlobalConfigService.getIndex(id);
    }







}
