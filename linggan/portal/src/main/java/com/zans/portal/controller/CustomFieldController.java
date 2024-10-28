package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.SysCustomData;
import com.zans.portal.model.SysCustomField;
import com.zans.portal.model.SysCustomFieldOption;
import com.zans.portal.service.*;
import com.zans.portal.vo.custom.CustomDataRespVO;
import com.zans.portal.vo.custom.CustomOptionRespVO;
import com.zans.portal.vo.custom.CustomReqVO;
import com.zans.portal.vo.custom.CustomRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.INIT_DATA_TABLE;
import static com.zans.portal.config.GlobalConstants.MODULE_CUSTOM_FIELD_MODULE;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/custom", tags = {"/custom ~ 自定义字段"})
@RestController
@RequestMapping("/custom")
@Validated
@Slf4j
public class CustomFieldController extends BasePortalController {

    @Autowired
    ICustomFieldService customFiledService;
    @Autowired
    ICustomFieldOptionService customFieldOptionService;
    @Autowired
    ICustomDataService customDataService;
    @Autowired
    IConstantItemService constantItemService;
    @Autowired
    ILogOperationService logOperationService;

    @ApiOperation(value = "/list", notes = "自定义字段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "CustomReqVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list(@RequestBody CustomReqVO req) {
        super.checkPageParams(req, "id desc");

        PageResult<CustomRespVO> pageResult = customFiledService.getCustomFieldPage(req);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/insertOrUpdate", notes = "新增自定义字段")
    @RequestMapping(value = "insertOrUpdate", method = {RequestMethod.POST})
    @Transactional(rollbackFor = Exception.class)
    @Record(module = PORTAL_MODULE_CUSTOM_FIELD,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insertOrUpdate(@Valid @RequestBody CustomRespVO req, HttpServletRequest request) {
        SysCustomField customField = new SysCustomField();
        BeanUtils.copyProperties(req, customField);
        if (customField.getSort() == null) {
            Integer count = getSort(req.getModuleName());
            //先改成15个
            if (count > 15) {
                return ApiResult.error("最多只能新增15个自定义字段");
            }
            customField.setSort(count);
        }

        if (StringUtils.isEmpty(customField.getFieldKey())) {
            customField.setFieldKey("key" + customField.getSort());
        }
        if (customField.getId() == null) {
            customFiledService.insert(customField);
        } else {
            customFiledService.update(customField);
        }

        //清空options
        List<SysCustomFieldOption> oldFieldOptions = customFieldOptionService.findByFieldId(customField.getId());
        customFieldOptionService.deleteAll(oldFieldOptions);

        //保存 options
        List<CustomOptionRespVO> options = req.getOptions();
        if (!CollectionUtils.isEmpty(options)) {
            for (CustomOptionRespVO option : options) {
                SysCustomFieldOption fieldOption = new SysCustomFieldOption();
                BeanUtils.copyProperties(option, fieldOption);
                fieldOption.setCustomFieldId(customField.getId());
                if (fieldOption.getId() == null) {
                    customFieldOptionService.insert(fieldOption);
                } else {
                    customFieldOptionService.update(fieldOption);
                }
            }
        }
        return ApiResult.success(MapBuilder.getSimpleMap("id", "")).appendMessage("请求成功");
    }

    @ApiOperation(value = "/insertOrUpdateBatch", notes = "新增自定义字段")
    @RequestMapping(value = "insertOrUpdateBatch", method = {RequestMethod.POST})
    @Transactional(rollbackFor = Exception.class)
    @Record(module = PORTAL_MODULE_CUSTOM_FIELD,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insertOrUpdateBatch(@Valid @RequestBody List<CustomRespVO> req, HttpServletRequest request) {
        for (CustomRespVO respVO : req) {
            SysCustomField customField = new SysCustomField();
            BeanUtils.copyProperties(req, customField);
            if (customField.getSort() == null) {
                Integer count = getSort(respVO.getModuleName());
                if (count > 10) {
                    return ApiResult.error("最多只能新增10个自定义字段");
                }
                customField.setSort(count);
            }

            if (StringUtils.isEmpty(customField.getFieldKey())) {
                customField.setFieldKey("key" + customField.getSort());
            }
            if (customField.getId() == null) {
                customFiledService.insert(customField);
            } else {
                customFiledService.update(customField);
            }

            //清空options
            List<SysCustomFieldOption> oldFieldOptions = customFieldOptionService.findByFieldId(customField.getId());
            customFieldOptionService.deleteAll(oldFieldOptions);

            //保存 options
            List<CustomOptionRespVO> options = respVO.getOptions();

            if (!CollectionUtils.isEmpty(options)) {
                for (CustomOptionRespVO option : options) {
                    SysCustomFieldOption fieldOption = new SysCustomFieldOption();
                    BeanUtils.copyProperties(option, fieldOption);
                    fieldOption.setCustomFieldId(customField.getId());
                    if (fieldOption.getId() == null) {
                        customFieldOptionService.insert(fieldOption);
                    } else {
                        customFieldOptionService.update(fieldOption);
                    }
                }
            }

        }


        return ApiResult.success(MapBuilder.getSimpleMap("id", "")).appendMessage("请求成功");
    }

    @ApiOperation(value = "/view", notes = "查看详情")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.POST})
    public ApiResult view(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        CustomRespVO respVO = new CustomRespVO();
        SysCustomField customField = customFiledService.getById(id);
        BeanUtils.copyProperties(customField, respVO);

        List<CustomOptionRespVO> options = new ArrayList<>();
        List<SysCustomFieldOption> customFieldOptions = customFieldOptionService.findByFieldId(id);
        for (SysCustomFieldOption option : customFieldOptions) {
            CustomOptionRespVO resp = new CustomOptionRespVO();
            BeanUtils.copyProperties(option, resp);
            options.add(resp);
        }
        respVO.setOptions(options);
        return ApiResult.success(respVO);
    }

    @ApiOperation(value = "/delete", notes = "删除自定义字段")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_CUSTOM_FIELD,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        SysCustomField customField = customFiledService.getById(id);
        if (customField == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("自定义字段不存在#" + id);
        }
        Integer count = customDataService.findCountByFieldId(customField.getId());
        if (count > 0) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前字段已使用中，无法删除#" + id);
        }
        customFiledService.delete(customField);
        // 记录日志

        return ApiResult.success().appendMessage("删除成功");
    }


