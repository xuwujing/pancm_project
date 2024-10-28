package com.zans.portal.controller;

import com.zans.base.vo.ApiResult;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.util.RedisUtil;
import com.zans.portal.vo.log.EsLoginLogRespVO;
import com.zans.portal.vo.log.EsOperationLogRespVO;
import com.zans.portal.vo.user.TUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "/log", tags = {"/log ~ 操作日志/运行日志"})
@RestController
@RequestMapping("/log")
@Validated
@Slf4j
public class LogController extends BasePortalController {

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    private RedisUtil redisUtil;



//
//
//
//
//    @Autowired
//    IConstantItemService constantItemService;
//
//    @Autowired
//    IRoleService roleService;
//
//

//    @ApiOperation(value = "/op/list", notes = "用户操作日志查询")
//    @ApiImplicitParams({
//            @ApiImplicitParam( name = "req", value = "查询条件", required = true,
//                    dataType = "OpLogSearchVO", paramType = "body")
//    })
//    @RequestMapping(value = "/op/list", method = {RequestMethod.POST})
//    @ResponseBody
//    public ApiResult<PageResult<OpLogRespVO>> getOperationPageList(@RequestBody OpLogSearchVO req) {
//        super.checkPageParams(req, operationOrder);
//        PageResult<OpLogRespVO> pageResult = logOperationService.getPage(req);
//        return ApiResult.success(pageResult);
//    }
//
//    @ApiOperation(value = "/op/init", notes = "操作日志初始化")
//    @RequestMapping(value = "/op/init", method = {RequestMethod.GET})
//    @ResponseBody
//    public ApiResult initOperationList() {
//        List<SelectVO> roleList = roleService.findRoleToSelect();
//        List<SelectVO> moduleList = constantItemService.findItemsByDict(MODULE_OP_LOG_MODULE);
//
//        OpLogSearchVO req = new OpLogSearchVO();
//        super.checkPageParams(req, operationOrder);
//        PageResult<OpLogRespVO> pageResult = logOperationService.getPage(req);
//        Map<String, Object> result = MapBuilder.getBuilder()
//                .put(MODULE_ROLE, roleList)
//                .put(MODULE_OP_LOG_MODULE, moduleList)
//                .put(INIT_DATA_TABLE, pageResult)
//                .build();
//        return ApiResult.success(result);
//    }
//
//    @ApiOperation(value = "/op/view", notes = "操作日志详情，by id")
//    @ApiImplicitParam(name = "id", value = "地址id", required = true,
//            dataType = "int", paramType = "query")
//    @RequestMapping(value = "/op/view", method = {RequestMethod.GET})
//    public ApiResult<OpLogRespVO> getIpById(@NotNull(message="id必填")  Integer id) {
//        OpLogRespVO vo = logOperationService.getOpLog(id);
//        if (vo == null) {
//            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("操作日志ID不存在#" + id);
//        }
//        return ApiResult.success(vo);
//    }


    @ApiOperation(value = "/login/list",notes = "登录日志查看")
    @RequestMapping(value = "/login/list",method = RequestMethod.POST)
    public ApiResult loginLog(@RequestBody EsLoginLogRespVO esLoginLogVO){
        super.checkPageParams(esLoginLogVO);
        return ApiResult.success(logOperationService.loginLog(esLoginLogVO));
    }

    @ApiOperation(value = "/op/list",notes = "操作日志查看")
    @RequestMapping(value = "/op/list",method = RequestMethod.POST)
    public ApiResult operationLog(@RequestBody EsOperationLogRespVO esOperationLogRespVO){
        super.checkPageParams(esOperationLogRespVO);
        return ApiResult.success(logOperationService.operationLog(esOperationLogRespVO));
    }

    @ApiOperation(value = "/op/list/init",notes = "下拉数据")
    @RequestMapping(value = "/op/list/init",method = RequestMethod.GET)
    public ApiResult init(){
        return logOperationService.init();
    }


    @ApiOperation(value = "/onLine/list",notes = "在线用户统计")
    @RequestMapping(value = "/onLine/list",method = RequestMethod.POST)
    public ApiResult onLineList(@RequestBody TUserVO tUserVO){
        super.checkPageParams(tUserVO);
        return logOperationService.onLineList(tUserVO);
    }

}
