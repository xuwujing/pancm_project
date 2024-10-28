package com.zans.portal.controller;

import com.alibaba.fastjson.JSON;
import com.zans.base.annotion.Record;
import com.zans.base.config.BaseConstants;
import com.zans.base.config.EnumErrorCode;
import com.zans.base.office.excel.ExcelHelper;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.SysSwitcherBranch;
import com.zans.portal.service.*;
import com.zans.portal.vo.switcher.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;

import static com.zans.base.config.EnumErrorCode.*;
import static com.zans.portal.config.GlobalConstants.*;

@Api(value = "/switchBrunch", tags = {"/switchBrunch ~ 交换机"})
@RestController
@RequestMapping("/switchBrunch")
@Slf4j
@Validated
public class SwitchBranchController extends BasePortalController {

    @Autowired
    ISwitcherBrunchService switcherBrunchService;
    @Autowired
    IAreaService areaService;
    @Autowired
    ILogOperationService logOperationService;
    @Autowired
    IConstantItemService constantItemService;
    @Autowired
    IFileService fileService;

    @Value("${api.export.folder}")
    String exportFolder;
    @Value("${api.upload.folder}")
    String uploadFolder;
    @Autowired
    ISysBrandService brandService;


    @ApiOperation(value = "/list", notes = "交换机查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "SwitchBranchSearchVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult getList(@Valid @RequestBody SwitchBranchSearchVO req) {
        String sortName = req.getSortName();
        if (StringUtils.isEmpty(sortName)) {
//            sortName = " id desc ";
            sortName = " s.create_time desc ";
        } else {
//            sortName = sortName + " ,id desc ";
            sortName = sortName + " ,s.create_time desc ";
        }
        super.checkPageParams(req, sortName);


        List<SelectVO> regionSecondList = areaService.findRegionToSelect(REGION_LEVEL_TWO);
        List<SelectVO> switcher = constantItemService.findItemsByDict(SWITCHER_TYPE);
        switcher.forEach(it->it.setItemValue(it.getItemValue().replace("层","")));

        List<SelectVO> brandList = new ArrayList<>();
        brandService.getAll().forEach(e -> {
            SelectVO vo = new SelectVO();
            vo.setItemKey(e.getBrandId());
            vo.setItemValue(e.getBrandName());
            brandList.add(vo);
        });
        PageResult<SwitchBranchResVO> pageResult = switcherBrunchService.getSwitchPage(req);
        Integer[] statusArray={0,1};
        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .put("online", getStickSelectVOS(new Integer[]{0,2,1},new String[]{"离线（断光）","离线（掉电）","在线"}))
                .put(MODULE_SWITCHER, switcher)
                .put(MODULE_REGION_SECOND, regionSecondList)
                .put("brand", brandList)
                .put("acceptance", getStickSelectVOS(statusArray,new String[]{"未验收","已验收"}))
                .put("status", getStickSelectVOS(statusArray,new String[]{"启用","停用"}))
                .put("consBatch", getStickSelectVOS(new Integer[]{1,2},new String[]{"一期","二期"}))
                .build();
        return ApiResult.success(result);
    }


    private List<SelectVO> getStickSelectVOS(Integer[] id,String[] name) {
        List<SelectVO> onlineVo = new ArrayList<>();
        for (int i = 0; i < id.length; i++) {
            SelectVO online1=new SelectVO();
            online1.setItemKey(id[i]);
            online1.setItemValue(name[i]);
            onlineVo.add(online1);
        }
        return onlineVo;
    }

    @ApiOperation(value = "/getAreaByParent", notes = "获取大队")
    @ApiImplicitParam(name = "parentId", value = "parentId", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/getAreaByParent", method = {RequestMethod.GET})
    @Record
    public ApiResult getAreaByParent(@RequestParam("parentId") Integer parentId, HttpServletRequest request){
        List<SelectVO> regionList = null;
        List<SelectVO> resultList = new ArrayList<>();
        regionList.forEach(it->{
            SelectVO selectVO = new SelectVO();
            BeanUtils.copyProperties(it,selectVO);
            selectVO.setItemValue(selectVO.getItemValue()+"大队");
            resultList.add(selectVO);
        });
        return ApiResult.success(resultList);
    }

    @ApiOperation(value = "/acceptance", notes = "批量验收")
    @ApiImplicitParam(name = "acceptVO", value = "批量验收", required = true, dataType = "SwitchBranchAcceptVO", paramType = "body")
    @RequestMapping(value = "/acceptance", method = {RequestMethod.POST})
    @Record
    public ApiResult acceptance(@Valid @RequestBody SwitchBranchAcceptVO acceptVO, HttpServletRequest request){
       return switcherBrunchService.batchAcceptance(acceptVO);
    }

    @ApiOperation(value = "/delete", notes = "删除交换机")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record
    public ApiResult delete(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        ApiResult  result = switcherBrunchService.deleteSwitcherBranch(id);
        if (result.getCode() == BaseConstants.CODE_SUCCESS)
          insertLog(id+"", request,LOG_MODULE_SWITCH_BRANCH,GlobalConstants.LOG_OPERATION_DELETE);
        return result;
    }

    private void insertLog(String id, HttpServletRequest request,Integer module,String operation) {
        // 记录日志
        LogOperation logOperation = new LogOperation();
        logOperation.setTraceId(UUID.randomUUID().toString());
        logOperation.setModule(module+"");
        logOperation.setOperation(operation);
        logOperation.setResult(GlobalConstants.LOG_RESULT_SUCCESS);
        logOperationService.save(logOperation);
    }


    @ApiOperation(value = "/insert", notes = "新增交换机")
    @ApiImplicitParam(name = "mergeVO", value = "新增交换机", required = true, dataType = "SwitchBranchMergeVO", paramType = "body")
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @Record
    public ApiResult insert(@Valid @RequestBody SwitchBranchMergeVO mergeVO, HttpServletRequest request) {
        SysSwitcherBranch switcher = switcherBrunchService.findBySwHost(mergeVO.getIpAddr(), null);
        if (switcher != null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前Ip已存在#" + mergeVO.getIpAddr());
        }
        int id = switcherBrunchService.insertSwitcherBranch(mergeVO,getUserSession(request));
        insertLog(JSON.toJSONString(mergeVO), request,LOG_MODULE_SWITCH_BRANCH,mergeVO.getId() == null ? GlobalConstants.LOG_OPERATION_ADD : LOG_OPERATION_EDIT);
        return ApiResult.success(MapBuilder.getSimpleMap("id",id)).appendMessage("请求成功");
    }

    @ApiOperation(value = "/update", notes = "修改交换机")
    @ApiImplicitParam(name = "mergeVO", value = "修改交换机", required = true,  dataType = "SwitchBranchMergeVO", paramType = "body")
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @Record
    public ApiResult update(@Valid @RequestBody SwitchBranchMergeVO mergeVO, HttpServletRequest request) {
        if (mergeVO.getId() == null){
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("主键id不能为空#" + mergeVO.getIpAddr());
        }
        SysSwitcherBranch sysSwitcher = switcherBrunchService.findBySwHost(mergeVO.getIpAddr(),null);
        if (sysSwitcher == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前交换机不存在#" + mergeVO.getId());
        }
        if (sysSwitcher.getId().intValue() != mergeVO.getId()) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前Ip已存在#" + mergeVO.getIpAddr());
        }
        switcherBrunchService.editSwitcherBranch(sysSwitcher,mergeVO,getUserSession(request));

//        insertLog(JSON.toJSONString(mergeVO), request,LOG_MODULE_SWITCH_BRANCH,mergeVO.getId() == null ? GlobalConstants.LOG_OPERATION_ADD : LOG_OPERATION_EDIT);
        return ApiResult.success(MapBuilder.getSimpleMap("id", sysSwitcher.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/dispose", notes = "批量处置")
    @ApiImplicitParam(name = "disposeVO", value = "批量处置", required = true,  dataType = "SwitchBranchDisposeVO", paramType = "body")
    @RequestMapping(value = "/dispose", method = {RequestMethod.POST})
    @Record
    public ApiResult  dispose(@Valid @RequestBody SwitchBranchDisposeVO disposeVO, HttpServletRequest request){
        if (disposeVO.getId() != null){
            disposeVO.addId(disposeVO.getId());
        }
        if (disposeVO.getStatus() == null){
            return ApiResult.error(CLIENT_PARAMS_ERROR);
        }
        switcherBrunchService.dispose(disposeVO,getUserSession(request));
        insertLog(JSON.toJSONString(disposeVO), request,LOG_MODULE_SWITCH_BRANCH,  GlobalConstants.LOG_OPERATION_ENABLE  );
        return ApiResult.success(MapBuilder.getSimpleMap("id", disposeVO.getIds())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/download/template", notes = "文件下载")
    @GetMapping("/download/template")
    @Record
    public void downloadFile(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String filePath = this.uploadFolder+"template/前端交换机导入模板.xlsx";
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "交换机导入模板" + date+".xlsx";
        this.download(filePath, fileCnName, request, response);
    }

    @ApiOperation(value = "/upload", notes = "上传文件，post参数id")
    @ApiResponse(code = 0,message = "",responseContainer = "totalCount=3,successCount=1,errorCount=2,errorFilePath=")
    @PostMapping("/upload")
    @ResponseBody
    @Record
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,   HttpServletRequest request) throws Exception {
        if (!ExcelHelper.checkExtension(file)) {
            return ApiResult.error(SERVER_UPLOAD_MIME_ERROR).appendMessage("不是Excel类型");
        }
        long size = file.getSize();
        if (size > UPLOAD_FILE_MAX_SIZE) {
            return ApiResult.error(SERVER_UPLOAD_MAX_SIZE_ERROR)
                    .appendMessage("最大" + FileHelper.getSizeInMb(UPLOAD_FILE_MAX_SIZE) + "MB");
        }

        // 上传文件，持久化到本地，写数据库
        String originName = file.getOriginalFilename();
        String newFileName = fileService.getNewFileName(originName);
        boolean saved = FileHelper.saveFile(file, uploadFolder, newFileName);
        if (saved){
            UserSession userSession = getUserSession(request);
            return  switcherBrunchService.batchAddSwitcher(newFileName, originName,userSession);
        }
        return ApiResult.success();
    }

    @ApiOperation(value = "/download/errorFile", notes = "下载上传失败的文件")
    @RequestMapping(value = "/download/errorFile", method = {RequestMethod.GET})
    @Record
    public void errorFile(HttpServletRequest request,
                          HttpServletResponse response, @RequestParam(value = "errorFilePath") String errorFilePath) throws Exception {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "前端交换机导入上传失败" + date+".xlsx";
        this.download(errorFilePath, fileCnName, request, response);
    }

    @ApiOperation(value = "/export", notes = "导出")
    @RequestMapping(value = "/export", method = {RequestMethod.POST})
    public void export( @RequestBody SwitchBranchSearchVO req ,HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("downloadFile#===========");
        String sortName = req.getSortName();
        if (StringUtils.isEmpty(sortName)) {
            sortName = " id desc ";
        } else {
            sortName = sortName + " ,id desc ";
        }
        super.checkPageParams(req, sortName);
        String filePath = this.exportFolder + "交换机导出_" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "交换机";
        String fileCnName = name + "_" + DateHelper.getTodayShort() + ".xlsx";
        req.setPageSize(10000);
        PageResult<SwitchBranchResVO> pageResult = switcherBrunchService.getSwitchPage(req);
        if (pageResult.getList().size()==0){
            this.setErrorToResponse(request, response, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR, EnumErrorCode.SERVER_DOWNLOAD_NULL_ERROR.getMessage());
            return;
        }
        List<ExcelSwitchBranchExportVO> list = new ArrayList<>();
        for (SwitchBranchResVO it : pageResult.getList()) {
            ExcelSwitchBranchExportVO exportVO = new ExcelSwitchBranchExportVO();
            BeanUtils.copyProperties(it,exportVO);
            exportVO.setBrandName(it.getBrandName());
            exportVO.setModel(it.getModel());
            if (it.getAcceptance()!=null)
             exportVO.setAcceptance(it.getAcceptance()==1?"已验收":"未验收");
            exportVO.setAcceptDate(it.getAcceptDate());
            exportVO.setLat(it.getLat().toString());
            exportVO.setLon(it.getLon().toString());
            String useInterface="";
            if(it.getScanMacAll() != null && it.getScanInterfaceCount() != null){
                useInterface = it.getScanMacAll()+"/"+it.getScanInterfaceCount();
            }else if(it.getScanMacAll() != null){
                useInterface = it.getScanMacAll()+"/0";
            }else if(it.getScanInterfaceCount() != null){
                useInterface = "0/"+it.getScanInterfaceCount();
            }
            ///这里和查询显示的逻辑保持一致，如果在资产表查不到，就设置为离线（断光）
            if(it.getAlive()!=null){
                if(it.getAlive() == 1){
                    exportVO.setOfflineName("在线");
                }else if(it.getAlive() == 2){
                    if(it.getOfflineType()!=null){
                        if(it.getOfflineType() == 1){
                            exportVO.setOfflineName("离线（断电）");
                        }else {
                            exportVO.setOfflineName("离线（断光）");
                        }
                    }else {
                        exportVO.setOfflineName("离线（断光）");
                    }
                }
            }else {
                exportVO.setOfflineName("离线（断光）");
            }
            //接入设备数
            exportVO.setScanMacAll(it.getScanMacAlive());
            exportVO.setUseInterface(useInterface);
            list.add(exportVO);
        }
        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }


}