    @ApiOperation(value = "/module", notes = "获取可自定义的模块名")
    @RequestMapping(value = "/module", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult module() {
        List<SelectVO> dict = constantItemService.findItemsByDict(MODULE_CUSTOM_FIELD_MODULE);
        return ApiResult.success(dict);
    }

    @ApiOperation(value = "/data", notes = "根据模块名查询当前模块的自定义字段")
    @RequestMapping(value = "/data", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult data(@RequestParam(value = "id", required = false) String id,
                          @RequestParam(value = "moduleName") String moduleName) {
        List<CustomRespVO> customData = customFiledService.findCustomDataList(id, moduleName);
        return ApiResult.success(customData);
    }

    @ApiOperation(value = "/saveCustomData", notes = "保存自定义字段值")
    @RequestMapping(value = "saveCustomData", method = {RequestMethod.POST})
    @Record
    public ApiResult saveCustomData(@Valid @RequestBody List<CustomDataRespVO> req, HttpServletRequest request) {
        for (CustomDataRespVO respVO : req) {
            SysCustomField field = customFiledService.getById(respVO.getFieldId());
            String rowid = respVO.getRowid();
            String fieldValue = respVO.getFieldValue();
            String tableName = field.getModuleName();
            String columnName = null;

            SysCustomData data = new SysCustomData();
            BeanUtils.copyProperties(respVO, data);
            if (data.getId() != null) {
                customDataService.update(data);
            } else {
                customDataService.insert(data);
            }
            //更新主表字段
            customFiledService.updateCustomFieldInTable(rowid, tableName, columnName, fieldValue);
        }
        return ApiResult.success();
    }

    public Integer getSort(String moduleName) {
        Integer count = customFiledService.getMaxSort(moduleName);
        count = count == null ? 1 : (count + 1);
        if (count > 10) {
            CustomReqVO reqVO = new CustomReqVO();
            reqVO.setModuleName(moduleName);
            reqVO.setPageNum(1);
            reqVO.setPageSize(100);
            PageResult<CustomRespVO> page = customFiledService.getCustomFieldPage(reqVO);
            List<CustomRespVO> list = page.getList();
            if (list.size() >= 10) {
                return count;
            }

            for (int i = 1; i <= 10; i++) {
                Boolean bool = false;
                for (int j = 0; j < list.size(); j++) {
                    bool = list.get(j).getSort() == i;
                    if (bool) {
                        break;
                    }
                }
                if (!bool) {
                    return i;
                }

            }

        }
        return count;
    }

}
