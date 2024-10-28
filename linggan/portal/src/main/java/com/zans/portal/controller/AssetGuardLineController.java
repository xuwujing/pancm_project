package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.config.EnumErrorCode;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.*;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.AssetGuardLine;
import com.zans.portal.model.AssetGuardLineAsset;
import com.zans.portal.service.*;
import com.zans.portal.vo.asset.branch.req.AssetBranchAddReqVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchEditReqVO;
import com.zans.portal.vo.asset.branch.resp.ChoiceDeviceRespVO;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineAssetAddReqVO;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineReqVO;
import com.zans.portal.vo.asset.guardline.req.GuardLineChoiceDeviceReqVO;
import com.zans.portal.vo.asset.guardline.req.GuardLineForceOnlineOfflineReqVO;
import com.zans.portal.vo.asset.guardline.resp.*;
import com.zans.portal.vo.asset.req.SeqReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/asset/guardline", tags = { "/asset/guardline ~ 警卫线路" })
@RestController
@Validated
@RequestMapping("/asset/guardline")
@Slf4j
public class AssetGuardLineController extends BasePortalController {

    @Autowired
    IAssetGuardLineService assetGuardLineService;

    @Autowired
    IAssetGuardLineAssetService assetGuardLineAssetService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IFileService fileService;

    @Value("${api.export.folder}")
    String exportFolder;


    @Value("${api.upload.folder}")
    String uploadFolder;


    private static Map<String, List<ChoiceDeviceRespVO>> choiceMap = new ConcurrentHashMap<>();

