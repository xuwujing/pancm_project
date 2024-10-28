package com.zans.portal.controller;

import com.zans.base.annotion.Record;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.AssetBranchMapper;
import com.zans.portal.model.AssetBranch;
import com.zans.portal.model.AssetBranchAsset;
import com.zans.portal.model.AssetBranchStatistics;
import com.zans.portal.service.*;
import com.zans.portal.vo.arp.AssetRespVO;
import com.zans.portal.vo.asset.branch.req.*;
import com.zans.portal.vo.asset.branch.resp.AssetBranchJoinRespVO;
import com.zans.portal.vo.asset.branch.resp.ChoiceDeviceRespVO;
import com.zans.portal.vo.asset.branch.resp.ExcelAssetRunStatisticsVO;
import com.zans.portal.vo.asset.branch.resp.RunningListRespVO;
import com.zans.portal.vo.asset.req.AssetBranchSnapShootReqVO;
import com.zans.portal.vo.asset.req.SeqReqVO;
import com.zans.portal.vo.common.TreeSelect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;
import static com.zans.base.config.EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR;
import static com.zans.base.office.excel.ExportConfig.TOP;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/asset/branch", tags = {"/asset/branch ~ 资产子集"})
@RestController
@Validated
@RequestMapping("/asset/branch")
@Slf4j
public class AssetBranchController extends BasePortalController{
    @Autowired
    IAssetBranchService assetBranchService;

    @Autowired
    IAssetBranchStatisticsService branchStatisticsService;

    @Autowired
    IRadiusEndPointService  endPointService;
    @Autowired
    private IAssetBranchSnapShootService branchSnapShootService;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IAreaService areaService;

    @Value("${api.export.folder}")
    String exportFolder;

    @Autowired
    IFileService fileService;

    @Autowired
    IAssetBranchAssetService assetBranchAssetService;

    @Autowired
    IAssetService assetService;

    @Autowired
    private ISysConstantService sysConstantService;

    @Resource
    private AssetBranchMapper assetBranchMapper;


