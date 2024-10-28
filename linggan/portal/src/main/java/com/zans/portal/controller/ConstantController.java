package com.zans.portal.controller;

import com.alibaba.fastjson.JSON;
import com.zans.base.annotion.Record;
import com.zans.base.util.LicenseHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.LicenseVO;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.SysConstant;
import com.zans.portal.service.*;
import com.zans.portal.util.LogBuilder;
import com.zans.portal.vo.constant.ConstantReqVO;
import com.zans.portal.vo.constant.ConstantSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.INIT_DATA_TABLE;
import static com.zans.portal.config.GlobalConstants.LOG_MODULE_MODEL;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/constant", tags = {"/constant ~ 系统鉴权管理"})
@RestController
@RequestMapping("/constant")
@Validated
@Slf4j
public class ConstantController extends BasePortalController {

    @Autowired
    ISysConstantService sysConstantService;

    @Autowired
    IRoleService roleService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IDeviceTypeService deviceTypeService;



    @Autowired
    IModuleService moduleService;

    @Autowired
    IArpService arpService;

    @Autowired
    ILogOperationService logOperationService;

    @ApiOperation(value = "/list", notes = "系统鉴权列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "ConstantSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list(@RequestBody ConstantSearchVO req) {
        super.checkPageParams(req, "id desc");

        PageResult<ConstantReqVO> pageResult = sysConstantService.getConstantPage(req);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/insert", notes = "新增系统鉴权值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "ConstantReqVO", paramType = "body")
    })
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CONSTANT,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insert(@Valid @RequestBody ConstantReqVO req, HttpServletRequest request) {
        SysConstant constant = sysConstantService.findConstantByKey(req.getConstantKey());
        if (constant != null) {
            return ApiResult.error("当前常量键已存在");
        }
        constant = new SysConstant();
        BeanUtils.copyProperties(req, constant);
        sysConstantService.insert(constant);


        return ApiResult.success(MapBuilder.getSimpleMap("id", constant.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/update", notes = "修改系统鉴权值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "ConstantReqVO", paramType = "body")
    })
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CONSTANT,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult update(@RequestBody ConstantReqVO req, HttpServletRequest request) {
        SysConstant constant = sysConstantService.getById(req.getId());
        if (constant == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("系统常量不存在#" + req.getId());
        }
        if (!constant.getConstantKey().equals(req.getConstantKey())) {
            return ApiResult.error("不允许修改键");
        }
        BeanUtils.copyProperties(req, constant);
        sysConstantService.update(constant);


        return ApiResult.success(MapBuilder.getSimpleMap("id", constant.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/delete", notes = "设置为不可用")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CONSTANT,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        SysConstant constant = sysConstantService.getById(id);
        if (constant == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("系统常量不存在#" + id);
        }
        constant.setStatus(0);
        sysConstantService.update(constant);

        // 记录日志

        return ApiResult.success().appendMessage("设置成功");
    }

    @ApiOperation(value = "/findByKey", notes = "根据key查找系统鉴权值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "常量主键", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/findByKey", method = {RequestMethod.GET})
    public ApiResult findByKey(String key) {
        SelectVO selectVO = sysConstantService.findSelectVOByKey(key);
        return ApiResult.success(selectVO);
    }


    @ApiOperation(value = "/getLicense", notes = "获取当前系统的授权信息")
    @RequestMapping(value = "/getLicense", method = {RequestMethod.GET})
    public ApiResult getLicense() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            SelectVO license = sysConstantService.findSelectVOByKey("license");
            SelectVO publicKey = sysConstantService.findSelectVOByKey("publicKey");
            if (StringUtils.isEmpty(license) || StringUtils.isEmpty(publicKey)) {
                return ApiResult.success(resultMap);
            }
            LicenseVO licenseVO = LicenseHelper.LicenseDecrypt(publicKey.getItemValue(), license.getItemValue());

            Map<String, Object> map = new HashMap<String, Object>();
            Class<?> clazz = licenseVO.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(licenseVO);
                map.put(fieldName, value);
            }

            for (String key : map.keySet()) {
                Object value = map.get(key);
                resultMap.put(key, value);
            }

            resultMap.put("copyright", "武汉零感网御网络科技有限公司");
            return ApiResult.success(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.error("请求异常");
    }


    @ApiOperation(value = "/activation", notes = "激活")
    @RequestMapping(value = "/activation", method = {RequestMethod.GET})
    @Record(module = PORTAL_MODULE_CONSTANT,operation = LOG_OPERATION_ACTIVATION)
    public ApiResult getLicenseMsg(String license) {
        SelectVO publicKey = sysConstantService.findSelectVOByKey("publicKey");
        LicenseVO licenseVO = LicenseHelper.LicenseDecrypt(publicKey.getItemValue(), license);
        if (licenseVO == null || StringUtils.isEmpty(licenseVO.getMenuList())) {
            return ApiResult.error("激活码错误");
        }
        SysConstant constant = sysConstantService.findConstantByKey("license");
        constant.setConstantValue(license);
        sysConstantService.update(constant);

        //todo 将可用菜单回写到菜单表
        return ApiResult.success();
    }

}
