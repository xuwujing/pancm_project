package com.zans.mms.controller.applet;


import com.zans.base.controller.BaseController;
import com.zans.base.util.HttpHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.service.*;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.common.TreeSelect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.GlobalConstants.*;


/**
 * (BaseArea)表控制层
 *
 * @author makejava
 * @since 2021-01-12 15:20:08
 */
@Api(value = "基础数据(app)", tags = {"/基础数据(app)"})
@RestController
@RequestMapping("app/base")
@Slf4j
public class AppBaseMMSController extends BaseController {

    @Autowired
    IConstantItemService constantItemService;


    @Autowired
    ISysUserService sysUserService;

    @Autowired
    IBaseAreaService baseAreaService;

    @Autowired
    IBaseDeviceTypeService baseDeviceTypeService;

    @Autowired
    IBaseOrgService baseOrgService;

    @Autowired
    IBaseOrgRoleService baseOrgRoleService;

    @Autowired
    IBaseFaultTypeService baseFaultTypeService;

    @Autowired
    private ITicketService ticketService;

    @Autowired
    private IPatrolTaskService patrolTaskService;

    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    private IPoManagerService poManagerService;

    @ApiOperation(value = "/init", notes = "")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    public ApiResult listBaseInitData(HttpServletRequest request) {
        List<SelectVO> patrolAssetStatusList = constantItemService.findItemsByDict(MODULE_PATROL_ASSET_STATUS);
        List<SelectVO> patrolStatusList = constantItemService.findItemsByDict(MODULE_PATROL_STATUS);
        List<SelectVO> maintainStatusList = constantItemService.findItemsByDict(SYS_DICT_KEY_MAINTAIN_STATUS);
        List<SelectVO> checkResultList = constantItemService.findItemsByDict(SYS_DICT_KEY_PATROL_CHECK_RESULT);
        List<SelectVO> deviceCategoryList = constantItemService.findItemsByDict(MODULE_DEVICE_CATEGORY);
        List<SelectVO> deviceTypeList = baseDeviceTypeService.findDeviceTypeToSelect();
        List<TreeSelect> areaTree = baseAreaService.areaTreeList();
        List<SelectVO> areaList = baseAreaService.areaList();
        List<SelectVO> itemClassificationList = constantItemService.findItemsByDict(SYS_DICT_KEY_ITEM_CLASSIFICATION);
        UserSession user = httpHelper.getUser(request);
        //查询单位类型
        String orgType = baseOrgService.getOrgType(user.getOrgId());
        if(!StringHelper.isEmpty(orgType)&&"03".equals(orgType)){
            itemClassificationList = constantItemService.findItemsByDict("item_classification1");
        }
        if(!StringHelper.isEmpty(orgType)&&"02".equals(orgType)){
            if("30001".equals(user.getOrgId())){
                itemClassificationList = constantItemService.findItemsByDict("item_classification5");

            }
            if("30002".equals(user.getOrgId())){
                itemClassificationList = constantItemService.findItemsByDict("item_classification6");

            }
            if("30003".equals(user.getOrgId())){
                itemClassificationList = constantItemService.findItemsByDict("item_classification7");

            }
        }
        List<SelectVO> projectNameList = constantItemService.findItemsByDict(SYS_DICT_KEY_PROJECT_NAME);
        List<SelectVO> allOrgList = baseOrgService.orgList();
        List<SelectVO> orgList = baseOrgService.queryBaseOrg();
        List<SelectVO>  orgRoleList = baseOrgRoleService.orgRoleList();
        List<SelectVO> faultList = baseFaultTypeService.faultList();
        Map<String, List<SelectVO>> baseDeviceTypeFaultSelect = baseFaultTypeService.listFaultTypeView();
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_DEVICE,deviceTypeList)
                .put(MODULE_PATROL_ASSET_STATUS,patrolAssetStatusList)
                .put(MODULE_PATROL_STATUS,patrolStatusList)
                .put("area_tree",areaTree)
                .put("areaList",areaList)
                .put("allOrgList",allOrgList)
                .put("orgList",orgList)
                .put(SYS_DICT_KEY_MAINTAIN_STATUS,maintainStatusList)
                .put(MODULE_DEVICE_CATEGORY,deviceCategoryList)
                .put(SYS_DICT_KEY_PATROL_CHECK_RESULT,checkResultList)
                .put("orgRoleList",orgRoleList)
                .put("type_fault_list",baseDeviceTypeFaultSelect)
                .put("type_fault",faultList)
                .put("projectName",projectNameList)
                .put("itemClassificationList",itemClassificationList)
                .build();
        return ApiResult.success(result);
    }




    @ApiOperation(value = "/依据用户名得到单位角色", notes = "依据用户名得到单位角色")
    @RequestMapping(value = "/getMaintainRoleByUser", method = {RequestMethod.GET})
    public ApiResult getMaintainRoleByUser(@RequestParam("mobile") String mobile) {
        return ApiResult.success(sysUserService.queryByMobile(null, mobile));
    }



    @ApiOperation(value = "/getTotal", notes = "APP总数查询")
    @RequestMapping(value = "/getTotal", method = {RequestMethod.GET})
    public ApiResult getTotal(HttpServletRequest request) {
        UserSession userSession = httpHelper.getUser(request);
        List<CircleUnit> ticketsTotal = ticketService.getAppTicketTotal(userSession);
        List<CircleUnit> patrolSchemeTotal = patrolTaskService.getAppPatrolTotal(userSession);
        List<CircleUnit> poManagerTotal = poManagerService.getAppPoManagerTotal(userSession);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("tickets_total", ticketsTotal)
                .put("patrol_total", patrolSchemeTotal)
                .put("po_total",poManagerTotal)
                .build();
        if(userSession.getRoleId().equals("0201")){
            result = MapBuilder.getBuilder()
                    .put("tickets_total", ticketsTotal)
                    .put("patrol_total", patrolSchemeTotal)
                    .build();
        }
        return ApiResult.success(result);
    }


}
