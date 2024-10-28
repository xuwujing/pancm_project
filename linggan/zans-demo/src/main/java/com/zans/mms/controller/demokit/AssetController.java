package com.zans.mms.controller.demokit;

import com.zans.base.controller.BaseController;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.service.IArpService;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.IDeviceTypeService;
import com.zans.mms.vo.arp.AssetRespVO;
import com.zans.mms.vo.arp.AssetSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import static com.zans.mms.config.PortalGlobalConstants.*;
import static com.zans.base.config.BaseConstants.DATE_RANGE_ARRAY_SIZE;


@Api(value = "demoKit/asset", tags = {"demoKit/asset ~ 资产管理"})
@RestController
@Validated
@RequestMapping("demoKit/asset")
@Slf4j
public class AssetController extends BaseController {




    @Value("${api.export.folder}")
    String exportFolder;

    @Value("${api.upload.folder}")
    String uploadFolder;

    public static Integer LOG_MODULE_ASSET = 2;

    Integer module = LOG_MODULE_ASSET;

    @Autowired
    IArpService arpService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IConstantItemService constantItemService;





    @ApiOperation(value = "/list", notes = "资产查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "AssetSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<AssetRespVO>> list(@RequestBody AssetSearchVO req) {
        super.checkPageParams(req, " update_time desc ");

        List<String> dateRange = req.getAliveDateRange();
        if (dateRange != null && dateRange.size() == DATE_RANGE_ARRAY_SIZE) {
            req.setAliveStartDate(dateRange.get(0));
            req.setAliveEndDate(dateRange.get(1));
        }
        PageResult<AssetRespVO> pageResult = arpService.getAssetPage(req);
        return ApiResult.success(pageResult);
    }

    @ApiOperation(value = "/list_init", notes = "设备综合查询初始化")
    @RequestMapping(value = "/list_init", method = {RequestMethod.GET})
    @ResponseBody
    public ApiResult init() {
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> arpAlive = constantItemService.findItemsByDict(MODULE_ARP_ALIVE);
        List<SelectVO> arpDisStatus = constantItemService.findItemsByDict(MODULE_ARP_DIS_STATUS);
        List<SelectVO> arpMute = constantItemService.findItemsByDict(MODULE_ARP_MUTE);


        //是否可用
        List<SelectVO> enableStatus = constantItemService.findItemsByDict(SYS_DICT_KEY_ENABLE_STATUS);
        //维护状态
        List<SelectVO> maintainStatusList = constantItemService.findItemsByDict(SYS_DICT_KEY_MAINTAIN_STATUS);


        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_ARP_ALIVE, arpAlive)
                .put(MODULE_ARP_DIS_STATUS, arpDisStatus)
                .put(MODULE_ARP_MUTE, arpMute)
                .put(MODULE_DEVICE, deviceTypeList)
                .put(SYS_DICT_KEY_ENABLE,enableStatus)
                .put(SYS_DICT_KEY_MAINTAIN_STATUS,maintainStatusList)
                .build();
        return ApiResult.success(result);
    }


}
