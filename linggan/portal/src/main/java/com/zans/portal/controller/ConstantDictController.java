package com.zans.portal.controller;

import com.alibaba.fastjson.JSON;
import com.zans.base.annotion.Record;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.SysConstantDict;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.service.IModuleService;
import com.zans.portal.service.ISysConstantDictService;
import com.zans.portal.util.LogBuilder;
import com.zans.portal.vo.constant.ConstantDictResVO;
import com.zans.portal.vo.constant.ConstantDictSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/constant", tags = {"/constant ~ 系统字典表类型管理"})
@RestController
@RequestMapping("/constant")
@Validated
@Slf4j
public class ConstantDictController extends BasePortalController {

    @Autowired
    ISysConstantDictService constantDictService;

    @Autowired
    IModuleService moduleService;

    @Autowired
    ILogOperationService logOperationService;

    @ApiOperation(value = "/dict/list", notes = "系统字典列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "ConstantDictSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/dict/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list(@RequestBody ConstantDictSearchVO req) {
        super.checkPageParams(req, "id desc");

        List<SelectVO> moduleu = moduleService.findModuleToSelect();

        PageResult<ConstantDictResVO> pageResult = constantDictService.getConstantDictPage(req);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .put(MODULE_MODULE, moduleu)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/dict/insert", notes = "新增字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "ConstantDictResVO", paramType = "body")
    })
    @RequestMapping(value = "/dict/insert", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CONSTANT_DICT,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insert(@Valid @RequestBody ConstantDictResVO req, HttpServletRequest request) {
        SelectVO selectByKey = constantDictService.findByKeyOrName(req.getDictKey(), "");
        SelectVO selectByName = constantDictService.findByKeyOrName("", req.getDictName());
        if (selectByKey != null && StringUtils.isNotEmpty(selectByKey.getItemValue())) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("已存在该key值#" + req.getDictKey());
        }
        if (selectByName != null && StringUtils.isNotEmpty(selectByName.getItemValue())) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("已存在该名称#" + req.getDictKey());
        }
        SysConstantDict dict = new SysConstantDict();
        BeanUtils.copyProperties(req, dict);
        dict.setModuleId(req.getModuleId());
        constantDictService.insert(dict);


        return ApiResult.success(MapBuilder.getSimpleMap("id", dict.getDictKey())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/dict/update", notes = "修改字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "ConstantDictResVO", paramType = "body")
    })
    @RequestMapping(value = "/dict/update", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CONSTANT_DICT,operation = PORTAL_LOG_OPERATION_EDIT)
    public ApiResult update(@Valid @RequestBody ConstantDictResVO req, HttpServletRequest request) {
        SelectVO selectByKey = constantDictService.findByKeyOrName(req.getDictKey(), "");
        SelectVO selectByName = constantDictService.findByKeyOrName("", req.getDictName());
        if (selectByKey == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("系统字典不存在#" + req.getDictKey());
        }
        if (selectByName != null && StringUtils.isNotEmpty(selectByName.getItemValue()) && !selectByName.getItemValue().equals(selectByKey.getItemValue())) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("已存在该名称#" + req.getDictKey());
        }
        SysConstantDict dict = new SysConstantDict();
        BeanUtils.copyProperties(req, dict);
        dict.setModuleId(req.getModuleId());
        constantDictService.update(dict);


        return ApiResult.success(MapBuilder.getSimpleMap("id", dict.getDictKey())).appendMessage("请求成功");
    }


    @ApiOperation(value = "/dict/delete", notes = "删除字典数据")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/dict/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CONSTANT_DICT,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "id必填") String id, HttpServletRequest request) {
        SysConstantDict dict = constantDictService.findByKey(id);
        if (dict == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("系统字典不存在#" + id);
        }
        constantDictService.delete(dict);

        // 记录日志

        return ApiResult.success().appendMessage("删除成功");
    }


}
