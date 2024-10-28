package com.zans.mms.controller.demokit;

import com.zans.base.annotion.Record;
import com.zans.base.controller.BaseController;
import com.zans.base.vo.ApiResult;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.dao.mms.MacWhiteMapper;
import com.zans.mms.model.MacWhite;
import com.zans.mms.service.IMacWhiteService;
import com.zans.mms.vo.MacWhiteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.common.Mapper;

import java.util.Arrays;
import java.util.List;

import static com.zans.mms.config.MMSConstants.LOG_OPERATION_SAVE;
import static com.zans.mms.config.MMSConstants.MODULE_MAC_WHITE_LIST;
import static com.zans.mms.config.PortalGlobalConstants.LOG_OPERATION_DELETE;

@Api(value = "demoKit/macWhite", tags = {"demoKit/macWhite ~ mac地址"})
@RestController
@Validated
@RequestMapping("demoKit/macWhite")
@Slf4j
public class MacWhiteController extends BaseController {

    @Autowired
    private IMacWhiteService macWhiteService;

    @ApiOperation(value = "查找全部mac地址", notes = "查找全部mac地址")
    @RequestMapping(value = "/selectAllMac", method = {RequestMethod.POST})
    public ApiResult selectAllMac(@RequestBody MacWhiteVO macWhiteVO){
        super.checkPageParams(macWhiteVO);
        return macWhiteService.selectAllMac(macWhiteVO);
    }

    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/delMacByIds", method = {RequestMethod.GET})
    @Record(module = MODULE_MAC_WHITE_LIST,operation = MMSConstants.LOG_OPERATION_DELETE)
    public ApiResult delMacByIds(String ids){
        return macWhiteService.delMac(ids);
    }

    @ApiOperation(value = "添加", notes = "添加")
    @RequestMapping(value = "/insertIntoMac", method = {RequestMethod.POST})
    @Record(module = MODULE_MAC_WHITE_LIST,operation = LOG_OPERATION_SAVE)
    public ApiResult insertIntoMac(@RequestBody MacWhiteVO macWhiteVO){
        return macWhiteService.insertMac(macWhiteVO);
    }



}