    @ApiOperation(value = "/statistics/snapshoot/list", notes = "快照查询")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "req", value = "查询条件",
                    dataType = "AssetBranchSnapShootReqVO", paramType = "body")
    })
    @RequestMapping(value = "/statistics/snapshoot/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<AssetRespVO>> getSnapShootList(@RequestBody AssetBranchSnapShootReqVO req) {
        return branchSnapShootService.getAssetBranchSnapShoot(req);
    }

    @ApiOperation(value = "/statistics/snapshoot/save", notes = "快照新增")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "req", value = "快照新增",
                    dataType = "AssetBranchSnapShootReqVO", paramType = "body")
    })
    @RequestMapping(value = "/statistics/snapshoot/save", method = {RequestMethod.POST})
    @ResponseBody
    @Record
    public ApiResult<PageResult<AssetRespVO>> saveSnapShootList(@RequestBody AssetBranchSnapShootReqVO req, HttpServletRequest request) {
        UserSession session = super.getUserSession(request);
        req.setCreatorId(session.getUserId());
        return branchSnapShootService.save(req);
    }


    @ApiOperation(value = "/groups/init", notes = "分组初始化")
    @RequestMapping(value = "/groups/init", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<AssetRespVO>> groupsInit(@RequestParam(value = "name",required = false) String name) {
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> arpAlive = constantItemService.findItemsByDict(MODULE_ARP_ALIVE);
        //是否可用
        List<SelectVO> enableStatus = constantItemService.findItemsByDict(GlobalConstants.SYS_DICT_KEY_ENABLE_STATUS);
        List<SelectVO> assetBranchList = assetBranchService.findToSelect(name);

        List<TreeSelect> treeSelectList =  assetBranchService.findToTreeSelect();

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_ARP_ALIVE, arpAlive)
                .put(MODULE_DEVICE, deviceTypeList)
                .put("assetBranch", assetBranchList)
                .put("treeSelectList", treeSelectList)
                .put(SYS_DICT_KEY_ENABLE,enableStatus)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/groups/list", notes = "分组列表")
    @RequestMapping(value = "/groups/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<AssetBranchStatisticsVO>> groupsList(@RequestBody AssetBranchReqVO reqVO) {
        super.checkPageParams(reqVO, null);
        PageResult<AssetBranchStatisticsVO> assetPage = assetBranchService.assetBranchEndpointPage(reqVO);
        return ApiResult.success(assetPage);
    }

    @ApiOperation(value = "/groups/export", notes = "分组导出")
    @RequestMapping(value = "/groups/export", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ASSET_BRANCH,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void groupsExport(@RequestBody AssetBranchReqVO reqVO,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        log.info("downloadFile#{}", reqVO);
        String filePath = this.exportFolder + "/" +
                "asset_branch_" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "分组设备";
        if (reqVO.getAssetBranchId() != null){
            AssetBranch assetBranch =  assetBranchService.getById(reqVO.getAssetBranchId());
            if (assetBranch != null){
                name = assetBranch.getName();
            }
        }
        String fileCnName = name + "_" + DateHelper.formatDate(new Date(), "yyyyMMddHHmm") + ".xlsx";
        List<ExcelAssetBranchStatisticsVO> list = assetBranchService.assetBranchEndpoint(reqVO);
        log.info("data#{}", list.size());

        if (list == null || list.size() == 0){
            this.setErrorToResponse(request, response, SERVER_DOWNLOAD_NULL_ERROR, SERVER_DOWNLOAD_NULL_ERROR.getMessage());
            return;
        }

        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .extraContentPosition(TOP).extraContent(null)
                .freeze(true).freezeCol(10).freezeRow(1).build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }

    @Record(module = PORTAL_MODULE_ASSET_BRANCH,operation = PORTAL_LOG_OPERATION_ADD)
    @ApiOperation(value = "/groups/add", notes = "分组新增")
    @RequestMapping(value = "/groups/add", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<AssetBranchAddReqVO>> groupsAdd(@RequestBody AssetBranchAddReqVO  req, HttpServletRequest request) {
        Integer count = assetBranchService.findByName(req.getName(), req.getId());
        if (count!=null && count>0) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("设备分组名称已存在#" + req.getName());
        }
        Integer level = req.getLevel();
        if(level!=null && level>3){
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("设备分组级别不允许超过三级#" + req.getName());
        }

        List<AssetBranch> allList = assetBranchService.getAllList();
        SelectVO selectVO = sysConstantService.findSelectVOByKey(ASSET_GROUP_NUM);
        int num = Integer.parseInt(selectVO.getItemValue().trim());
        if (allList != null && allList.size()>num){
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("设备分组数量超过:"+num+" 最大限制" + req.getName());
        }
        Integer nextSeq = assetBranchService.getNextSeq(req.getParentId());
        UserSession userSession = getUserSession(request);
        AssetBranch assetBranch = new AssetBranch();
        assetBranch.setName(req.getName());
        assetBranch.setLevel(level);
        assetBranch.setParentId(req.getParentId());
        assetBranch.setDeleteStatus(DELETE_NOT);
        assetBranch.setCreatorId(userSession.getUserId());
        assetBranch.setUpdateId(userSession.getUserId());
        assetBranch.setSeq(nextSeq);
        assetBranchService.saveSelective(assetBranch);
        return ApiResult.success(req);
    }

    @ApiOperation(value = "/groups/getDetailById", notes = "获取详情,自己、上一个、下一个")
    @RequestMapping(value = "/groups/getDetailById", method = {RequestMethod.GET})
    public ApiResult<AssetBranchJoinRespVO> getDetailById(@NotNull Integer id) {
        AssetBranchJoinRespVO detail = assetBranchService.getDetailById(id);
        return ApiResult.success(detail);
    }

    @ApiOperation(value = "/groups/updateSeq", notes = "更新分组排序")
    @RequestMapping(value = "/groups/updateSeq", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_ASSET_BRANCH,operation = LOG_OPERATION_UPDATE_SEQ)
    public ApiResult updateSeq(@Valid @RequestBody List<SeqReqVO> updateList){
        if (null == updateList || updateList.size() ==0){
            return ApiResult.error("数据异常");
        }
        AssetBranch updateVO = null;
        for (SeqReqVO vo : updateList) {
            updateVO = new AssetBranch();
            updateVO.setId(vo.getId());
            updateVO.setSeq(vo.getSeq());
            assetBranchService.updateSelective(updateVO);
        }
        return ApiResult.success().appendMessage("更新分组排序成功");
    }

    @ApiOperation(value = "/groups/update", notes = "分组更新")
    @RequestMapping(value = "/groups/update", method = {RequestMethod.POST})
    @ResponseBody
    @Record(module = PORTAL_MODULE_ASSET_BRANCH,operation = LOG_OPERATION_GROUP_UPDATE)
    public ApiResult<PageResult<AssetBranchEditReqVO>> groupsUpdate(@RequestBody AssetBranchEditReqVO  req, HttpServletRequest request) {
        Integer count = assetBranchService.findByName(req.getName(), req.getId());
        if (count!=null && count>0) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("设备分组名称已存在#" + req.getName());
        }
        UserSession userSession = getUserSession(request);
        AssetBranch assetBranch = new AssetBranch();
        BeanUtils.copyProperties(req,assetBranch);
        assetBranch.setUpdateId(userSession.getUserId());

        assetBranchService.updateSelective(assetBranch);
        return ApiResult.success(req);
    }
    @Record(module = PORTAL_MODULE_ASSET_BRANCH,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    @ApiOperation(value = "/groups/delete", notes = "分组删除")
    @RequestMapping(value = "/groups/delete", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult groupsDelete(@RequestParam("id") Integer id, HttpServletRequest request) {
        AssetBranch assetBranch = new AssetBranch();
        assetBranch.setId(id);
        List<AssetBranch> assetBranchList = assetBranchMapper.findByParentId(id);
        assetBranchService.delete(assetBranch);
        assetBranchService.deleteAll(assetBranchList);
        return ApiResult.success(true);
    }

//    @Record
//    @ApiOperation(value = "/groups/addAsset", notes = "分组中添加资产")
//    @RequestMapping(value = "/groups/addAsset", method = {RequestMethod.POST})
//    @ResponseBody
//    public ApiResult groupsAddAsset(@RequestBody List<AssetBranchEndpointAddReqVO>  reqList, HttpServletRequest request) {
//        UserSession userSession = getUserSession(request);
//        for (AssetBranchEndpointAddReqVO req : reqList) {
//            branchEndPointService.groupsAddAsset(req,userSession);
//        }
//        return ApiResult.success();
//    }
    @Record(module = PORTAL_MODULE_ASSET_BRANCH,operation = PORTAL_LOG_OPERATION_ADD)
    @ApiOperation(value = "/groups/addAsset", notes = "分组中添加资产")
    @RequestMapping(value = "/groups/addAsset", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult groupsAddAsset(@RequestBody List<AssetBranchAssetAddReqVO>  reqList, HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        for (AssetBranchAssetAddReqVO req : reqList) {
            assetBranchAssetService.groupsAddAsset(req,userSession);
        }
        return ApiResult.success();
    }

    @Record(module = PORTAL_MODULE_ASSET_BRANCH,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    @ApiOperation(value = "/groups/removeAsset", notes = "分组移除资产")
    @RequestMapping(value = "/groups/removeAsset", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult groupsRemoveAsset(@RequestParam("id") Integer id, HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        AssetBranchAsset assetBranchAsset = new AssetBranchAsset();
        assetBranchAsset.setId(id);
        assetBranchAsset.setUpdateId(userSession.getUserId());
        assetBranchAsset.setDeleteStatus(DELETE_DONE);

        assetBranchAssetService.updateSelective(assetBranchAsset);
        return ApiResult.success();
    }

    @Record(module = PORTAL_MODULE_ASSET_BRANCH,operation = LOG_OPERATION_DISPOSE)
    @ApiOperation(value = "/groups/disposeAsset", notes = "资产处置")
    @RequestMapping(value = "/groups/disposeAsset", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult groupsDisposeAsset(@RequestBody AssetBranchDisposeReqVO reqVO, HttpServletRequest request) {
        //处置停用并不会从设备分组中移除
        if (reqVO.getEnableStatus()==null || (reqVO.getEnableStatus()!=0 && reqVO.getEnableStatus() !=1)){
            return ApiResult.error(CLIENT_PARAMS_ERROR);
        }
        UserSession userSession = getUserSession(request);
        assetService.disposeAsset(reqVO,userSession);
        return ApiResult.success();
    }


    @ApiOperation(value = "/groups/choiceDeviceInit", notes = "选择设备init")
    @RequestMapping(value = "/groups/choiceDeviceInit", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult choiceDeviceInit() {
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> arpAlive = constantItemService.findItemsByDict(MODULE_ARP_ALIVE);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(MODULE_ARP_ALIVE, arpAlive)
                .put(MODULE_DEVICE, deviceTypeList)
        //        .put(INIT_DATA_TABLE, assetPage)
                .build();
        return ApiResult.success(result);
    }

    @ApiOperation(value = "/groups/choiceDevice", notes = "选择设备")
    @RequestMapping(value = "/groups/choiceDevice", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<ChoiceDeviceRespVO>> choiceDevice(@RequestBody ChoiceDeviceReqVO reqVO) {
        super.checkPageParams(reqVO, null);
        PageResult<ChoiceDeviceRespVO> assetPage = assetBranchService.choiceDevicePage(reqVO);
        return ApiResult.success(assetPage);
    }


    @ApiOperation(value = "/statistics/running/list", notes = "运行统计")
    @RequestMapping(value = "/statistics/running/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<RunningListRespVO>> runningList(@RequestBody RunningListReqVO  reqVO) {
        PageResult<RunningListRespVO> assetPage = null;
        super.checkPageParams(reqVO, null);
        if (StringUtils.isEmpty(reqVO.getStatisticsTime()) ){
            //如果运行统计时间是空 就实施查询
             assetPage = assetBranchService.syncStatistics(reqVO);
            return ApiResult.success(assetPage);
        }

         assetPage = assetBranchService.runningList(reqVO);

        return ApiResult.success(assetPage);
    }
    @ApiOperation(value = "/statistics/timeList", notes = "统计时间列表")
    @RequestMapping(value = "/statistics/timeList", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<List<String>> timeList() {
        List<String> timeList = assetBranchService.timeList();
        return ApiResult.success(timeList);
    }

    @ApiOperation(value = "/statistics/running/export", notes = "分组导出")
    @RequestMapping(value = "/statistics/running/export", method = {RequestMethod.POST})
    @ResponseBody
    @Record(module = PORTAL_MODULE_ASSET_BRANCH,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void runningListExport(@RequestBody RunningListReqVO reqVO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.info("downloadFile#{}", reqVO);
        String filePath = this.exportFolder + "/" +
                "asset_statistics_list" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "分组设备列表统计快照";
        String title = DateHelper.getTodayShort();
        if (StringUtils.isNotBlank(reqVO.getStatisticsTime())) {
            title = reqVO.getStatisticsTime().replaceAll("-", "").replaceAll(" ", "_").replaceAll(":","");
        }
        String fileCnName = name + "_" + title + ".xlsx";
        List<ExcelAssetRunStatisticsVO > list = assetBranchService.runningListExport(reqVO);
        log.info("data#{}", list.size());

        if (list == null || list.size() == 0){
            this.setErrorToResponse(request, response, SERVER_DOWNLOAD_NULL_ERROR, SERVER_DOWNLOAD_NULL_ERROR.getMessage());
            return;
        }

        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .extraContentPosition(TOP).extraContent(null)
                .freeze(true).freezeCol(10).freezeRow(1).build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }




    @ApiOperation(value = "/statistics/running/detailList", notes = "运行统计详情list")
    @RequestMapping(value = "/statistics/running/detailList", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult<PageResult<AssetBranchStatisticsVO>> runningDetailList(@RequestBody RunningDetailListReqVO   reqVO) {
        super.checkPageParams(reqVO, null);
        PageResult<AssetBranchStatisticsVO> assetPage = assetBranchService.runningDetailListPage(reqVO);
        return ApiResult.success(assetPage);
    }

    @ApiOperation(value = "/statistics/detailList/export", notes = "分组导出")
    @RequestMapping(value = "/statistics/detailList/export", method = {RequestMethod.POST})
    @ResponseBody
    @Record(module = PORTAL_MODULE_ASSET_BRANCH,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void detailListExport(@RequestBody RunningDetailListReqVO reqVO,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        log.info("downloadFile#{}", reqVO);
        String filePath = this.exportFolder + "/" +
                "asset_statistics_detail" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "分组设备";
        if (reqVO.getAssetBranchStatisticsId() != null) {
            AssetBranchStatistics branchStatis = branchStatisticsService.getById(reqVO.getAssetBranchStatisticsId());
            if (branchStatis.getAssetBranchId() != null){
                AssetBranch assetBranch =  assetBranchService.getById(branchStatis.getAssetBranchId());
                if (assetBranch != null){
                    name = assetBranch.getName();
                }
            }
        }
        String fileCnName = name + "_统计快照详情_" + DateHelper.formatDate(new Date(), "yyyyMMddHHmm") + ".xlsx";
        List<ExcelAssetBranchStatisticsVO> list = assetBranchService.runningDetailList(reqVO);
        log.info("data#{}", list.size());

        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .extraContentPosition(TOP).extraContent(null)
                .freeze(true).freezeCol(10).freezeRow(1).build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }
}