    @ApiOperation(value = "/init", notes = "初始化")
    @RequestMapping(value = "/init", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult initList(@RequestParam(value = "name", required = false) String name) {
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> arpAlive = constantItemService.findItemsByDict(MODULE_ARP_ALIVE);
        // 是否可用
        List<SelectVO> enableStatus = constantItemService.findItemsByDict(GlobalConstants.SYS_DICT_KEY_ENABLE_STATUS);
        List<AssetGuardLineRespVO> guardLineRespVOs = assetGuardLineService.getListByName(name);
        Map<String, Object> result = MapBuilder.getBuilder().put(MODULE_ARP_ALIVE, arpAlive)
                .put(MODULE_DEVICE, deviceTypeList)
                .put("assetGuardline", guardLineRespVOs)
                .put(SYS_DICT_KEY_ENABLE, enableStatus).build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/list", notes = "线路资产列表")
    @RequestMapping(value = "/list", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult<List<AssetGuardLineListVO>> groupsList(@RequestBody AssetGuardLineReqVO reqVO) {
        super.checkPageParams(reqVO, null);
        List<AssetGuardLineListVO> assetPage = assetGuardLineService.getGuardLineAsset(reqVO);
        return ApiResult.success(assetPage);
    }

    @ApiOperation(value = "/export", notes = "线路资产列表导出")
    @RequestMapping(value = "/export", method = { RequestMethod.POST })
    @ResponseBody
    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void exportLineAsset(@RequestBody AssetGuardLineReqVO reqVO, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        log.info("downloadFile#{}", reqVO);
        String filePath = this.exportFolder + "/" + "asset_guardline_" + DateHelper.getTodayShort() + "_"
                + StringHelper.getUuid() + ".xlsx";
        String name = "警卫线路设备";
        if (reqVO.getAssetGuardLineId() != null) {
            AssetGuardLine assetBranch = assetGuardLineService.getById(reqVO.getAssetGuardLineId());
            if (assetBranch != null) {
                name = assetBranch.getName() + "_警卫线路设备";
            }
        }
        String fileCnName = name + "_" + DateHelper.formatDate(new Date(), "yyyyMMddHHmm") + ".xlsx";
        List<ExcelAssetGuardLineStatisticsVO> list = assetGuardLineService.getGuardListExcelList(reqVO);
        log.info("data#{}", list.size());

        if (list == null || list.size() == 0) {
            this.setErrorToResponse(request, response, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR,
                    EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR.getMessage());
            return;
        }

        ExportConfig exportConfig = ExportConfig.builder().seqColumn(true).wrap(true)
                .extraContentPosition(ExportConfig.TOP).extraContent(null).freeze(true).freezeCol(10).freezeRow(1)
                .build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }


    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = PORTAL_LOG_OPERATION_ADD)
    @ApiOperation(value = "/addLine", notes = "新增线路经纬度")
    @RequestMapping(value = "/addLine", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult addLine(@RequestParam("file") MultipartFile file,
                             @RequestParam(name = "assetGuardLineId", required = false) Integer assetGuardLineId,
                             @RequestParam(name = "startName", required = false) String startName,
                             @RequestParam(name = "endName", required = false) String endName,
                             HttpServletRequest request) {

        // 上传文件，持久化到本地，写数据库
        String originName = file.getOriginalFilename();
        String newFileName = fileService.getNewFileName(originName);
        boolean saved = FileHelper.saveFile(file, uploadFolder, newFileName);
        if (saved) {
            UserSession userSession = getUserSession(request);

        }
        return ApiResult.success();
    }



    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = PORTAL_LOG_OPERATION_ADD)
    @ApiOperation(value = "/add", notes = "新增警卫线路")
    @RequestMapping(value = "/add", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult addGuardLine(@RequestBody AssetBranchAddReqVO req, HttpServletRequest request) {
        if (assetGuardLineService.isNameExist(req.getName(), req.getId())) {
            return ApiResult.error(EnumErrorCode.CLIENT_PARAMS_ERROR).appendMessage("警卫线路名称已存在#" + req.getName());
        }
        if (!assetGuardLineService.canAddNewLine()) {
            return ApiResult.error(EnumErrorCode.CLIENT_PARAMS_ERROR).appendMessage("警卫线路最多只能添加30条");
        }
        Integer nextSeq = assetGuardLineService.getNextSeq();
        UserSession userSession = getUserSession(request);
        AssetGuardLine guardLine = new AssetGuardLine();
        guardLine.setName(req.getName());
        guardLine.setCreatorId(userSession.getUserId());
        guardLine.setUpdateId(userSession.getUserId());
        guardLine.setSeq(nextSeq);
        guardLine.setDeleteStatus(GlobalConstants.DELETE_NOT);
        assetGuardLineService.saveSelective(guardLine);
        return ApiResult.success();
    }

    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = PORTAL_LOG_OPERATION_EDIT)
    @ApiOperation(value = "/update", notes = "分组更新")
    @RequestMapping(value = "/update", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult updateGuardLine(@RequestBody AssetBranchEditReqVO req, HttpServletRequest request) {
        if (assetGuardLineService.isNameExist(req.getName(), req.getId())) {
            return ApiResult.error(EnumErrorCode.CLIENT_PARAMS_ERROR).appendMessage("警卫线路名称已存在#" + req.getName());
        }
        UserSession userSession = getUserSession(request);
        AssetGuardLine guardLine = new AssetGuardLine();
        BeanUtils.copyProperties(req, guardLine);
        guardLine.setUpdateId(userSession.getUserId());
        assetGuardLineService.updateSelective(guardLine);
        return ApiResult.success();
    }

    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    @ApiOperation(value = "/delete", notes = "分组删除")
    @RequestMapping(value = "/delete", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult deleteGuardLine(@RequestParam(name = "id", required = true) Integer id,
            HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        AssetGuardLine guardLine = new AssetGuardLine();
        guardLine.setId(id);
        guardLine.setUpdateId(userSession.getUserId());
        guardLine.setDeleteStatus(GlobalConstants.DELETE_DONE);
        guardLine.setEnableFastScan(GlobalConstants.STATUS_DISABLE);
        assetGuardLineService.updateSelective(guardLine);
        return ApiResult.success(true);
    }

    @ApiOperation(value = "/updateSeq", notes = "更新排序")
    @RequestMapping(value = "/updateSeq", method = { RequestMethod.POST })
    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = LOG_OPERATION_UPDATE_SEQ)
    public ApiResult updateSeq(@Valid @RequestBody List<SeqReqVO> updateList) {
        if (null == updateList || updateList.size() == 0) {
            return ApiResult.error("数据异常");
        }
        AssetGuardLine updateVO = null;
        for (SeqReqVO vo : updateList) {
            updateVO = new AssetGuardLine();
            updateVO.setId(vo.getId());
            updateVO.setSeq(vo.getSeq());
            assetGuardLineService.updateSelective(updateVO);
        }
        return ApiResult.success().appendMessage("更新排序成功");
    }

    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = PORTAL_LOG_OPERATION_ADD)
    @ApiOperation(value = "/addAsset", notes = "线路中添加资产")
    @RequestMapping(value = "/addAsset", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult addAsset(@Valid @RequestBody List<AssetGuardLineAssetAddReqVO> reqList,
            HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        for (AssetGuardLineAssetAddReqVO req : reqList) {
            assetGuardLineAssetService.addAssetToLine(req, userSession);
        }
        return ApiResult.success();
    }

    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    @ApiOperation(value = "/removeAsset", notes = "分组移除资产")
    @RequestMapping(value = "/removeAsset", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult removeAsset(@RequestBody List<Integer> id, HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        for (Integer i : id) {
            AssetGuardLineAsset asset = new AssetGuardLineAsset();
            asset.setId(i);
            asset.setUpdateId(userSession.getUserId());
            asset.setDeleteStatus(GlobalConstants.DELETE_DONE);
            assetGuardLineAssetService.updateSelective(asset);
        }
        return ApiResult.success();
    }

    @ApiOperation(value = "/choiceDevice", notes = "选择设备")
    @RequestMapping(value = "/choiceDevice", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult<PageResult<ChoiceDeviceRespVO>> choiceDevice(@RequestBody GuardLineChoiceDeviceReqVO reqVO) {
        super.checkPageParams(reqVO, null);
        PageResult<ChoiceDeviceRespVO> assetPage = assetGuardLineService.choiceDevice(reqVO);
        return ApiResult.success(assetPage);
    }

    @ApiOperation(value = "/choiceDevice", notes = "选择设备缓存")
    @RequestMapping(value = "/choiceDevice/cache", method = { RequestMethod.POST })
    @ResponseBody
    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = LOG_OPERATION_CHOOSE_DEVICE_CACHE)
    public ApiResult<PageResult<ChoiceDeviceRespVO>> choiceDeviceCache(@RequestBody List<ChoiceDeviceRespVO> list,HttpServletRequest request) {
        UserSession userSession =   getUserSession(request);
        List<ChoiceDeviceRespVO> choiceDeviceRespVOS = choiceMap.get(userSession.getUserName());
        if(StringHelper.isEmpty(choiceDeviceRespVOS)){
            choiceMap.put(userSession.getUserName(),list);
            choiceDeviceRespVOS = list;
        }else {
            choiceDeviceRespVOS.addAll(list);
            choiceMap.put(userSession.getUserName(),choiceDeviceRespVOS);
        }
        return ApiResult.success(choiceDeviceRespVOS);
    }




    @ApiOperation(value = "/choiceDevice", notes = "选择设备缓存")
    @RequestMapping(value = "/choiceDevice/cacheClear", method = { RequestMethod.POST })
    @ResponseBody
    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = LOG_OPERATION_NO_CHOOSE_DEVICE_CACHE)
    public ApiResult<PageResult<ChoiceDeviceRespVO>> choiceDeviceCacheClear(@RequestBody List<ChoiceDeviceRespVO> list,HttpServletRequest request) {
        UserSession userSession =  getUserSession(request);
        List<ChoiceDeviceRespVO> choiceDeviceRespVOS = choiceMap.get(userSession.getUserName());
        if(StringHelper.isEmpty(choiceDeviceRespVOS)){
            return ApiResult.success();
        }
        choiceDeviceRespVOS.removeAll(list);
        choiceMap.put(userSession.getUserName(),choiceDeviceRespVOS);
        return ApiResult.success(choiceDeviceRespVOS);
    }


    @ApiOperation(value = "/running/list", notes = "运行统计")
    @RequestMapping(value = "/running/list", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult<PageResult<GuardLineRunningListRespVO>> runningList(@RequestBody BasePage reqVO) {
        super.checkPageParams(reqVO, null);
        return ApiResult.success(assetGuardLineAssetService.runningList(reqVO));
    }

    @ApiOperation(value = "/running/export", notes = "线路统计导出")
    @RequestMapping(value = "/running/export", method = { RequestMethod.GET })
    @ResponseBody
    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void runningListExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filePath = this.exportFolder + "/" + "asset_guardline_statistics_list"
                + DateHelper.getDownloadFileSuffix() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "警卫线路统计";
        String fileCnName = name + "_" + DateHelper.getDownloadFileSuffix() + ".xlsx";
        List<ExcelGuardLineRunningListRespVO> list = assetGuardLineAssetService.runningListExport();
        log.info("data#{}", list.size());

        if (list == null || list.size() == 0) {
            this.setErrorToResponse(request, response, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR,
                    EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR.getMessage());
            return;
        }

        ExportConfig exportConfig = ExportConfig.builder().seqColumn(true).wrap(true)
                .extraContentPosition(ExportConfig.TOP).extraContent(null).freeze(true).freezeCol(10).freezeRow(1)
                .build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }

    @ApiOperation(value = "/getDetailById", notes = "获取详情,自己、上一个、下一个")
    @RequestMapping(value = "/getDetailById", method = { RequestMethod.GET })
    public ApiResult<AssetGuardLineJoinRespVO> getDetailById(@NotNull Integer id) {
        AssetGuardLineJoinRespVO detail = assetGuardLineService.getDetailById(id);
        return ApiResult.success(detail);
    }

    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = LOG_OPERATION_FORCE_OFFLINE)
    @ApiOperation(value = "/forceOffline", notes = "强制下线")
    @RequestMapping(value = "/forceOffline", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult forceOffline(@RequestBody GuardLineForceOnlineOfflineReqVO reqVo, HttpServletRequest request) {
        if (reqVo == null || reqVo.getPolicy() == null) {
            return ApiResult.error("参数传输错误");
        }
        assetGuardLineService.execForceOfflineCommand(reqVo.getAssetId(),reqVo.getIpAddr(), reqVo.getPolicy(), getUserSession(request));
        return ApiResult.success();
    }

    @Record(module = PORTAL_MODULE_GUARD_LINE,operation = LOG_OPERATION_FORCE_ONLINE)
    @ApiOperation(value = "/forceOnline", notes = "恢复入网")
    @RequestMapping(value = "/forceOnline", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult forceOnline(@RequestBody GuardLineForceOnlineOfflineReqVO reqVo, HttpServletRequest request) {
        if (reqVo == null || reqVo.getPolicy() == null) {
            return ApiResult.error("参数传输错误");
        }
        assetGuardLineService.execForceOnlineCommand(reqVo.getAssetId(),reqVo.getIpAddr(), reqVo.getPolicy(), getUserSession(request));
        return ApiResult.success();
    }

    /**
     *
     * @return
     */
    @ApiOperation(value = "/commandExecResult", notes = "强制下线执行结果查询")
    @RequestMapping(value = "/commandExecResult", method = { RequestMethod.POST })
    @ResponseBody
    public ApiResult commandExecResult(@RequestBody List<Integer> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            List<GuardLineForceCommandObjectVO> list = assetGuardLineAssetService.getCommandExecResult(idList);
            return ApiResult.success(list);
        }
        return ApiResult.success();
    }
}
