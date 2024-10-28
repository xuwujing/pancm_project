package com.zans.portal.controller;

import com.alibaba.fastjson.JSON;
import com.zans.base.annotion.Record;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.SysBrand;
import com.zans.portal.service.IArpService;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.service.ISysBrandService;
import com.zans.portal.util.LogBuilder;
import com.zans.portal.vo.brand.BrandMergeVO;
import com.zans.portal.vo.brand.BrandResponseVO;
import com.zans.portal.vo.brand.BrandSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

import static com.zans.base.config.BaseConstants.CODE_SUCCESS;
import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.PORTAL_LOG_OPERATION_ADD;
import static com.zans.portal.constants.PortalConstants.PORTAL_MODULE_BRAND;
//import static com.zans.portal.constants.PortalConstants.PORTAL_MODULE_MODEL;

@Api(value = "/brand", tags = {"/brand ~ 设备品牌管理"})
@RestController
@RequestMapping("/brand")
@Validated
@Slf4j
public class SysBrandController extends BasePortalController {

    @Autowired
    ISysBrandService service;
    @Autowired
    ILogOperationService logOperationService;
    @Autowired
    IArpService arpService;

    @ApiOperation(value = "/list", notes = "设备品牌查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "BrandSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult getList(@RequestBody BrandSearchVO req) {
        super.checkPageParams(req, "brand_id desc");
        PageResult<BrandResponseVO> pageResult = service.getBrandPage(req);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/insertOrUpdate", notes = "新增或修改设备品牌")
    @ApiImplicitParam(name = "mergeVO", value = "新增或修改设备品牌", required = true, dataType = "BrandMergeVO", paramType = "body")
    @RequestMapping(value = "/insertOrUpdate", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_BRAND,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insertOrUpdate(@Valid @RequestBody BrandMergeVO mergeVO,
                                    HttpServletRequest request) {
        SysBrand model = new SysBrand();
        BeanUtils.copyProperties(mergeVO, model);
        //校验"品牌名称、品牌公司、品牌关键字"不能重复
        ApiResult result = service.findBrandCountByElement(mergeVO);
        if (result.getCode() != CODE_SUCCESS) {
            return result;
        }
        if (model.getBrandId() == null) {
            service.save(model);
        } else {
            service.update(model);
        }

        return ApiResult.success(MapBuilder.getSimpleMap("id", model.getBrandId())).appendMessage("请求成功");
    }


}
